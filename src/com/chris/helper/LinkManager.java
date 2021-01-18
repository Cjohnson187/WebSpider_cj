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
 * save them in the spider's desired format. It will also check for new links 
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
	private static String currentPage;

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
		// initialize set for sites that will be checked
		visited  = new HashSet<>();

		// initialize q and add starting site
		priority = new PriorityQueue<String>();
		priority.add(site);
	}	

	/**
	 * Add to q if not in set that stores visited sites
	 * @param link
	 */
	public void addLink(List<String> links) {
		for (String link: links) {
			// add link to q if not in set that indicates sites visited
			if(!visited.contains(link) && !priority.contains(link)) {
				priority.add(link);
			}

		}
	}
	
	public String getURI() {
		return currentPage;
	}

	/**
	 * Getting next page in queu and adding it to the set of pages visited.
	 * 
	 * Problably shouldnt be returning null though ****
	 */
	public String getNextPage() {
		try {
			currentPage = priority.poll();
			visited.add(currentPage);
			return currentPage;
		}
		catch(Exception e){
			System.out.println("Error getting next page, " + e + "occurred");
		}
		// if page has not been traversed 	
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
