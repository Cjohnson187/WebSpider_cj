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
	
	private String pageUri;
	private File file;
	private List<String> linksToReturn;
	
	/**
	 * Making a parser with a socket and string for the page URI
	 * @param uri
	 * @param socket
	 */
	public Parser(File file, String pageUri) {
		linksToReturn = new ArrayList<String>();
		this.file = file;
		this.pageUri = pageUri;
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
		Document doc = Jsoup.parse(file, "UTF-8", pageUri);
		Elements links = doc.select("a[href]");
		String linkFound = "";
		
		for(Element link: links) {
			linkFound = extractLink(link.toString());
			if(linkFound.length() > 1 && linkFound.charAt(0) == '/') {
				linksToReturn.add(GetBaseURL() + linkFound);
			}
		}
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
	 * Getting the base url of the current page.
	 * @return
	 */
    private String GetBaseURL() {
		StringBuilder base = new StringBuilder();
		try {
			for (int i=0; i< pageUri.length(); i++) {
				if (pageUri.charAt(i) != '/') {
					base.append(pageUri.charAt(i));
				} else break;
			}
		} catch(NullPointerException e) {
			System.out.println("Error getting base url. Nullpointer Exception -" + e);
		}
		return base.toString();
	}




	
	
}
