package com.chris.helper;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
	private static Map<String, String> siteMap;
	
	private LinkManager() {
		siteMap = new HashMap<String, String>();	
	}
	
	public String getNextPage() {
		// if page has not been traversed 	
		return "";
	}

	public void savePage (String page) {
		List<String> url = Arrays.asList(page.split("/")); 
		for(int i=0; i<url.size(); i++ ){
			// traverse hasmap to check if each value exists
			// check base url pos 1
			// check next part of hashmap
		}
	}
	public boolean hasNew(){
		// check if map has url that has not been read
		return false;
	}
	

	
}
