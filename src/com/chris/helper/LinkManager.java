package com.chris.helper;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

/****************************************************************************
 * <b>Title</b>: LinkManager.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> The Link manager class is going to receive links and
 * save them for the  spider. It will also check for new links 
 * and send the new ones to the spider.
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Jan 5, 2021
 * @updates:
 ****************************************************************************/

public class LinkManager {

	private static Set<String> visited;
	private static Queue<String> priority;
	private static String host;

	/**
	 * basic constructor
	 */
	public LinkManager() {
		// initialize set and q
		visited  = new HashSet<>();
		priority = new PriorityQueue<String>();
	}

	/**
	 * overloaded constructor that builds linkManager with starting page
	 * @param site
	 */
	public LinkManager(String site) {
		setHostName(site);
		// initialize set for sites that will be checked
		visited  = new HashSet<>();
		// initialize q and add starting site
		priority = new PriorityQueue<String>();
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
			url = url.substring(0, url.indexOf("/"));
			priority.add(dir);
		}
		host = url;
	}
	
	/**
	 * Add a list of links to q if not in set that stores visited sites
	 * @param link
	 */
	public void addLinks(List<String> dirs) {
		try {
			for (String dir: dirs) {
				// add link to q if not in set that indicates sites visited
				if(!visited.contains(host+dir) && !priority.contains(dir)) {
					priority.add(dir);
				}
			}
		}
		catch (NullPointerException e) {
			System.out.println("No links found sssooooorrrrryyyyyy.....");
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
	 * Getting next page in queu and adding it to the set of pages visited.
	 */
	public String getNextPage() {
		try {
			String currentPage = host + priority.poll();
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
	 * which it could be if i didnt initialize with a value.
	 * @return
	 */
	public boolean hasNew(){
		if (!priority.isEmpty()){
			return true;
		}
		return false;
	}
}
