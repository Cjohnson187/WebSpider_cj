package com.chris.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    private static SSLSocket socket;
    private InetAddress address;
    private static PrintWriter socketWriter;
    private static BufferedReader socketReader;
    private Document page;
    
    private final String fileDir = "files/";
	private String fileName;
    

    /**
     * Empty constructor since the URL will most likely be changing a few times.
     */
    public ConnectionManager(String link) {
    	this.link = link;
    	System.out.println(link + "$$$$$$$$$$$$$$$$$$$$$$$$$$4");
    }
    
    
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
            address = InetAddress.getByName(link);
            System.out.println(address.getHostName());
            socket = (SSLSocket) factory.createSocket(address.getHostName(), 443);

            // Creating reader and writer.
            socketWriter = new PrintWriter(socket.getOutputStream());
        } catch (UnknownHostException e) {
            System.out.println("I pitty the fool who cant recognize a host, exception- " + e);
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
	public String GetBaseURL() {
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
	
	public void savePage() {
		File file = new File(fileDir+ getPageName());
		
		String line="";
		try (BufferedWriter out = new BufferedWriter(new FileWriter(file));){
			socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			
			while((line= socketReader.readLine()) != null) {
				out.write(line);
				System.out.println(line);
			}
			
			socketReader.close();
			socketWriter.close();
			System.out.println("*************************************************");
			
		} catch (IOException e) {
			System.out.println("Error reading from socket input stream, exception - " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to make an input stream for the parser.
	 * @return
	 * @throws IOException
	 */
	public Document getDoc(String uri) {
			System.out.println("Making a document");
			// making a page from the input stream to parse
			String line = "";
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println(uri);
				page = Jsoup.parse(socket.getInputStream(), "ISO-8859-1", "https://"+uri);
				System.out.println("jsoup is taking forever");
				while((line = reader.readLine()) != null) {
					System.out.println("&&&&& a line printed &&&&&");
					System.out.println(line.toString());
				}
			} catch (IOException e) {
				System.out.println("exception e- " +e);
				e.printStackTrace();
			}
			
		return page;
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
