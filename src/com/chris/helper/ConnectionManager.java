package com.chris.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/****************************************************************************
 * <b>Title</b>: ConnectionManager.java <b>Project</b>: WebSpider
 * <b>Description: </b> The connection manager goes through a list of links
 * to connect them and request a response. It should dynamically handle a
 * but it only crawls unsecured sites to get more sites to crawl. 
 * For the secured sites it makes a single post request for the first secured 
 * site and gets the cookie to search the following sites in the list but 
 * does not search for more sites to crawl.
 * <b>Copyright:</b> Copyright (c) 2021 <b>Company:</b> Silicon Mountain
 * Technologies
 * 
 * @author Chris Johnson
 * @version 2.0
 * @since Feb 04, 2021
 * @updates:
 ****************************************************************************/

public class ConnectionManager {
	// properties file for login
	private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
	private static final Properties PROPERTIES = propertiesLoader.readPropFile();
	private static final String FILE_DIR = PROPERTIES.getProperty("fileDir");
	
    private String cookie;
    private String hostName;
    private File file;

    private static String path;
    
    private LinkManager linkMan;
    private SSLSocket socket;
    private SSLSocketFactory factory;
    private BufferedReader socketReader;
    
    /**
     * Building ConnectionManager with linkManager to manage pages while socket is connected
     * @param linkMan
     */
    public ConnectionManager(LinkManager linkMan) {
    	this.linkMan = linkMan;
    	this.hostName = linkMan.getHost();
    	this.path = "";
    	factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
    }
    
    /**
     * Get Basic pages and look for more to crawl
     * @throws IOException 
     */
    public void getBasic() throws IOException {
    	
    	while(linkMan.hasNew()) {
    		path = linkMan.getNextPage();
    		connectSocket();
        	sendGet();
    	}
    }
    
    /**
     * Uses post login for first admin page and gets a 
     * cookie to search the rest.
     * @throws IOException
     */
    public void getAdminToolPages() throws IOException {
    	// getting the first login page with the post to get the cookie
    	path = linkMan.getNextPage();
		connectSocket();
		sendPost();
		
		// list of sites to add - This should be done on the spider
		// but the priority Q reorders the sites by smallest to largest
		// which is a problem because im  not dynamically handling responses yet
		List<String> sites = new ArrayList<String>();
		sites.add("https://www.siliconmtn.com/admintool");
		sites.add("https://www.siliconmtn.com/admintool?cPage=index&actionId=FLUSH_CACHE");
		sites.add("https://www.siliconmtn.com/admintool?cPage=index&actionId=SCHEDULE_JOB_INSTANCE&organizationId=BMG_SMARTTRAK");
		sites.add("https://www.siliconmtn.com/admintool?cPage=index&actionId=WEB_SOCKET&organizationId=BMG_SMARTTRAK");
		sites.add("https://www.siliconmtn.com/admintool?cPage=index&actionId=ERROR_LOG&organizationId=BMG_SMARTTRAK");
		linkMan.addLinks(sites);


		
		// Start crawling the remaining links now that there is a cookie.
    	while(linkMan.hasNew()) {
    		System.out.println("old path- " + path);
    		path = linkMan.getNextPage();
    		
    		System.out.println("new path- " + path);
    		connectSocket();
        	sendPost();
    	}
    }

    /**
     * Make connection to page and test handshake, probably dont need the test
     */
    private void connectSocket() {
        try {
			socket = (SSLSocket) factory.createSocket(hostName, 443);
			//socket.startHandshake(); 
		} catch (IOException e) {
			System.out.println("could not connect to socket, exception - " + e);
			e.printStackTrace();
		}
    }

