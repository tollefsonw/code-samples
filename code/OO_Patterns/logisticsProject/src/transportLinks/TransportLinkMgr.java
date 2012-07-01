/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transportLinks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import utilities.ErrorCheckUtility;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class TransportLinkMgr {

    private static TransportLinkMgr instance;
    private HashMap<String,TransportLink> transportLinks = new HashMap<String,TransportLink>();

	private TransportLinkMgr() throws IOException
	{
            loadTransportLinks();
	}

	public static TransportLinkMgr getInstance() throws IOException
	{
            if (instance == null)
                instance = new TransportLinkMgr();
            return instance;
	}

	private void loadTransportLinks() throws IOException
	{
            String transportLinksInfo = "transport-links.txt";
            ErrorCheckUtility errorCheck = new ErrorCheckUtility();
            BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
            BufferedReader input =  new BufferedReader(new FileReader(transportLinksInfo));
            if (input == null)
            {
                fstream.write("Error in Class 'TransportLinkMgr'"
                                + "in Method 'loadTransportLinks' - Link Information Does Not Exist "
                                + "Check File 'transport-links.txt'\r\n");
            }
            String transportLink;
            while ((transportLink = input.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(transportLink);
                int numOfFields = st.countTokens();
                if (isValidTransportLink(numOfFields))
                {
                    String source = st.nextToken();
                    String destination = st.nextToken();
                    String transitLinkType = st.nextToken();
                    String temp = st.nextToken();
                    if (errorCheck.isIntNumber(temp))
                    {
                    int travelTime = Integer.parseInt(temp);
                    try{
                    TransportLinkImpl newTransportLink =
                            new TransportLinkImpl(source, destination,
                                transitLinkType, travelTime);
                    transportLinks.put(newTransportLink.getSource()
                        + newTransportLink.getDestination(), newTransportLink);
                        }
                    catch(InvalidParameterException ipe)
                    {
                        fstream.write(ipe.getMessage()+"\r\n");
                    }
                    }
                    else
                    {
                        fstream.write("Error in Class 'TransportLinkMgr'"
                                + "in Method 'loadTransportLinks' - Invalid Data: Trave Time"
                                + "Must Be An Integer, but was " + temp +"\r\n");
                    }
                }
                else
                {
                       fstream.write("Error in Class 'TransportLinkMgr'"
                                + "in Method 'loadTransportLinks' - Invalid Data Format: "
                                + "Must Have Four Fields\r\n");
                }
            }
            fstream.close();
	}

	private boolean isValidTransportLink(int numOfFields)
        {
            if (numOfFields != 4)
                return false;
            return true;
        }

	public boolean isTransportLink(String id)
	{
            return transportLinks.containsKey(id);
	}

        public ArrayList<TransportLink> getTransportLinksFromSource(String source)
                throws InvalidParameterException
        {
            if (source == null)
                throw new InvalidParameterException("Error in Class 'TranportLinkMgr'"
                                + "in Method 'getTransportLinksFromSource' - Invalid Paramter: value "
                                + "NULL");
            ArrayList<TransportLink> links = new ArrayList<TransportLink>();
            for (TransportLink transportLink : transportLinks.values())
                if (transportLink.getSource().equalsIgnoreCase(source))
                    links.add(transportLink);
            return links;
        }

        public void printTransPortLinks()
	{
		for (TransportLink transportLink: transportLinks.values())
			System.out.println(transportLink.transportLinksToString());
	}

}
