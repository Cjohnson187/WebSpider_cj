package com.chris.helper;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/****************************************************************************
 * <b>Title</b>: Parser.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> The parser will use a buffer to read from the page and
 * a buffer to write to a file for or save a link for the connection manager.
 * <b>Copyright:</b> Copyright (c) 2020
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Dec 21, 2020
 * @updates:
 ****************************************************************************/

public class Parser {
	
	private final String fileDir = "files/";
	private String pageUri;
	private File file;
	private List<String> linksToReturn;
	
	/**
	 * Making a parser with a socket and string for the page URI
	 * @param uri
	 * @param socket
	 */
	public Parser(String pageName) {
		linksToReturn = new ArrayList<String>();
		pageUri = pageName;
		file = new File(fileDir+ pageName);
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
				linksToReturn.add(pageUri + linkFound);
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



	
	
}