    /**
     * Make get request for unsecured pages which does not get cookies yet.
     */
    private void sendGet(){
    	Pair responsePair = new Pair();

    	try (PrintWriter socketWriter = new PrintWriter(
                new BufferedWriter(
                new OutputStreamWriter(
                socket.getOutputStream())))){
    		
            socketWriter.println("GET " + path + " HTTP/1.1");
            socketWriter.println("Host: " + hostName);
            socketWriter.println("User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT");
            socketWriter.println("Accept-Language: en-us");
            socketWriter.println("Connection: Keep-Alive");
            
            // adding carriage return
            socketWriter.println();
            socketWriter.flush();
            
            // making socket reader
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // get response response header and body
        	responsePair = getResponse();
        	// add new links found
        	linkMan.addLinks(Parser.getLinksFromFile(responsePair.getFile(), hostName));

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
    }

    /**
     * Make a post get depending on if a cookie exists but 
     * needs to be dynamic check for redirects
     */
    private void sendPost(){
    	// Pair for Response string and html file
    	Pair responsePair = new Pair();
    	
    	try (PrintWriter socketWriter = new PrintWriter(
                new BufferedWriter(
                new OutputStreamWriter(
                socket.getOutputStream())))){
 
    		//Building params for login.
			String params = "requestType=reqBuild&pmid=ADMIN_LOGIN";
			params += "&emailAddress=" + PROPERTIES.getProperty("usernamme");
			params += "&password=" + PROPERTIES.getProperty("password") +"&l=";

	    		if (cookie != null) {
	    			socketWriter.println("GET " + path + " HTTP/1.1");
	    			socketWriter.println("Host: " + hostName);
	    			socketWriter.println("Cookie: JSESSIONID=" + cookie);
	    			socketWriter.println("User-Agent: bgold/1.2");
	    			socketWriter.println();
	    			socketWriter.flush();
	    			// socket reader to get response
	                socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                // get response response header and body
	            	responsePair = getResponse();
	            	
	            	socketReader.close();
	    		}
	    		// Post for first login but i need a check for redirects instead
	    		else {
	    			
	    			socketWriter.println("POST " + path + " HTTP/1.1");
	    			socketWriter.println("Host: " + hostName);
	    			socketWriter.println("Accept-Encoding: gzip, deflate, br");
	    			socketWriter.println("Accept-Language: en-US,en;q=0.9");
	    			socketWriter.println("Cache-Control: no-cache");
	    			socketWriter.println("Content-Type: application/x-www-form-urlencoded");
	    			socketWriter.println("User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");
	    			socketWriter.println("Content-Length: " + params.length());
	    			socketWriter.println();
	    	        // login properties
	    			socketWriter.println(params);
	    			socketWriter.println();
	    			socketWriter.println();
	    			socketWriter.flush();
	
	                socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                // get response response header and body
	            	responsePair = getResponse();
	            	//System.out.println("response -  " + responsePair.getResponse());
	            	cookie = Parser.getCookieFromResponse(responsePair.getResponse());
	            	
	            	socketReader.close();
	    		}
		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
    }
    
    /**
     * Getting response from socket.
     */
    private Pair getResponse() {
    	// Making a pair from the string response and File to parse later
    	Pair responsePair = new Pair();
    	String response = "";
    	String line = "";

    	file = new File(FILE_DIR + makeFileName());
    	
    	try (BufferedWriter out = new BufferedWriter(new FileWriter(file))){

    		// Using this boolean to split response and html.
			boolean isResponse = true;
			boolean redirect  = false;
			while ((line = socketReader.readLine()) != null) {
				// need a better check to know if redirect
				if (line.toString().contains("HTTP/1.1 302")) redirect = true;
				// Response finished starting to read html
				if (line.toString().contains("<!DOCTYPE html>")) isResponse = false;
				// save response to string
				if (isResponse) response += line;
				// writing page to file
				if(redirect && line.contains("Location: /admintool")) break;
				if(!isResponse) out.write(line);
				//breaking here because my reader wasn't closing
				if (line.toString().contains("</html>")) break;
			}
			socketReader.close();
			responsePair = new Pair(response, file);
			
		} catch (Exception e) {
			System.out.println("Could not read response Exception  e - " + e);
		}
    	// need check in case its empty
    	return responsePair;
    }
    
	/**
	 * Make a file name by replacing reserved characters to save in a directory.
	 * @return
	 */
	private static String makeFileName() {
		return path.replaceAll("/", "_").replaceAll(":", "-");
	} 
}


