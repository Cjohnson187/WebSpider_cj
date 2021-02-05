package com.chris.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.net.UnknownHostException;
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
    
    private String directory;
    private String hostName;
    private static File file;
    
    private static String link;
    
    private LinkManager linkMan;
    

    private static SSLSocket socket;
    BufferedReader socketReader;
    
    /**
     * Building ConnectionManager with linkManager to manage pages while socket is connected
     * @param linkMan
     */
    public ConnectionManager(LinkManager linkMan) {
    	this.linkMan = linkMan;
    	this.hostName = linkMan.getHost();
    	this.directory = "";
    }
    
    /**
     * Get Basic pages
     */
    public void getBasic() {
    	connectSocket();
    	sendGet();
    }
    
    /**
     * Uses Post login for admintool pages
     */
    public void getAdminToolPages() {
    	connectSocket();
    	//TODO fix post method
    	sendPost();
    }

    /**
     * Make connection to page and build writer / reader
     */
    private void connectSocket() {
    	Pair responsePair = new Pair();
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try (SSLSocket socket = (SSLSocket) factory.createSocket(hostName, 443)) {
        	socket.startHandshake();
        	while(linkMan.hasNew()) {
        		directory = linkMan.getNextPage();
            	sendGet();
            	// Getting String(header) and File(html) to parse with jsoup.
            	responsePair = getResponse();
            	cookie = Parser.parseResponse(responsePair.getResponse());
            	linkMan.addLinks(Parser.parsePage(file, hostName));
            	
            	
            	//TODO break for now just to test
            	break;
        	}
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

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
    }
    
    /**
     * Getting response from socket.
     */
    private static Pair getResponse() {
    	Pair responseMap = new Pair();
    	String response = "";
    	file = new File(FILE_DIR + makeFileName());
    	try (BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    			BufferedWriter out = new BufferedWriter(new FileWriter(file))){
		
    		String line = "";
			boolean isResponse = true;
			while ((line  = socketReader.readLine()) != null) {
				if (isResponse) {
					// Concatenating response.
					response += line;
					
					//TODO delete println
					System.out.println("Response line - " + line);
				}
				// Response finished starting to read html
				if (line.contains("<html>")) isResponse = false;
				// writing page to file
				if(!isResponse) out.write(line);
				
				if (line.contains("</html>")) break;
			}
			responseMap = new Pair(response, file);
    		
		} catch (Exception e) {
			System.out.println("Could not read response Exception  e - " + e);
		}
    	return responseMap;
    }
    
    
    
    //TODO fix post
    /**
     * Make post request for login on secure site
     */
    private void sendPost(){
		try (PrintWriter socketWriter = new PrintWriter(socket.getOutputStream())){
			// writing params
			String params = "?requestType=reqBuild&pmid=ADMIN_LOGIN";
			params += "&emailAddress=" + PROPERTIES.getProperty("usernamme");
			params += "&password=" + PROPERTIES.getProperty("password") + "&l=";

			socketWriter.println("POST " + directory + " HTTP/1.1");
	        socketWriter.println("Host: " + hostName);
	        socketWriter.println("content-type: application/x-www-form-urlencoded");
	        socketWriter.println("user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36");
	        socketWriter.println("Content-Length: " + params.length()); // length of message needed
	        
	        // writing params
	        socketWriter.println();
	        socketWriter.println(params);
	        socketWriter.println();
	        // send request to page
	        socketWriter.flush();    
	        
	        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        
	    	//savePage();

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
    }
    
	/**
	 * Make a file name by replacing reserved characters to save in a directory.
	 * @return
	 */
	private static String makeFileName() {
		return link.replaceAll("/", "_").replaceAll(":", "-");
	}
 
    /**
     * Method to check if we have an existing cookie.
     * @return
     */
    private boolean checkForCookie() {
        if (cookie != null) return true;  
        return false;
    }

    
    //TODO delete theses unused methods
        
//	/**
//	 * Getting the host name of the current page.
//	 * @return
//	 */
//    public String getHostName() {
//		StringBuilder host = new StringBuilder();
//		//removing protocol
//		String newLink = link.replace("http://", "");
//		newLink = newLink.replace("https://", "");
//		//System.out.println(" +++++ " + newLink);
//		try {
//			for (int i=0; i< newLink.length(); i++) {
//				if (newLink.charAt(i) != '/') {
//					host.append(newLink.charAt(i));
//				} else break;
//			}
//			// checking for address
//			if(newLink.length() > host.length()) {
//				directory = newLink.replace(host.toString(), "");
//			}
//		} catch(NullPointerException e) {
//			System.out.println("Error getting host name. Nullpointer Exception -" + e);
//		}
//		hostName = host.toString();
//		return host.toString();
//	}
//    
    /**
     * Save the page including response to a file for the parser.
     */
//    private void savePage() {
//		file = new File(FILE_DIR + makeFileName());
//		String line="";
//		
//		try ( BufferedWriter out = new BufferedWriter(new FileWriter(file)) ){
//		
//			while((line = socketReader.readLine()) != null) {
//				//TODO delete println
//				System.out.println(line.toString());
//				out.write(line);
//				// breaking at and of html page because the reader would no stop
//				if(line.contains("</html>")) {
//					break;
//				}
//			}
//			socketReader.close();
//		} catch (IOException e) {
//			System.out.println("Error reading from socket input stream, exception - " + e);
//			e.printStackTrace();
//		}
//	}
//		
	

//    /**
//     * saving cookie in connnectionManager
//     * @param newCookie
//     */
//    public void saveCookie(String newCookie) {
//        this.cookie = newCookie;
//    }
//    
//    /**
//     * Getting Cookie for file writer.
//     * @return
//     */
//    public String getCookie() {
//        return cookie;
//    }
//    
//    // Close readers G*$#@M%&*!!!!!

}
