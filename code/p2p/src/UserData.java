package com.se560.project;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserData implements Serializable
{

	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String url;
	@Persistent
	private String username;
	@Persistent
	private boolean active;
	@Persistent
	private ArrayList<String> bookmarks = new ArrayList<String>();
	@Persistent
	private ArrayList<String> categories = new ArrayList<String>();
	@Persistent
	private ArrayList<String> comments = new ArrayList<String>();
	@Persistent
	private ArrayList<String> bookmarkDates = new ArrayList<String>();
	
	
	public UserData(String url)
	{ this.url = url; username="empty"; active = false;}
	
	public Long getID() 
	{ return id; }

	public String getURL() 
	{ return url; }
	
	public String getUserName()
	{ return username; }
	
	public boolean getActive()
	{ return active; }
	
	public void setUserName(String username)
	{ this.username = username; }
	
	public void setActive(boolean active)
	{ this.active = active; }
	
	public ArrayList<String> getBookmarks()
	{ return bookmarks; }
	
	public ArrayList<String> getCategories()
	{ return categories; }
	
	public ArrayList<String> getComments()
	{ return comments; }
	
	public ArrayList<String> getBookmarkDates()
	{ return bookmarkDates; }
	
	public void addBookmark(String bookmark)
	{ bookmarks.add(bookmark); }
	
	public void addCategory(String category)
	{ categories.add(category); }
	
	public void addComment(String comment)
	{ comments.add(comment); }

	public void addBookmarDate(String date)
	{ bookmarkDates.add(date); }
}
