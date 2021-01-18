package com.chris.helper;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	private String fileName;
	private Document page;
	private String baseUri;
	private Elements linksFound;
	private String cookie;


	/**
	 * Empty constructor
	 */
	public Parser(String uri) {
		baseUri = uri;
		fileName = uri.replaceAll("/", "_").replaceAll(":", "-");
	}
	
	/**
	 * Look for cookie and in document.
	 * @param doc
	 */
	private void checkForCookie(Document doc) {
	
	}
	
	/**
	 * Parse the goiven input stream from a page.
	 * @param socketStream
	 */
	public void readPage(InputStream socketStream) {
		// Make buffered writer with fileName.
		System.out.println("filename");
		try (BufferedWriter out = new BufferedWriter(new FileWriter("files/"+fileName))){
			System.out.println("Starting document");
			page = Jsoup.parse(socketStream, "UTF-8", baseUri);
			// Parse links with Jsoup
			System.out.println("Getting links from document.");
			linksFound = page.select("a[href]");
			
			//TODO check for cookie
			
			// write to page from Jsoup document.
			System.out.println("Starting to write from document.");
			out.write(page.body().text());
			out.close();
			System.out.println("links found =  " + linksFound.toString());
			
		} catch (IOException e) {
			System.out.println("Could not read line, exception - " + e);
			e.printStackTrace();
		}
	}
	
}
