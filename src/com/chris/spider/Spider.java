package com.chris.spider;

import org.jsoup.*;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chris.helper.ConnectionManager;
import com.chris.helper.LinkManager;

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
	
	/**
	 * configuration file directories / maybe login
	 */
	private static final File config= new File("config.properties");
	
	/**
	 * Hard coding the sites we need since we are only going to two main sites.
	 */
	private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com"));
	//private static List<String> sitesToSearch = new ArrayList<>(Arrays.asList("www.siliconmtn.com", "stage-st-stage.qa.siliconmtn.com"));
	
	
	/**
	 * Link Manager will handle 
	 */
	private static LinkManager linkManager = new LinkManager();
	
	/**
	 * Method to connect the page handle the response.
	 * If the connection manager gets an ok response it will pass the bufferred reader 
	 * to the parer to get more links for the link manager and write to a file.
	 * @param linkMan
	 * @throws IOException 
	 */
	public static BufferedReader connect(LinkManager linkMan) {
		ConnectionManager connectMan = new ConnectionManager();
			
		//connect to page
		connectMan.connectSocket(linkMan.getNextPage());
		
		// sent get request and returns a socket reader
		return connectMan.sendGet();
	}

	/**
	 * The parse method will take a reader from the connection manager and read the body in order to 
	 * get more links 
	 * @param reader
	 */
	public static void parse(BufferedReader reader) {
		//File out = new File(linkManager.get);
		FileWriter writer;
		String line = "";
		try {
			while((line = reader.readLine()) != null) {
				System.out.println(line.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("outy 5000");
		
	}
	
	/**
	 * Using each main link in the above class to build a link manger and crawl each seperately.
	 * I will probably have an issue if eahc site refers to each other but im using a priority queu
	 * so i could probably just add both to the same link mnanager and it wont matter since its sorted.
	 * @param args
	 */
	public static void main(String... args) {
		System.out.println("Starting");
		BufferedReader responseReader;
		
		// add base links to link manager
		for (String site: sitesToSearch) {
			linkManager.addLink(site);
		}
		

		while(linkManager.hasNew()){
			// get a buffered reader from the current and page get request
			responseReader = connect(linkManager);
			parse(responseReader);
			// connect
			// parse
		}

	}
}
