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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.chris.helper.PropertiesLoader;

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
	private static final PropertiesLoader propertiesLoader = new PropertiesLoader();
	private static final Properties PROPERTIES = propertiesLoader.readPropFile();
    private String cookie;
    private String link;
    private String directory;
    private String hostName;
    private File file;
    private final String FILE_DIR = "files/";
    

    private static SSLSocket socket;
    BufferedReader socketReader;
    private InetAddress address;
    
    /**
     * Empty constructor since the URL will most likely be changing a few times.
     */
    public ConnectionManager(String link) {
    	//TODO delete println
    	System.out.println("prop " + PROPERTIES.getProperty("usernamme"));
    	System.out.println("link - " + link);
    	this.link = link;
    	this.directory = "";
    	this.hostName = "";
    }
    
    /**
     * Connect to page and save the response in a document for the parser.
     * @return
     */
    public File getPageFile() {
    	connectSocket();
    	sendGet();
    	savePage();
    	return file;
    }
    /**
     * Post login for admintool
     * @return
     */
    public File getSecurePageFile() {
    	connectSocket();
    	sendPost();
    	savePage();
    	return file;
    }

    /**
     * Make connection to page and build writer / reader
     */
    private void connectSocket() {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
        	System.out.println("host name in con man " + getHostName());
			socket = (SSLSocket) factory.createSocket(getHostName(), 443);
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
    		//TODO clean this or return it to how it was
    		//socketWriter.println("GET http://" + link + " HTTP/1.1");
            socketWriter.println("GET http://" + hostName + " HTTP/1.1");
            socketWriter.println("Host: " + hostName);
            // adding carriage return
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
			// writing params
			// may not need req type and pmid if i use content length
			String s = "requestType=reqBuild&pmid=ADMIN_LOGIN&emailAddress=chris.johnson%40siliconmtn.com&password=1040Disco%23&l=";
			//               ? before req
			String params = "requestType=reqBuild&pmid=ADMIN_LOGIN";
			params += "&emailAddress=" + PROPERTIES.getProperty("usernamme");
			params += "&password=" + PROPERTIES.getProperty("password") + "&l=";
			System.out.println("post - " + params + " len - " + s.length() + "  " + directory + "  " + hostName );
			// params len 102 encoded + 4  

	        socketWriter.println("POST " + directory + " HTTP/1.1");
	        socketWriter.println("Host: " + hostName);
	        socketWriter.println("content-type: application/x-www-form-urlencoded");
	        socketWriter.println("accept-encoding: gzip, deflate, br");
	        socketWriter.println("user-agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.96 Safari/537.36");
	        //socketWriter.println("Content-Length: 106"); // length of message needed
	        socketWriter.println("Cache-Control: no-cache");
	        // writing params
	        socketWriter.println(params);
	        socketWriter.println("");
	        // send request to page
	        socketWriter.flush();    
	        
	        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			System.out.println("IO exception - " + e);
			e.printStackTrace();
		}
    }
        
	/**
	 * Getting the host name of the current page.
	 * @return
	 */
    public String getHostName() {
		StringBuilder host = new StringBuilder();
		//removing protocol
		String newLink = link.replace("http://", "");
		newLink = newLink.replace("https://", "");
		System.out.println(" +++++ " + newLink);
		try {
			for (int i=0; i< newLink.length(); i++) {
				if (newLink.charAt(i) != '/') {
					host.append(newLink.charAt(i));
				} else break;
			}
			// checking for address
			if(newLink.length() > host.length()) {
				directory = newLink.replace(host.toString(), "");
			}
		} catch(NullPointerException e) {
			System.out.println("Error getting host name. Nullpointer Exception -" + e);
		}
		hostName = host.toString();
		return host.toString();
	}
    
    /**
     * Save the page including response to a file for the parser.
     */
    private void savePage() {
		file = new File(FILE_DIR + makeFileName());
		String line="";
		
		try ( BufferedWriter out = new BufferedWriter(new FileWriter(file)) ){
		
			while((line= socketReader.readLine()) != null) {
				//TODO delete println
				System.out.println(line);
				out.write(line);
				// breaking at and of html page because the reader would no stop
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
	 * Make a file name by replacing reserved characters to save in a directory.
	 * @return
	 */
	private String makeFileName() {
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
