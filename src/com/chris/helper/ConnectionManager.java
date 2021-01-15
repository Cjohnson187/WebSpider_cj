package com.chris.helper;

import java.io.BufferedReader;
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
    private static SSLSocket socket;
    private InetAddress address;
    private static PrintWriter socketWriter;
    private static BufferedReader socketReader;

    // input buffer
    // output buffer

    /**
     * Empty constructor since the URL will most likely be changing a few times.
     */
    public ConnectionManager() {
    }

    /**
     * Make connection to page and build writer / reader
     */
    public void connectSocket(String link) {
        SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
            // Using InetAdress to make socket because its safer than using my parsed strings
            address = InetAddress.getByName(link);
            socket = (SSLSocket) factory.createSocket(address.getHostName(), 443);

            // Creating reader and writer.
            socketWriter = new PrintWriter(socket.getOutputStream());
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
    public BufferedReader sendGet(){
        // basic post to get  HTML
        socketWriter.println("GET http://" + address.getHostName() + " HTTP/1.1");
        socketWriter.println("Host: " + address.getHostName());

        // adding carraige return and sending
        socketWriter.println();
        socketWriter.flush();
        // send request to page
        
        return socketReader;
    }

    /**
     * Making post request for login on secure site
     */
    public BufferedReader sendPost(){
        socketWriter.println("POST http://" + address.getHostName() + " HTTP/1.1");
        socketWriter.println("Host: " + address.getHostName());
        
        // if there is an existing jsessionID send it as well 
        if (checkForCookie()) {
        	//socket.writer
        }

        // adding carriage return and sending
        socketWriter.println();
        socketWriter.flush();
        // send request to page
        
        return socketReader;
    }
    
    

    /**
     * Method to read header
     * @param siteResponse
     */
    public static void readResponse(String siteResponse){
        // read response maybe
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
    public  void saveCookie(String newCookie) {
        this.cookie = newCookie;
    }
    
    /**
     * Getting Cookie for fiel writer.
     * @return
     */
    private String getCookie() {
        return cookie;
    }

    // Close readers G*$#@M%&*!!!!!


}
