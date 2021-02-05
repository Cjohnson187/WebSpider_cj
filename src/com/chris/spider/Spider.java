package com.chris.spider;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

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
	
	private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("https://www.siliconmtn.com/"));
	//private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com", "stage-st-stage.qa.siliconmtn.com/admintool"));
	private static List<String> adminToolSitesToSearch = new ArrayList<>(Arrays.asList("https://stage-st-stage.qa.siliconmtn.com/admintool"));																					
	
	private static LinkManager linkManager;
	private static ConnectionManager connectMan;
	
	
	
	/**
	 * Crawl the unsecured sites
	 * @throws IOException
	 */
	public static void urlCrawl() throws IOException {
		
		while(linkManager.hasNew()){
			connectMan = new ConnectionManager(linkManager.getNextPage());
		
			Parser parser = new Parser(connectMan.getPageFile(), connectMan.getHostName());
			parser.parsePage();
			
			if(parser.getLinksFound() != null) {
				linkManager.addLink(parser.getLinksFound(), connectMan.getHostName());
			}
		}
	}
	
	/**
	 * Crawl the secure sites
	 * @throws IOException
	 */
	public static void adminCrawl() throws IOException {
		connectMan = new ConnectionManager(linkManager.getNextPage());
		//TODO delete println
		System.out.println("pageFile and hostName " + connectMan.getSecurePageFile()+ " " + connectMan.getHostName());
		connectMan.getSecurePageFile();
		//Parser parser = new Parser(connectMan.getSecurePageFile(), connectMan.getHostName());
		//parser.parsePage();
		
	}


	/**
	 * The main will be used to add the initial links and crawl the 
	 * regular pages and the secure pages.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String... args) throws IOException {
		linkManager = new LinkManager();
		
		// add base links to link manager
		linkManager.addLink(sitesToSearch);	
		//linkManager.addLink(adminToolSitesToSearch);
		
		urlCrawl();
		//adminCrawl();
	
	}
	
	
	
}
