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
 * <b>Description: </b> The parser is just used to parse a file for links or 
 * a string for the jsessionID.

 * <b>Copyright:</b> Copyright (c) 2020
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 2.0
 * @since Feb 4, 2021
 * @updates:
 ****************************************************************************/

public class Parser {
	
	/**
	 * Empty constructor.
	 */
	public Parser() {
	}
	
	/**
	 * Method to parse a response String and get JsessionID
	 * @param response
	 * @return
	 */
	public static String getCookieFromResponse(String response) {
		// getting just jsessionID
		if(response.contains("JSESSIONID")) {
			response = response.substring(response.indexOf("JSESSIONID"));
			response = response.substring(11, response.indexOf(";"));	
			return response;
		}
		return null;
	}
	
	/**
	 * Parse the text file and search for more sites to add to the link manager.
	 * The host name is used to build full URLs but it is not necessary because im
	 * splitting them later.
	 * @param file
	 * @param hostName
	 * @return
	 * @throws IOException
	 */
	public static List<String> getLinksFromFile(File file, String hostName) throws IOException {
		List<String> linksToReturn = new ArrayList<>();
		Document doc = Jsoup.parse(file, "UTF-8", hostName);
		Elements links = doc.select("a[href]");
		String dirFound = "";
		
		for(Element link: links) {
			dirFound = extractLink(link.toString());
			if(dirFound.length() > 1 && dirFound.charAt(0) == '/') {
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
		line = line.substring(line.indexOf('"')+1);
		return line.substring( 0, line.indexOf('"'));
	}
}
