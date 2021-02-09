package com.chris.helper;

import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/****************************************************************************
 * <b>Title</b>: LinkManager.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> The link manager stores the pages that have been 
 * visited and stores the new sites in a priority Q.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 2.0
 * @since Feb 4, 2021
 * @updates:
 ****************************************************************************/

public class LinkManager {

	private static Set<String> visited;
	private static LinkedList<String> priority;
	private String host;

	/**
	 *Basic constructor
	 */
	public LinkManager() {
		// initialize set and q
		visited  = new HashSet<>();
		priority = new LinkedList<String>();
		host = "";
	}

	/**
	 * Overloaded constructor that builds linkManager with starting page.
	 * @param site
	 */
	public LinkManager(String site) {
		// initialize set for sites that will be checked
		visited  = new HashSet<>();
		// initialize q and add starting site
		priority = new LinkedList<String>();
		setHostName(site);
	}	
	
	/**
	 * Not being used yet but will be for dynamic redirects
	 * @param redirect
	 */
	public void addFront(String redirect) {
		priority.addFirst(redirect);
	}
	
	/**
	 * Setting host name and adding directory to the Q if there is one
	 * @param url
	 */
    public void setHostName(String url) {
    	//removing protocol
    	String path =  "";
		url = url.replace("http://", "");
		url = url.replace("https://", "");
		if(url.contains("/")) {
			path  = url.substring(url.indexOf("/"));
			priority.add(path);
		}
		host = url.substring(0, url.indexOf("/"));	
	}
	
	/**
	 * Adding paths pulled from urls if they have not been added/visited
	 * @param urls
	 */
	public void addLinks(List<String> urls) {
		try {
			String path = "";
			for (String url: urls) {
				url = url.replace("http://", "");
				url = url.replace("https://", "");
				if(url.contains("/")) {
					path = url.substring(url.indexOf("/"));
					// add link to q if not in set that indicates sites visited
					if(!visited.contains(path) && !priority.contains(path)) {
						priority.add(path);
					}
				}	
			}
		}
		catch (NullPointerException e) {
			System.out.println("No links found.");
		}
	}
	
	/**
	 * Getting host name
	 * @return
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Getting next page in Q and adding it to the set of pages visited.
	 */
	public String getNextPage() {
		try {
			String currentPage = priority.poll();
			visited.add(currentPage);
			return currentPage;
		}
		catch(Exception e){
			System.out.println("Error getting next page, " + e + "occurred");
		}
		// Shouldn't return if check hasNew()
		return null;
	}

	/**
	 * Checking if the q is empty.
	 * @return
	 */
	public boolean hasNew(){
		return !priority.isEmpty();
	}
}
