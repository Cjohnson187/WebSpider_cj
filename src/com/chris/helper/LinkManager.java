package com.chris.helper;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
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
	private static Queue<String> priority;
	private String host;

	/**
	 *Basic constructor
	 */
	public LinkManager() {
		// initialize set and q
		visited  = new HashSet<>();
		priority = new PriorityQueue<String>();
	}

	/**
	 * Overloaded constructor that builds linkManager with starting page.
	 * @param site
	 */
	public LinkManager(String site) {
		// initialize set for sites that will be checked
		visited  = new HashSet<>();
		// initialize q and add starting site
		priority = new PriorityQueue<String>();
		setHostName(site);
	}	
	
	/**
	 * Setting host name and adding directory to priorityQ if there is one
	 * @param url
	 */
    public void setHostName(String url) {
    	//removing protocol
    	String dir =  "";
		url = url.replace("http://", "");
		url = url.replace("https://", "");
		if(url.contains("/")) {
			dir  = url.substring(url.indexOf("/"));
			priority.add(dir);
		}
		host = url.substring(0, url.indexOf("/"));	
	}
	
	/**
	 * Adding directories pulled from urls if they have not been added/visited
	 * @param urls
	 */
	public void addLinks(List<String> urls) {
		try {
			String index = "";
			for (String url: urls) {
				url = url.replace("http://", "");
				url = url.replace("https://", "");
				if(url.contains("/")) {
					index = url.substring(url.indexOf("/"));
					// add link to q if not in set that indicates sites visited
					if(!visited.contains(index) && !priority.contains(index)) {
						priority.add(index);
					}
				}	
			}
		}
		catch (NullPointerException e) {
			System.out.println("No links found.");
		}
	}
	
	/**
	 * getting host name
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
