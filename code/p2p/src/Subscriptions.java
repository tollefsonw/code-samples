package com.se560.project;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Subscriptions implements Serializable
{
	private static final long serialVersionUID = 1L;
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private ArrayList<String> subscriptions = new ArrayList<String>();
	@Persistent
	private ArrayList<String> subscriberCallbackURLs = new ArrayList<String>();
	@Persistent
	private ArrayList<String> notifications = new ArrayList<String>();
	
	public Subscriptions(){ }
	
	public Long getID() 
	{ return id; }
	
	public ArrayList<String> getSubscriptions()
	{ return subscriptions; }
	
	public ArrayList<String> getSubscriberCallbackURLs()
	{ return subscriberCallbackURLs; }
	
	public ArrayList<String> getNotifications()
	{ return notifications; }
	
	public void addSubscription(String subscription)
	{ subscriptions.add(subscription); }
	
	public void addCallbackURL(String url)
	{ subscriberCallbackURLs.add(url); }
	
	public void addNotifcation(String notification)
	{ notifications.add(notification); }
}
