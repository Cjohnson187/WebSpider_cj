package com.chris.helper;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

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

	// I think i might keep this in the connection manager.
	private static String jSessionID;
	
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

		 //  checking whats in the q
		System.out.println(priority.toString()); 
	}	

	/**
	 * Add to q if not in set that stores visited sites
	 * @param link
	 */
	public void addLink(String link) {
		// add link ot q if not in set that indicates sites visited
		if(!visited.contains(link) && !priority.contains(link)) {
			priority.add(link);
		}
	}

	/**
	 * Getting next page in queu and adding it to the set of pages visited.
	 *  
	 * Problably shouldnt be returning null though ****
	 */
	public String getNextPage() {
		try {
			visited.add(priority.peek());
			return priority.poll();
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