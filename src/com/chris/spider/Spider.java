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
 * @since Dec 21, 2020
 * @updates:
 ****************************************************************************/


public class Spider {
	
	private static final File config= new File("config/config.properties");

	private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com"));
	//private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com", "stage-st-stage.qa.siliconmtn.com"));
	
	private static LinkManager linkManager;
	private static ConnectionManager connectMan;
	

	/**
	 * Connecting to the page and saving the response to a text file.
	 * @return
	 * @throws IOException
	 */
	public static String connectPage() throws IOException {
		connectMan = new ConnectionManager(linkManager.getNextPage());
		return connectMan.getPage();
	}
	
	/**
	 * Crawling URLs from the link manager.
	 */
	public static void urlCrawl() {
		while(linkManager.hasNew()){
			try {
				System.out.println("Getting next page.");
				Parser parser = new Parser(connectPage());
				parser.parsePage();
				if(parser.getLinksFound() != null) {
					linkManager.addLink(parser.getLinksFound());
				}
			} catch (IOException e) {
				System.out.println("Failed to connect to web page, exception - " + e);
				e.printStackTrace();
			}
		}
	}


	/**
	 * Using each main link in the above class to build a link manger and crawl each seperately.
	 * I will probably have an issue if eahc site refers to each other but im using a priority queu
	 * so i could probably just add both to the same link mnanager and it wont matter since its sorted.
	 * @param args
	 */
	public static void main(String... args) {
		linkManager = new LinkManager();
		
		// add base links to link manager
		linkManager.addLink(sitesToSearch);	
		
		System.out.println("Web Spider crawling unsecured pages.");
		urlCrawl();		
	}
	
	
	
}
