package com.chris.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.chris.helper.ConnectionManager;
import com.chris.helper.LinkManager;

/****************************************************************************
 * <b>Title</b>: Spider.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> 
 * 
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
	 * Crawl the siliconmtn.com before admintool.
	 * @throws IOException
	 */
	public static void urlCrawl(LinkManager linkMan) throws IOException {
		// initialize ConnectionManager with LinkManager.
		ConnectionManager connectMan = new ConnectionManager(linkMan);
		connectMan.getBasic();
	}
	
	/**
	 * Crawl the secure sites
	 * @throws IOException
	 */
	public static void adminCrawl(LinkManager linkMan) throws IOException {
		// initialize ConnectionManager with LinkManager.
		ConnectionManager connectMan = new ConnectionManager(linkMan);
		// crawl "https://www.siliconmtn.com/" and link found 
		connectMan.getAdminToolPages();

	}


	/**
	 * The main will be used to add the initial links and crawl the 
	 * regular pages and the secure pages.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String... args) throws IOException {
		// Crawl the easy pages.
		//LinkManager linkManager = new LinkManager("https://www.siliconmtn.com/");
		//urlCrawl(linkManager);
		

		List<String> sites = new ArrayList<String>();
		sites.add("https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=FLUSH_CACHE");
		sites.add("https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=stats&actionId=FLUSH_CACHE");
		sites.add("https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=SCHEDULE_JOB_INSTANCE&organizationId=BMG_SMARTTRAK");
		sites.add("https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=WEB_SOCKET&organizationId=BMG_SMARTTRAK");
		sites.add("https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=ERROR_LOG&organizationId=BMG_SMARTTRAK");
		LinkManager adminLinkManager = new LinkManager("https://stage-st-stage.qa.siliconmtn.com/admintool");
		
		adminLinkManager.addLinks(sites);
		adminCrawl(adminLinkManager);
	}
	
	
	
}
