package com.chris.helper;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/****************************************************************************
 * <b>Title</b>: Parser.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> The parser will recieve a file from the connection
 * manager and return a list of links found.
 * <b>Copyright:</b> Copyright (c) 2020
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Jan 5, 2021
 * @updates:
 ****************************************************************************/

public class Parser {
	
	private String hostName;
	private File file;
	private List<String> linksToReturn;
	
	
	/**
	 * Creating a parser and initializing a file and host 
	 * name that was passed
	 * @param file
	 * @param pageUri
	 */
	public Parser(File file, String hostName) {
		linksToReturn = new ArrayList<String>();
		this.file = file;
		this.hostName = hostName;
	}
	
	/**
	 * Return links found by the parser.
	 * @return
	 */
	public List<String> getLinksFound(){
		return linksToReturn;
	}
	
	/**
	 * Parse the text file for the current site.
	 * @param socketStream
	 * @throws IOException 
	 */
	public void parsePage() throws IOException {
		Document doc = Jsoup.parse(file, "UTF-8", hostName);
		Elements links = doc.select("a[href]");
		String linkFound = "";
		
		for(Element link: links) {
			linkFound = extractLink(link.toString());
			//TODO delete println
			System.out.println("link found" + linkFound);
			if(linkFound.length() > 1 && linkFound.charAt(0) == '/') {
				linksToReturn.add(getHostName() + linkFound);
			}
		}
		//TODO do something with jsession id
		Elements response = doc.select("JSESSIONID");
		//TODO delete println
		System.out.println("jsessionID" + response.toString());
	}
	
	/**
	 * Get substring that is 
	 * @param line
	 * @return
	 */
	public String extractLink(String line) {
		StringBuilder linkFound = new StringBuilder();
		boolean link = false;
		for(int i=0; i< line.length(); i++) {
			if(line.charAt(i) == '"' && link == false) {
				link = true;
				continue;
			} else if(line.charAt(i) == '"' && link == true) {
				break;
			}
			if (link == true) {
				linkFound.append(line.charAt(i));
			}
		}
		return linkFound.toString();
	}
	
	/**
	 * Getting the host name of the current page.
	 * @return
	 */
    private String getHostName() {
		StringBuilder host = new StringBuilder();
		try {
			for (int i=0; i< hostName.length(); i++) {
				if (hostName.charAt(i) != '/') {
					host.append(hostName.charAt(i));
				} else break;
			}
		} catch(NullPointerException e) {
			System.out.println("Error getting host name. Nullpointer Exception -" + e);
		}
		return host.toString();
	}




	
	
}
