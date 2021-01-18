package com.chris.spider;

import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
	
	private static final File config= new File("config.properties");

	private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.christopherfjohnson.com"));
	//private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com", "stage-st-stage.qa.siliconmtn.com"));
	
	private static LinkManager linkManager = new LinkManager();
	private static ConnectionManager connectMan = new ConnectionManager();
	
	/**
	 * Method to connect the page handle the response.
	 * If the connection manager gets an ok response it will pass the bufferred reader 
	 * to the parer to get more links for the link manager and write to a file.
	 * @param linkMan
	 * @throws IOException 
	 */
	public static void connect(LinkManager linkMan) {
		connectMan = new ConnectionManager();
		//connect to page
		System.out.println("Getting next page.");
		connectMan.connectSocket(linkMan.getNextPage());
		

	}

	/**
	 * Using each main link in the above class to build a link manger and crawl each seperately.
	 * I will probably have an issue if eahc site refers to each other but im using a priority queu
	 * so i could probably just add both to the same link mnanager and it wont matter since its sorted.
	 * @param args
	 */
	public static void main(String... args) {
		System.out.println("Web Spider starting.");
		
		// add base links to link manager
		for (String site: sitesToSearch) {
			System.out.println("Adding pages to Link Manager.");
			linkManager.addLink(site);
		}
		
		// Start Crawling
		
		urlCrawl();		
	}
	
	public static void urlCrawl() {
		System.out.println("Starting page crawl.");
		while(linkManager.hasNew()){
			// get a buffered reader from the current and page get request
			connect(linkManager);
			System.out.println("Connecting link manager.");
			
			//Parser parser = new Parser(linkManager.getURI());
			
			
			// parse the current page
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connectMan.getStream()));
				String line = "";
				while((line = reader.readLine()) != null) {
					System.out.println("&&&&& a line printed &&&&&");
					System.out.println(line.toString());
				}
				System.out.println("Starting page parse.");
				//parser.readPage(connectMan.getStream());
			} catch (IOException e) {
				System.out.println("Could bot get inputStream from socket, exception - " + e);
				e.printStackTrace();
			}
			
			
		}

	
	}
}
