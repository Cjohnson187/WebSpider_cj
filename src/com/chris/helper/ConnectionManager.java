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
 * make and handle requests/cookies from the current page given by the spider.
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
    private static PrintWriter socketWriter;
    private final String fileDir = "files/";

    private static SSLSocket socket;
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
    public String getPage() {
    	connectSocket();
    	sendGet();
    	savePage();
    	return getPageName();
    }

    /**
     * Make connection to page and build writer / reader
     */
    private void connectSocket() {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
            // Using InetAdress to make socket because its safer than using my parsed strings
        	System.out.println("link = " +link);
            address = InetAddress.getByName(link);
            System.out.println("address hostname " + address.getHostName());
            socket = (SSLSocket) factory.createSocket(address.getHostName(), 443);

            // Creating reader and writer.
            socketWriter = new PrintWriter(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.out.println("Could not recognize host, exception- " + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception = " + e);
            e.printStackTrace();
        }
    }

    /**
     * Making get request for the current page
     */
    private void sendGet(){
        // basic post to get  HTML
        socketWriter.println("GET http://" + address.getHostName() + " HTTP/1.1");
        socketWriter.println("Host: " + address.getHostName());

        // adding carriage return and sending
        socketWriter.println();
        // send request
        socketWriter.flush();
        
    }
    
    /**
     * Making post request for login on secure site
     */
    private void sendPost(){
        socketWriter.println("POST http://" + address.getHostName() + " HTTP/1.1");
        socketWriter.println("Host: " + address.getHostName());
        
        // adding carriage return and sending
        socketWriter.println();
        // send request to page
        socketWriter.flush();     
    }
    
	/**
	 * Getting the base url of the current page.
	 * @return
	 */
    private String GetBaseURL() {
		StringBuilder base = new StringBuilder();
		String hostName="";
		try {
			hostName = address.getHostName();
			for (int i=0; i< hostName.length(); i++) {
				if (hostName.charAt(i) != '/') {
					base.append(hostName.charAt(i));
				} else break;
			}
			
		} catch(NullPointerException e) {
			System.out.println("Error getting base url. Nullpointer Exception -" + e);
		}
		
		return hostName;
	}
	
	private void savePage() {
		File file = new File(fileDir+ getPageName());
		
		String line="";
		try (BufferedWriter out = new BufferedWriter(new FileWriter(file));
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				){
			
			while((line= socketReader.readLine()) != null) {
				out.write(line);
				if(line.contains("</html>")) {
					break;
				}
			}
			socketReader.close();
			socketWriter.close();
			
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
	
	private String getPageName() {
		return address.getHostName().replaceAll("/", "_").replaceAll(":", "-");
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
