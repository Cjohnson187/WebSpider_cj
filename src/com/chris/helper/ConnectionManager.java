package com.chris.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

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
 * @version 1.0
 * @since Jan 5, 2021
 * @updates:
 ****************************************************************************/
public class ConnectionManager {

    private String cookie;
    private String link;
    private File file;
    private final String fileDir = "files/";

    private static SSLSocket socket;
    BufferedReader socketReader;
    private InetAddress address;
    
    /**
     * Empty constructor since the URL will most likely be changing a few times.
     */
    public ConnectionManager(String link) {
    	this.link = link;
    }
    
    /**
     * Connect to page and save the response in a document for the parser.
     * @return
     */
    public File getPage() {
    	connectSocket();
    	sendGet();
    	savePage();
    	return file;
    }
    
    public File getSecurePage() {
    	connectSocket();
    	sendPost();
    	return file;
    }

    /**
     * Make connection to page and build writer / reader
     */
    private void connectSocket() {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
			socket = (SSLSocket) factory.createSocket(GetBaseURL(), 443);
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
    	try (PrintWriter socketWriter = new PrintWriter(socket.getOutputStream())){
            // basic post to get  HTML
            socketWriter.println("GET http://" + link + " HTTP/1.1");
            socketWriter.println("Host: " + GetBaseURL());

            // adding carriage return and sending
            socketWriter.println();
            // send request
            socketWriter.flush();
            
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
        
    }
    
    /**
     * Make post request for login on secure site
     */
    private void sendPost(){
		try (PrintWriter socketWriter = new PrintWriter(socket.getOutputStream())){
			//https://www.siliconmtn.com/admintool?emailAddress=chris.johnson%40siliconmtn.com&password=1040SMTdisco%23&l
			System.out.println("address get host name" + address.getHostName());
	        socketWriter.print("POST http://" + address.getHostName() + " HTTP/1.1");
	        socketWriter.print("Host: " + address.getHostName());
	        socketWriter.print("requestType=reqBuild&"); // credentials needed
	        socketWriter.print("requestType=reqBuild&pw=123456&action=login"); // credentials needed
	        socketWriter.println("requestType=reqBuild&pw=123456&action=login"); // credentials needed
	        socketWriter.println("User=blah+blah&pw=123456&action=login"); // credentials needed
	        socketWriter.println("Content-Length: 37"); // length of message needed
	        socketWriter.println("Connection: Keep-Alive");
	        socketWriter.println("Cache-Control: no-cache");
	        // adding carriage return and sending
	        socketWriter.println();
	        // send request to page
	        socketWriter.flush();    
	        
	        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
    }
        
	/**
	 * Getting the base url of the current page.
	 * @return
	 */
    public String GetBaseURL() {
		StringBuilder base = new StringBuilder();
		try {
			for (int i=0; i< link.length(); i++) {
				if (link.charAt(i) != '/') {
					base.append(link.charAt(i));
				} else break;
			}
			
		} catch(NullPointerException e) {
			System.out.println("Error getting base url. Nullpointer Exception -" + e);
		}
		return base.toString();
	}
    
    /**
     * Save the page to a file for the parser.
     */
    private void savePage() {
		file = new File(fileDir+ getFileName());
		String line="";
		
		try ( BufferedWriter out = new BufferedWriter(new FileWriter(file)) ){
			
			
			while((line= socketReader.readLine()) != null) {
				out.write(line);
				if(line.contains("</html>")) {
					break;
				}
			}
			socketReader.close();
		} catch (IOException e) {
			System.out.println("Error reading from socket input stream, exception - " + e);
			e.printStackTrace();
		}
	}
		
	/**
	 * getting the entire URL
	 * @return
	 */
	public String GetURL() {
		return address.getHostName();
	}
	
	/**
	 * Make a file name to save in a directory.
	 * @return
	 */
	private String getFileName() {
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

    /**
     * saving cookie in connnectionManager
     * @param newCookie
     */
    public void saveCookie(String newCookie) {
        this.cookie = newCookie;
    }
    
    /**
     * Getting Cookie for file writer.
     * @return
     */
    public String getCookie() {
        return cookie;
    }
    
    // Close readers G*$#@M%&*!!!!!

}
