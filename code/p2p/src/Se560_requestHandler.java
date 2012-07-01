package com.se560.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.jdo.JDOHelper;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


public class Se560_requestHandler {

	private final ConcurrentSkipListSet<String> urls = new ConcurrentSkipListSet<String>();
	private final ConcurrentHashMap<String,String> users = new ConcurrentHashMap<String,String>();
	
	private final PersistenceManagerFactory pmfInstance = 
						JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private final PersistenceManager pm = pmfInstance.getPersistenceManager();
	
	public Se560_requestHandler()
	{
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> checkData = (List<UserData>) pm.newQuery(query).execute();
		if (checkData.size() == 0)
		{
			UserData ud = new UserData("http://se560cp.elasticbeanstalk.com");
			try {
				pm.makePersistent(ud);
			} 
			finally { }
		}
		@SuppressWarnings("unchecked")
		List<UserData> loadData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData data : loadData)
		{ urls.add(data.getURL()); }
		
		populateUsers();

		String isSubscriptionsOn = "select from " + Subscriptions.class.getName();
		@SuppressWarnings("unchecked")
		List<Subscriptions> status = (List<Subscriptions>) pm.newQuery(isSubscriptionsOn).execute();
		if (status.size() == 0)
		{
			Subscriptions subscriptions = new Subscriptions();
			try {
				pm.makePersistent(subscriptions);
			} 
			finally { }
		}
	}
	
	public void requestDispatcher(HttpServletRequest request, 
			HttpServletResponse response, ServletContext context) 
		throws IOException
	{
		String uri = request.getRequestURI();
		if (uri.equals("/v1/registerUrls"))
			registerUrls(request);
		else if (uri.equals("/v1/lookupUrls"))
			lookupUrls(response);
		else if (uri.equals("/v1/updateUrls"))
			updateUrls();
		else if (uri.equals("/v1/whoami"))
			whoami(response);
		else if (uri.equals("/v1/printDatastore"))
			printDatastore(response);
		else if (uri.equals("/v2/users"))
		{
			if (request.getMethod().equals("GET"))
				sendUserInfo(response, context);
			if (request.getMethod().equals("POST")) 
				addNewUser(request, response);
		}
		else if (uri.contains("/v2/users"))
		{
			String currentUser = null;
			Set<String> userNames = users.keySet(); 
			for (String user : userNames)
			{
				if (uri.contains(user))
				{					
					currentUser = user;
					break;
				}
			}
			if (currentUser != null)
			{	
				if (uri.equals("/v2/users/"+currentUser))	
					fetchUserDetail(response, context, currentUser);
				else if (uri.equals("/v2/users/"+currentUser+"/urls") && request.getMethod().equals("GET"))
					fetchUserBookmarks(response, context, currentUser);
				else if (uri.equals("/v2/users/"+currentUser+"/urls") && request.getMethod().equals("POST"))
					addNewBookmarkForUser(request, response, currentUser);
				//
				else if (uri.equals("/v2/users/"+currentUser+"/subscribe") && request.getMethod().equals("POST"))
					createSubscriptionForUser(request, response, currentUser);
				else if (uri.equals("/v2/users/"+currentUser+"/notify") && request.getMethod().equals("POST"))
					updateUserNotifications(request, response, currentUser);
				else if (uri.equals("/v2/users/"+currentUser+"/notifications"))
					getUserNotifications(response, context, currentUser);
				//
				else if (uri.contains("/v2/users/"+currentUser+"/urls"))
				{
					String userUrl = "/v2/users/"+currentUser+"/urls/";
					int l = userUrl.length(); int u = uri.length();
					String bookmark = uri.substring(l,u);
					if (request.getMethod().equals("GET"))
						fetchUserBookmarkMetadata(request, response, context, currentUser, bookmark);
					else if (request.getMethod().equals("PUT"))
						replaceUserBookmark(request, response, currentUser, bookmark);
				}
			}
			if (currentUser == null) unknownURL(request, response); 
		}
		else if (uri.equals("/v2/categories"))
			getAllCategories(response, context);
		else if (uri.contains("/v2/categories"))
		{
			String base = "/v2/categories/";
			int l = base.length(); int u = uri.length();
			String category = uri.substring(l,u);
			getCategoryBookmarks(request, response, context, category);
		}
		else
			unknownURL(request, response);
	}
	
	private void whoami(HttpServletResponse response) 
		throws IOException
	{
		response.setContentType("text/plain");
	    PrintWriter pw = response.getWriter();
	    pw.println("wade.tollefson@gmail.com");	
	}

	private void lookupUrls(HttpServletResponse response) 
	{
		response.setContentType("text/xml");
		try {
	    PrintWriter pw = response.getWriter();
	    pw.println("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
	    pw.println("<urls>");
	    Iterator<String> it = urls.iterator();
	    while (it.hasNext())
	    { pw.println("<url>"+it.next().toString()+"</url>"); }        
	    pw.println("</urls>");	
		}
		catch (IOException e) { }
	}

	private void registerUrls(HttpServletRequest request) 
	{
		try {
			Document document = buildDocument(request.getInputStream());
		    NodeList nl = document.getElementsByTagName("url");
		    for (int i = 0; i < nl.getLength(); i++) 
		    {
		    	Element urlElt = (Element) nl.item(i);	 
		    	if (!urls.contains(urlElt.getFirstChild().getNodeValue()))
		    		urls.add(urlElt.getFirstChild().getNodeValue());
		    } 
		    updateUrls();
		}
		catch (MalformedURLException e) { }
		catch (ParserConfigurationException e) { }
		catch (SAXException e) { }
		catch (IOException e) { }	

	}
	
	private void unknownURL(HttpServletRequest request, HttpServletResponse response) 
	throws IOException 
	{
		response.setContentType ("text/plain");
		PrintWriter pw = response.getWriter();
		pw.println ("Unknown URL");
		pw.println ("uri = \"" + request.getRequestURI() + "\"");
		pw.println ("url = \"" + request.getRequestURL() + "\"");
		pw.println ("servletPath = \"" + request.getServletPath() + "\"");
		pw.println ("queryString = \"" + request.getQueryString() + "\"");
	}
	
	private void updateUrls() 
	{
		Iterator<String> it = urls.iterator();
		HashSet<String> newUrls = new HashSet<String>();
		HashSet<String> evictUrls = new HashSet<String>();
		while (it.hasNext())
		{ 
			String url = it.next();
		    if (url.contains("p2p-node-wt")) continue; 
		    try {
		    	URL urlTest = new URL(url+"/v1/lookupUrls");
				URLConnection conn = urlTest.openConnection();
				Document document = buildDocument(conn.getInputStream());
				NodeList nl = document.getElementsByTagName("url");
				for (int i = 0; i < nl.getLength (); i++) 
				{
					Element urlElt = (Element) nl.item(i);
				    if (!newUrls.contains(urlElt.getFirstChild().getNodeValue()))
				    	newUrls.add(urlElt.getFirstChild().getNodeValue());
				 } 
		    }
		    catch (MalformedURLException e) { evictUrls.add(url); continue; }
		    catch (SAXException e) { evictUrls.add(url); continue; }
		    catch (ParserConfigurationException e) { evictUrls.add(url); continue; }
		    catch (IOException e) { evictUrls.add(url); continue; }	
		    catch (NullPointerException e) { evictUrls.add(url); continue; }	
		}
		for (String url : newUrls)
		{ if (!urls.contains(url)) urls.add(url); }
		for (String url : evictUrls)
		{ urls.remove(url); }		
		evictUrls();
		normalizeUrls();
		persistUserData();
		populateUsers();
	}
	
	private Document buildDocument(InputStream is) throws SAXException, 
										ParserConfigurationException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setValidating(false);
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document document = db.parse(is);
	    return document;
	}
	
	private void evictUrls()
	{
		Iterator<String> it = urls.iterator();
		HashSet<String> evictUrls = new HashSet<String>();
		while (it.hasNext())
		{ 
			String url = it.next();
		    if (url.contains("p2p-node-wt")) continue; 
		    try {
		    	URL urlTest = new URL(url+"/v1/lookupUrls");
				URLConnection conn = urlTest.openConnection();
				Document document = buildDocument(conn.getInputStream());
				NodeList nl = document.getElementsByTagName("url");
				if (nl.getLength() == 0)
					evictUrls.add(url);
		    }
		    catch (MalformedURLException e) { evictUrls.add(url); continue; }
		    catch (SAXException e) { evictUrls.add(url); continue; }
		    catch (ParserConfigurationException e) { evictUrls.add(url); continue; }
		    catch (IOException e) { evictUrls.add(url); continue; }		    
		}
		for (String url : evictUrls)
		{ urls.remove(url); }
	}
	
	private void normalizeUrls()
	{
		HashSet<String> hostSet = new HashSet<String>();	
		for (String url : urls)
		{ 
			try {
				URL u = new URL(url);
				if (!hostSet.contains(u.getHost()))
					hostSet.add(u.getHost());
			}
			catch (MalformedURLException e) {}
		}
		synchronized (urls) {
			urls.clear();
			for (String url : hostSet)
			{ urls.add("http://"+url); }
		}
	}
	
	private void persistUserData()
	{
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		HashSet<String> usersSet = new HashSet<String>();
		for (UserData user : userData)
			{ 
				try {
					usersSet.add(user.getURL()); 
				}
				catch (JDOUserException e) { }
			}
		for (String url : urls)
		{
			if (url.contains("p2p-node-wt")) continue; 
			if (!usersSet.contains(url))
			{
				UserData newUser = new UserData(url);
				try {
					pm.makePersistent(newUser);
				} 
				catch (JDOUserException e) { }
			}
		}
		HashSet<String> deleteUsers = new HashSet<String>();
		for (String existing : usersSet)
		{
			if (!urls.contains(existing) && !existing.equals("http://whitehouse.gov")) //check later
			{ deleteUsers.add(existing); }
		}
		for (UserData user : userData)
		{
			if (deleteUsers.contains(user.getURL()))
			{
				try {
					pm.currentTransaction().begin();
					user = pm.getObjectById(UserData.class, user.getID());
					pm.deletePersistent(user);
					pm.currentTransaction().commit();
				} catch (Exception ex) {
					pm.currentTransaction().rollback();
				} 
			} 
		}
	}
	
	private void printDatastore(HttpServletResponse response)
	{
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData user: userData)
		{
			response.setContentType ("text/plain");
			PrintWriter pw;
			try {
				pw = response.getWriter();
				pw.println(user.getURL());
			} 
			catch (Exception e) { }
		}
	}
	
	private void populateUsers()
	{
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData user: userData)
		{
			if (user.getUserName().equalsIgnoreCase("empty"))
			{
				try {
					URL whoami = new URL(user.getURL()+"/v1/whoami");
					URLConnection conn = whoami.openConnection();
					BufferedReader in = new BufferedReader(new 
					InputStreamReader(conn.getInputStream(), "UTF-8"));
					String username = in.readLine();
					if (username.contains("@"))
					{
						pm.currentTransaction().begin();
						user.setUserName(username);
						pm.currentTransaction().commit();
					}
				}
				catch (IOException e) { }
				catch (JDOUserException e) { }
				catch (NullPointerException e) { }
			}
		}
		String query2 = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> usersList = (List<UserData>) pm.newQuery(query2).execute();
		//
		//make sure other nodes v2/users is active
		/*ArrayList<String> inactiveUsers = new ArrayList<String>();
		for (UserData user : usersList)
		{
			try {
			    URL url = new URL(user.getURL()+"/v2/users");
			    URLConnection conn = url.openConnection();
			    conn.connect();
			    //if (!conn.getContentType().equals("application/xhtml+xml"))
			    //{ inactiveUsers.add(user.getURL()); }
			} catch (MalformedURLException e) {
			    // the URL is not in a valid form
				inactiveUsers.add(user.getURL());
			} catch (IOException e) {
			    // the connection couldn't be established
				inactiveUsers.add(user.getURL());
			}
		}*/
		//
		//
		synchronized (users) {
			users.clear();
			ArrayList<String> activeUsers = new ArrayList<String>();
			for (UserData user : usersList) 
			{
				if (user.getActive())
				{ 
					activeUsers.add(user.getUserName()); 
					users.put(user.getUserName(), user.getURL());
				}
				else
				{
				try {
					if (!activeUsers.contains(user.getUserName()))
					{
						activeUsers.add(user.getUserName());
						pm.currentTransaction().begin();
						user.setActive(true);
						pm.currentTransaction().commit();
						users.put(user.getUserName(), user.getURL());
					}
					else
					{
						pm.currentTransaction().begin();
						user.setActive(false);
						pm.currentTransaction().commit();
					}
				}
				catch (JDOUserException e) { }
				}
			}
		}
	}
	
	private void sendUserInfo(HttpServletResponse response, ServletContext context)         
	{
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("users");
			doc.appendChild(root);
			for (UserData user : userData)
			{
				if (!user.getUserName().equals("empty") && user.getActive())
				{
					Element child = doc.createElement("user");
					root.appendChild(child);
					Text text = doc.createTextNode(user.getUserName());
					child.appendChild(text);
				}
			}
			DOMSource source = new DOMSource(doc);
			URL path = context.getResource("/WEB-INF/v2users.xsl");
			StreamSource xslt = new StreamSource(path.toString());
			
			javax.xml.transform.TransformerFactory xformFactory =
			      javax.xml.transform.TransformerFactory.newInstance(
				"org.apache.xalan.processor.TransformerFactoryImpl",
				Thread.currentThread().getContextClassLoader()); 
			
		    Transformer transformer = xformFactory.newTransformer(xslt);
		    response.setContentType("application/xhtml+xml");
		    response.setCharacterEncoding("UTF-8");
		    StreamResult result = new StreamResult(response.getOutputStream());
		    transformer.transform(source, result);
		    result.getOutputStream().flush();
		}
		catch (ParserConfigurationException e) { }
		catch (IOException e) { }
		catch (TransformerConfigurationException e) { }
		catch (TransformerException e) { }
	}
	
	private void fetchUserDetail(HttpServletResponse response, ServletContext context, String currentUser) 
	{
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("user");
			doc.appendChild(root);
			Element child = doc.createElement("username");
			root.appendChild(child);
			Text text = doc.createTextNode(currentUser);
			child.appendChild(text);
			Element child2 = doc.createElement("url");
			root.appendChild(child2);
			Text text2 = doc.createTextNode(users.get(currentUser)); 
			child2.appendChild(text2);
			DOMSource source = new DOMSource(doc);
			
			URL path = context.getResource("/WEB-INF/v2users-user.xsl");
			StreamSource xslt = new StreamSource(path.toString());
			
			javax.xml.transform.TransformerFactory xformFactory =
			      javax.xml.transform.TransformerFactory.newInstance(
				"org.apache.xalan.processor.TransformerFactoryImpl",
				Thread.currentThread().getContextClassLoader()); 
			
		    Transformer transformer = xformFactory.newTransformer(xslt);
		    response.setContentType("application/xhtml+xml");
		    response.setCharacterEncoding("UTF-8");
		    StreamResult result = new StreamResult(response.getOutputStream());
		    transformer.transform(source, result);
		    result.getOutputStream().flush();
			
		}
		catch (ParserConfigurationException e) {}
		catch (IOException e) {}
		catch (TransformerConfigurationException e) {}
		catch (TransformerException e) {}
	}
	
	private void addNewUser(HttpServletRequest request, HttpServletResponse response)
	{
		String email = null;
		String node = null;
		try {
			Document document = buildDocument(request.getInputStream());
		    NodeList nl = document.getElementsByTagName("email");
		    NodeList n2 = document.getElementsByTagName("node");
		    Element emailElt = (Element) nl.item(0);	 
	    	email = emailElt.getFirstChild().getNodeValue();
	    	Element nodeElt = (Element) n2.item(0);	 
	    	node = nodeElt.getFirstChild().getNodeValue();
		  
			if (email != null && node != null)
			{
				UserData newUser = new UserData(node);
				newUser.setUserName(email);
				newUser.setActive(true);
				try {
					pm.currentTransaction().begin();
	;				pm.makePersistent(newUser);
					pm.currentTransaction().commit();
					users.put(email, node);
				} 
				catch (JDOUserException e) { }
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.setHeader("Location", "http://p2p-node-wt.appspot.com/v2/users/"+email);
			}
		}
		catch (MalformedURLException e) { }
		catch (ParserConfigurationException e) { }
		catch (SAXException e) { }
		catch (IOException e) { }	
		//silently fails if document is invalid
	}
	
	private void fetchUserBookmarks(HttpServletResponse response, ServletContext context, String currentUser) 
	throws IOException 
	{
		ArrayList<String> bookmarks = null;
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData user : userData)
		{
			if (user.getUserName().equals(currentUser))
			{
				bookmarks = user.getBookmarks();
				break;
			}
		}
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("urls");
			doc.appendChild(root);
			Iterator<String> it = bookmarks.iterator();
			int url = 1;
			while (it.hasNext())
			{
				Element child = doc.createElement("url");
				root.appendChild(child);
				Element child1 = doc.createElement("uri");
				child.appendChild(child1);
				Text text1 = doc.createTextNode("http://p2p-node-wt.appspot.com/v2/users/"+currentUser+"/urls/"+url);
				child1.appendChild(text1);
				url++;
				Element child2 = doc.createElement("bookmark");
				child.appendChild(child2);
				Text text2 = doc.createTextNode(it.next());
				child2.appendChild(text2);
			}
			DOMSource source = new DOMSource(doc);
			
			URL path = context.getResource("/WEB-INF/v2users-user-urls.xsl");
			StreamSource xslt = new StreamSource(path.toString());
			
			javax.xml.transform.TransformerFactory xformFactory =
			      javax.xml.transform.TransformerFactory.newInstance(
				"org.apache.xalan.processor.TransformerFactoryImpl",
				Thread.currentThread().getContextClassLoader()); 
			
		    Transformer transformer = xformFactory.newTransformer(xslt);
		    response.setContentType("application/xhtml+xml");
		    response.setCharacterEncoding("UTF-8");
		    StreamResult result = new StreamResult(response.getOutputStream());
		    transformer.transform(source, result);
		    result.getOutputStream().flush();
			
		}
		catch (IOException e) {}
		catch (TransformerConfigurationException e) {}
		catch (TransformerException e) {}
		catch (ParserConfigurationException e) {}
	}
	
	private void addNewBookmarkForUser(HttpServletRequest request, HttpServletResponse response, String currentUser)
	{
		String bookmark = null;
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<String> comments = new ArrayList<String>();
		try {
			Document document = buildDocument(request.getInputStream());
		    NodeList nl = document.getElementsByTagName("uri");
		    NodeList n2 = document.getElementsByTagName("category");
		    NodeList n3 = document.getElementsByTagName("comment");
		    Element urlElt = (Element) nl.item(0);	 
	    	bookmark = urlElt.getFirstChild().getNodeValue();
	    	
		    for (int i = 0; i < n2.getLength(); i++) 
		    {
		    	Element element = (Element) n2.item(i);	 
		    	categories.add(element.getFirstChild().getNodeValue());
		    } 
		    for (int i = 0; i < n3.getLength(); i++) 
		    {
		    	Element element = (Element) n3.item(i);	 
		    	comments.add(element.getFirstChild().getNodeValue());
		    } 
		    Calendar currentDate = Calendar.getInstance();
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd");
		    String dateNow = formatter.format(currentDate.getTime());
		    
		    String query = "select from " + UserData.class.getName();
			@SuppressWarnings("unchecked")
			List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
			int location = 0;
			for (UserData user : userData)
			{
				if (user.getActive() && user.getUserName().equals(currentUser))
				{
					location = user.getBookmarks().size()+1;
					pm.currentTransaction().begin();
					user.addBookmark(bookmark);	
					for (String category : categories)
					{
						user.addCategory(category);
						user.addCategory(location+""); 
					}
					for (String comment : comments)
						user.addComment(comment);
					user.addBookmarDate(dateNow);
					pm.currentTransaction().commit();
					//
					//ATTEMPT AT INTERNAL POST
					/*String query2 = "select from " + Subscriptions.class.getName();
					@SuppressWarnings("unchecked")
					List<Subscriptions> subscriptions = (List<Subscriptions>) pm.newQuery(query2).execute();
					for (Subscriptions subscription : subscriptions)
					{
						ArrayList<String> subs = subscription.getSubscriptions();
						ArrayList<Integer> callbackIDs = new ArrayList<Integer>();
						for (int i=0; i<subs.size(); i++)
						{
							if (subs.get(i).equals(currentUser))
								callbackIDs.add(i);
						}
						if (callbackIDs.size()!=0)
						{
							ArrayList<String> callbacks = new ArrayList<String>();
							for (int j=0; j<callbackIDs.size(); j++)
							{
								String callbackID = subscription.getSubscriberCallbackURLs().get(callbackIDs.get(j));
								callbacks.add(callbackID);
							}
							Iterator<String> it = callbacks.iterator();
							while (it.hasNext())
							{
								URL url = new URL(it.next());

								HttpURLConnection connection = (HttpURLConnection) url.openConnection();
								connection.setDoOutput(true);
								connection.setUseCaches(false);
								connection.setAllowUserInteraction(false);
								connection.setRequestMethod("POST");
								connection.setRequestProperty("Content-type","application/xml");

								OutputStream output = connection.getOutputStream();
								
								byte[] outputData;
								String one = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
								String two = "<notice><subscription>http://p2p-node-wt.appspot.com/v2/users/";
								String three = "</subscription><update>http://p2p-node-wt.appspot.com/v2/users/v2/users/";
								String four = "/urls/";
								String five = "</update></notice>";
								String urlid = location+"";
								String content = one+two+currentUser+three+currentUser+four+urlid+five;
								outputData = (byte[]) content.getBytes();
								
								output.write(outputData);
								output.flush();
								output.close();

							}
						}
					}*/
					//END ATTEMPT AT INTERNAL POST
					break;
				}
			}
			if (location != 0) 
			{
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.setHeader("Location", "http://p2p-node-wt.appspot.com/v2/users/"+currentUser+"/"+location);
			}
		}
		catch (MalformedURLException e) { }
		catch (ParserConfigurationException e) { }
		catch (SAXException e) { }
		catch (IOException e) { }	
		//silently fails if document is invalid
	}

	private void fetchUserBookmarkMetadata(HttpServletRequest request, HttpServletResponse response, 
														ServletContext context, String currentUser, String bookmark) 
	throws IOException
	{
		ArrayList<String> bookmarks = null;
		ArrayList<String> metadata = null;
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData user : userData)
		{
			if (user.getUserName().equals(currentUser))
			{
				bookmarks = user.getBookmarks();
				metadata = user.getBookmarkDates();
				break;
			}
		}
		try {
			String realBookmark = bookmarks.get(Integer.parseInt(bookmark)-1);
			String dateAdded = metadata.get(Integer.parseInt(bookmark)-1);
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element root = doc.createElement("meta");
				doc.appendChild(root);
				Element child = doc.createElement("bookmark");
				root.appendChild(child);
				Text text = doc.createTextNode(realBookmark);
				child.appendChild(text);
				Element child1 = doc.createElement("date");
				root.appendChild(child1);
				Text text2 = doc.createTextNode(dateAdded);
				child1.appendChild(text2);
				DOMSource source = new DOMSource(doc);
				
				URL path = context.getResource("/WEB-INF/v2users-user-urls-url.xsl");
				StreamSource xslt = new StreamSource(path.toString());
				javax.xml.transform.TransformerFactory xformFactory =
				      javax.xml.transform.TransformerFactory.newInstance(
					"org.apache.xalan.processor.TransformerFactoryImpl",
					Thread.currentThread().getContextClassLoader()); 
				
			    Transformer transformer = xformFactory.newTransformer(xslt);
			    response.setContentType("application/xhtml+xml");
			    response.setCharacterEncoding("UTF-8");
			    StreamResult result = new StreamResult(response.getOutputStream());
			    transformer.transform(source, result);
			    result.getOutputStream().flush();			
			}
			catch (IOException e) {}
			catch (TransformerConfigurationException e) {}
			catch (TransformerException e) {}
			catch (ParserConfigurationException e) {}		
		}
		catch (Exception e) {unknownURL(request, response);}
	}
	
	private void replaceUserBookmark(HttpServletRequest request, 
								HttpServletResponse response, String currentUser, String bookmark)
	{
		//SKELETON CODE - unimplemented
		//redirect in unknown bookmark url
	}
	
	private void getAllCategories(HttpServletResponse response, ServletContext context) throws IOException
	{
		ArrayList<String> categories = new ArrayList<String>();
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData user : userData)
		{
			ArrayList<String> temp = user.getCategories();
			for (int i=0; i<temp.size(); i=i+2)
				if (!categories.contains(temp.get(i)))
					categories.add(temp.get(i));
		}
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element root = doc.createElement("categories");
				doc.appendChild(root);
				Iterator<String> it = categories.iterator();
				while (it.hasNext())
				{
					Element child = doc.createElement("category");
					root.appendChild(child);
					Text text = doc.createTextNode(it.next());
					child.appendChild(text);
				}
				
				DOMSource source = new DOMSource(doc);
				URL path = context.getResource("/WEB-INF/v2categories.xsl");
				StreamSource xslt = new StreamSource(path.toString());
				javax.xml.transform.TransformerFactory xformFactory =
				      javax.xml.transform.TransformerFactory.newInstance(
					"org.apache.xalan.processor.TransformerFactoryImpl",
					Thread.currentThread().getContextClassLoader()); 
				
			    Transformer transformer = xformFactory.newTransformer(xslt);
			    response.setContentType("application/xhtml+xml");
			    response.setCharacterEncoding("UTF-8");
			    StreamResult result = new StreamResult(response.getOutputStream());
			    transformer.transform(source, result);
			    result.getOutputStream().flush();	
			}
			catch (IOException e) {}
			catch (TransformerConfigurationException e) {}
			catch (TransformerException e) {}
			catch (ParserConfigurationException e) {}	
	}
	
	private void getCategoryBookmarks(HttpServletRequest request, 
			HttpServletResponse response, ServletContext context, String category) throws IOException
	{
		ArrayList<String> bookmarkNum = new ArrayList<String>();
		ArrayList<String> username = new ArrayList<String>();
		ArrayList<String> bookmarkPage = new ArrayList<String>();
		
		String query = "select from " + UserData.class.getName();
		@SuppressWarnings("unchecked")
		List<UserData> userData = (List<UserData>) pm.newQuery(query).execute();
		for (UserData user : userData)
		{
			ArrayList<String> temp = user.getCategories();
			for (int i=0; i<temp.size(); i=i+2)
			{
				if (temp.get(i).equals(category))
				{
					bookmarkNum.add(temp.get(i+1));
					username.add(user.getUserName());
					String bookmark = user.getBookmarks().get(Integer.parseInt(temp.get(i+1))-1);
					bookmarkPage.add(bookmark); 
				}
			}
		}
		if (bookmarkNum.size() != 0)
		{
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element root = doc.createElement("bookmarks");
				doc.appendChild(root);
				
				int bookmarks = bookmarkNum.size();
				for (int i=0; i<bookmarks; i++)
				{
					Element child = doc.createElement("info");
					root.appendChild(child);
					Element numElt = doc.createElement("num");
					child.appendChild(numElt);
					Text numText = doc.createTextNode(bookmarkNum.get(i));
					numElt.appendChild(numText);
					Element usernameElt = doc.createElement("username");
					child.appendChild(usernameElt);
					Text usernameText = doc.createTextNode(username.get(i));
					usernameElt.appendChild(usernameText);
					Element pageElt = doc.createElement("page");
					child.appendChild(pageElt);
					Text pageText = doc.createTextNode(bookmarkPage.get(i));
					pageElt.appendChild(pageText);
				}
				
				DOMSource source = new DOMSource(doc);
				URL path = context.getResource("/WEB-INF/v2categories-category.xsl");
				StreamSource xslt = new StreamSource(path.toString());
				javax.xml.transform.TransformerFactory xformFactory =
				      javax.xml.transform.TransformerFactory.newInstance(
					"org.apache.xalan.processor.TransformerFactoryImpl",
					Thread.currentThread().getContextClassLoader()); 
				
			    Transformer transformer = xformFactory.newTransformer(xslt);
			    response.setContentType("application/xhtml+xml");
			    response.setCharacterEncoding("UTF-8");
			    StreamResult result = new StreamResult(response.getOutputStream());
			    transformer.transform(source, result);
			    result.getOutputStream().flush();	
			}
			catch (IOException e) {}
			catch (TransformerConfigurationException e) {}
			catch (TransformerException e) {}
			catch (ParserConfigurationException e) {}	
		}
		else
		{
			unknownURL(request, response);
		}
		//redirect if unknown category
	}
	
	private void createSubscriptionForUser(HttpServletRequest request, 
												HttpServletResponse response, String currentUser)
	{
		try {
			String callback = null;
			Document document = buildDocument(request.getInputStream());
		    NodeList nl = document.getElementsByTagName("callback-url");
		    Element urlElt = (Element) nl.item(0);	 
	    	callback = urlElt.getFirstChild().getNodeValue();
	    	  
		    String query = "select from " + Subscriptions.class.getName();
			@SuppressWarnings("unchecked")
			List<Subscriptions> subscriptions = (List<Subscriptions>) pm.newQuery(query).execute();
			for (Subscriptions subscription : subscriptions)
			{
				pm.currentTransaction().begin();
				subscription.addSubscription(currentUser);
				subscription.addCallbackURL(callback);
				pm.currentTransaction().commit();
			}			
		}
		catch (MalformedURLException e) { }
		catch (ParserConfigurationException e) { }
		catch (SAXException e) { }
		catch (IOException e) { }	
		//silently fails if document is invalid
	}
	
	private void updateUserNotifications(HttpServletRequest request, 
												HttpServletResponse response, String currentUser)
	{
		try {
			String subscript = null;
			String update = null;
			Document document = buildDocument(request.getInputStream());
		    NodeList nl = document.getElementsByTagName("subscription");
		    Element urlElt = (Element) nl.item(0);	 
	    	subscript = urlElt.getFirstChild().getNodeValue();
	    	NodeList n2 = document.getElementsByTagName("update");
		    Element urlElt2 = (Element) n2.item(0);	 
	    	update = urlElt2.getFirstChild().getNodeValue();
	    	
	    	Calendar currentDate = Calendar.getInstance();
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd");
		    String dateNow = formatter.format(currentDate.getTime());
		    
		    String query = "select from " + Subscriptions.class.getName();
			@SuppressWarnings("unchecked")
			List<Subscriptions> subscriptions = (List<Subscriptions>) pm.newQuery(query).execute();
			for (Subscriptions subscription : subscriptions)
			{
				pm.currentTransaction().begin();
				subscription.addNotifcation(currentUser);
				subscription.addNotifcation(subscript);
				subscription.addNotifcation(update);
				subscription.addNotifcation(dateNow);
				pm.currentTransaction().commit();
			}
		}
		catch (MalformedURLException e) { }
		catch (ParserConfigurationException e) { }
		catch (SAXException e) { }
		catch (IOException e) { }	
		//silently fails if document is invalid
	}
	
	private void getUserNotifications(HttpServletResponse response, 
														ServletContext context, String currentUser)
	{
		ArrayList<String> subs = new ArrayList<String>();
		ArrayList<String> update = new ArrayList<String>();
		ArrayList<String> date = new ArrayList<String>();
		
		String query = "select from " + Subscriptions.class.getName();
		@SuppressWarnings("unchecked")
		List<Subscriptions> subscriptions = (List<Subscriptions>) pm.newQuery(query).execute();
		for (Subscriptions subscription : subscriptions)
		{
			ArrayList<String> temp = subscription.getNotifications();
			for (int i=0; i<temp.size(); i=i+4)
			{
				if (temp.get(i).equals(currentUser))
				{
					subs.add(temp.get(i+1));
					update.add(temp.get(i+2));
					date.add(temp.get(i+3));
				}
			}
		}
			try {
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element root = doc.createElement("notes");
				doc.appendChild(root);

				int numNot = subs.size();
				for (int i=0; i<numNot; i++)
				{
					Element child = doc.createElement("note");
					root.appendChild(child);
					Element sElt = doc.createElement("s");
					child.appendChild(sElt);
					Text sText = doc.createTextNode(subs.get(i));
					sElt.appendChild(sText);
					Element uElt = doc.createElement("u");
					child.appendChild(uElt);
					Text uText = doc.createTextNode(update.get(i));
					uElt.appendChild(uText);
					Element dElt = doc.createElement("d");
					child.appendChild(dElt);
					Text dText = doc.createTextNode(date.get(i));
					dElt.appendChild(dText);
				}
				
				DOMSource source = new DOMSource(doc);
				URL path = context.getResource("/WEB-INF/v2users-user-notifications.xsl");
				
				StreamSource xslt = new StreamSource(path.toString());
				javax.xml.transform.TransformerFactory xformFactory =
				      javax.xml.transform.TransformerFactory.newInstance(
					"org.apache.xalan.processor.TransformerFactoryImpl",
					Thread.currentThread().getContextClassLoader()); 
				
			    Transformer transformer = xformFactory.newTransformer(xslt);
			    response.setContentType("application/xhtml+xml");
			    response.setCharacterEncoding("UTF-8");
			    StreamResult result = new StreamResult(response.getOutputStream());
			    transformer.transform(source, result);
			    result.getOutputStream().flush();	
			}
			catch (IOException e) {}
			catch (TransformerConfigurationException e) {}
			catch (TransformerException e) {}
			catch (ParserConfigurationException e) {}	
	}
}
