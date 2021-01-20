package com.chris.spider;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chris.helper.ConnectionManager;
import com.chris.helper.LinkManager;
import com.chris.helper.Parser;

/****************************************************************************
 * <b>Title</b>: Spider.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> This is the main runner for the Web Spider. It will have
 * a list of base pages to read and hold other pages found in order. THe connection
 * manager will connect to a given page and if the response is ok it will
 * pass the buffered reader to the parser to write the page and the link manager 
 * will save the read page and continue with the next new page.
 * <b>Copyright:</b> Copyright (c) 2020
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Jan 5, 2021
 * @updates:
 ****************************************************************************/


public class Spider {
	// was going to use a config gile
	private static final File config= new File("config/config.properties");

	private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com"));
	//private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com", "stage-st-stage.qa.siliconmtn.com/admintool"));
	private static List<String> adminToolSitesToSearch = new ArrayList<>(Arrays.asList("https://stage-st-stage.qa.siliconmtn.com/admintool", 
			"https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=FLUSH_CACHE", 
			"https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=stats&actionId=FLUSH_CACHE", 
			"https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=SCHEDULE_JOB_INSTANCE&organizationId=SMT_TEST", 
			"https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=WEB_SOCKET&organizationId=SMT_TEST",
			"https://stage-st-stage.qa.siliconmtn.com/sb/admintool?cPage=index&actionId=ERROR_LOG&organizationId=SMT_TEST"));																					
	
	private static LinkManager linkManager;
	private static ConnectionManager connectMan;
	
	/**
	 * Send post to body with login /cookie to parse pages.
	 */
	public static void SecurePageCrawl() {
		
	}
	
	/**
	 * Crawling unsecured URLs from the link manager.
	 */
	public static void urlCrawl() throws IOException {
		
		while(linkManager.hasNew()){
			connectMan = new ConnectionManager(linkManager.getNextPage());
			Parser parser = new Parser(connectMan.getPage(), connectMan.GetBaseURL());
			parser.parsePage();
			
			if(parser.getLinksFound() != null) {
				linkManager.addLink(parser.getLinksFound(), connectMan.GetBaseURL());
			}
		}
	}


	/**
	 * The main will be used to add the initial links and crawl the 
	 * regular pages and the secure pages.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String... args) throws IOException {
		String s = "https://stage-st-stage.qa.siliconmtn.com/admintool?User=Chris.johnson%40siliconmtn.com&pw=1040SMTdisco%24&Content-Length=70";
		System.out.println(s.length());
		linkManager = new LinkManager();
		
		// add base links to link manager
		linkManager.addLink(sitesToSearch);	
		
		urlCrawl();
	
	}
	
	
	
}
