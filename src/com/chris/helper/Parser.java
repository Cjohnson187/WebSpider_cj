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
	
	/**
	 * Empty constructor.
	 * @param file
	 * @param pageUri
	 */
	public Parser() {

	}
	
	/**
	 * Method to parse a response and get JsessionID
	 * @param response
	 * @return
	 */
	public static String getCookieFromResponse(String response) {
		// getting jsessionid
		if(response.contains("JSESSIONID")) {
			response = response.substring(response.indexOf("JSESSIONID"));
			response = response.substring(11, response.indexOf(";"));	
			return response;
		}
		return "";
	}
	
	/**
	 * Parse the text file for the current site.
	 * @param socketStream
	 * @throws IOException 
	 */
	public static List<String> getLinksFromFile(File file, String hostName) throws IOException {
		List<String> linksToReturn = new ArrayList<>();
		Document doc = Jsoup.parse(file, "UTF-8", hostName);
		Elements links = doc.select("a[href]");
		String dirFound = "";
		
		for(Element link: links) {
			dirFound = extractLink(link.toString());
			System.out.println("lin 68 parser, link found = " + dirFound);
			//TODO delete println
			//System.out.println("lin 65 parser, link found = " + dirFound);
			
			if(dirFound.length() > 1 && dirFound.charAt(0) == '/') {
				System.out.println("lin 68 parser, link found = " + dirFound);
				linksToReturn.add(dirFound);
			}
		}
		return linksToReturn;
	}
	
	/**
	 * Get substring that is the link
	 * @param line
	 * @return
	 */
	private static String extractLink(String line) {
		// linkFound = new StringBuilder();
		//boolean link = false;
		line = line.substring(line.indexOf('"'));
		line = line.substring(0 , line.indexOf('"'));
//		for(int i=0; i< line.length(); i++) {
//			if(line.charAt(i) == '"' && link == false) {
//				link = true;
//				continue;
//			} else if(line.charAt(i) == '"' && link == true) {
//				break;
//			}
//			if (link == true) {
//				linkFound.append(line.charAt(i));
//			}
//		}
		return line;
	}
	
	/**
	 * Getting the host name of the current page.
	 * @return
	 */




	
	
}
