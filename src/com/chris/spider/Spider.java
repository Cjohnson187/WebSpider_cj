package com.chris.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chris.helper.ConnectionManager;
import com.chris.helper.LinkManager;

/****************************************************************************
 * <b>Title</b>: Spider.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> The spider jsut initializes the ConnectionManager
 * and LinkManager to start the parse process for each 
 * 
 * -- could fix this by sorting the links differently and handling
 * responses dynamically.
 * 
 * <b>Copyright:</b> Copyright (c) 2020
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 2.0
 * @since Feb 4, 2021
 * @updates:
 ****************************************************************************/

public class Spider {
		
	/**
	 * Crawl the siliconmtn.com sites and find new links to parse.
	 * @param linkMan
	 * @throws IOException
	 */
	public static void urlCrawl(LinkManager linkMan) throws IOException {
		// initialize ConnectionManager with LinkManager that has a starting site
		// which will find new links
		ConnectionManager connectMan = new ConnectionManager(linkMan);
		connectMan.getBasic();
	}
	
	/**
	 * Crawl the secure sites that need a login/cookie
	 * @param linkMan
	 * @throws IOException
	 */
	public static void adminCrawl(LinkManager linkMan) throws IOException {
		// initialize ConnectionManager with LinkManager that has a starting site.
		ConnectionManager connectMan = new ConnectionManager(linkMan);
		// crawl "https://www.siliconmtn.com/sb/admintool/* sites
		// not  finding new links
		connectMan.getAdminToolPages();
	}


	/**
	 * The main is being used to separate each process.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException {
		// Crawl the easy pages.
		LinkManager linkManager = new LinkManager("https://www.siliconmtn.com/");
		urlCrawl(linkManager);
		
		// Crawl admintool pages the need cookie/login
		LinkManager adminLinkManager = new LinkManager("https://www.siliconmtn.com/sb/admintool");
		adminCrawl(adminLinkManager);
	}
}
