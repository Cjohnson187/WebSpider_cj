package com.chris.spider;

import java.net.Socket;

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
	private Socket socket;
	private String socketAddress;
	
	
	LinkManager(String link) {
		this.socketAddress = link;
		
	}
	
	private boolean hasNewLink() {
		
		
		return false;
	}

	
}
