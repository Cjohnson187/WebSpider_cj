package com.chris.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.util.Properties;

/****************************************************************************
 * <b>Title</b>: ConnectionManager.java <b>Project</b>: WebSpider
 * <b>Description: </b> The connection manager will be used by the spider to
 * make requests to the current page and save the response to a file
 * for the parser. 
 * It should also be able to send posts with login info as well as a cookie
 * if it exists.
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

    private static String directory;
    
    private LinkManager linkMan;
    private static SSLSocket socket;
    private static BufferedReader socketReader;
    
    /**
     * Building ConnectionManager with linkManager to manage pages while socket is connected
     * @param linkMan
     */
    public ConnectionManager(LinkManager linkMan) {
    	this.linkMan = linkMan;
    	this.hostName = linkMan.getHost();
    	// I dont think i need this
    	this.directory = "";
    }
    
    /**
     * Get Basic pages
     * @throws IOException 
     */
    public void getBasic() throws IOException {
    	
    	while(linkMan.hasNew()) {
    		directory = linkMan.getNextPage();
    		connectSocket();
        	sendGet();
    	}
    }
    
    /**
     * Uses Post login for admintool pages
     * @throws IOException 
     */
    public void getAdminToolPages() throws IOException {
    	while(linkMan.hasNew()) {
    		directory = linkMan.getNextPage();
    		connectSocket();
        	sendPost();
    	}
    	//connectSocket();
    	//sendPost();
    	//socketReader.close();
    }

    /**
     * Make connection to page and build writer / reader
     */
    private void connectSocket() {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
        	socket = (SSLSocket) factory.createSocket(hostName, 443);
        	socket.startHandshake();
		} catch (UnknownHostException e) {
			System.out.println("unknown host exception - " + e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}       
    }

    /**
     * Make get request for the current page
     */
    private void sendGet(){
    	Pair responsePair = new Pair();

    	try (PrintWriter socketWriter = new PrintWriter(
                new BufferedWriter(
                new OutputStreamWriter(
                socket.getOutputStream())))){
    		
            socketWriter.println("GET " + directory + " HTTP/1.1");
            socketWriter.println("Host: " + hostName);
            socketWriter.println("User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT");
            socketWriter.println("Accept-Language: en-us");
            socketWriter.println("Connection: Keep-Alive");
            // adding carriage return
            socketWriter.println();
            // send request
            socketWriter.flush();
            
            // making socket reader
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // get response response header and body
        	responsePair = getResponse();
        	
        	linkMan.addLinks(Parser.getLinksFromFile(responsePair.getFile(), hostName));

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}

    }
    

    /**
     * Make post request for login on secure site
     */
    private void sendPost(){
    	// Pair for Response string and html file
    	Pair responsePair = new Pair();
    	
    	try (PrintWriter socketWriter = new PrintWriter(
                new BufferedWriter(
                new OutputStreamWriter(
                socket.getOutputStream())))){
    		
    		//writing params for login
			String params = "?requestType=reqBuild&pmid=ADMIN_LOGIN";
			params += "&emailAddress=" + PROPERTIES.getProperty("usernamme");
			params += "&password=" + PROPERTIES.getProperty("password") + "&l=";

    		if (cookie != null) {
    			socketWriter.println("Post " + directory + " HTTP/1.1");
    	        socketWriter.println("Host: " + hostName);
    	        socketWriter.println("Cookie: JSESSIONID=" + cookie+";");
    	        socketWriter.println("Content-Encoding: deflate, gzip");
    	        socketWriter.println("User-Agent: Mozilla/4.0 (compatible; MSIE5.01; Windows NT");
    	        socketWriter.println("Accept-Language: en-us");
    	        socketWriter.println("Connection: Keep-Alive");
    	            
    	        // adding carriage return
    	        socketWriter.println();
    	        // send request
    	        socketWriter.flush();
    			}
    		else {
        		socketWriter.println("POST " + directory + " HTTP/1.1");
        	    socketWriter.println("Host: " + hostName);
        	    socketWriter.println("content-type: application/x-www-form-urlencoded");
        	    socketWriter.println("user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36");
        	    socketWriter.println("Connection: Keep-Alive");
        	    socketWriter.println("Content-Encoding: deflate, gzip");
        	    socketWriter.println("Content-Length: " + params.length());
        	    socketWriter.println();
        	    socketWriter.println(params);
        	    socketWriter.println();
        	    // send request to page
        	    socketWriter.flush();  
    		}
                socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // get response response header and body
            	responsePair = getResponse();
            	System.out.println("response -  " + responsePair.getResponse());
            	String cookieCheck = Parser.getCookieFromResponse(responsePair.getResponse());
            	if(cookieCheck != "") cookie = URLEncoder.encode(cookieCheck, StandardCharsets.UTF_8.toString());;


 
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
			while ((line = socketReader.readLine()) != null) {
				if (isResponse == true) response += line.toString();
				// Response finished starting to read html
				if (line.toString().contains("<!DOCTYPE html>")) isResponse = false;
				// writing page to file
				if(!isResponse) out.write(line);
				//breaking here because my reader wasn't closing
				if (line.toString().contains("</html>")) break;
			}
			responsePair = new Pair(response, file);	
		} catch (Exception e) {
			System.out.println("Could not read response Exception  e - " + e);
		}
    	return responsePair;
    }
    
	/**
	 * Make a file name by replacing reserved characters to save in a directory.
	 * @return
	 */
	private static String makeFileName() {
		return directory.replaceAll("/", "_").replaceAll(":", "-");
	}
    
}

