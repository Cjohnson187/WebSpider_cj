package com.chris.testJunk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import java.net.InetAddress;



/****************************************************************************
 * <b>Title</b>: Tester.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> Im just using this class to test stuff
 * <b>Copyright:</b> Copyright (c) 2021
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Jan 5, 2021
 * @updates:
 ****************************************************************************/
public class Tester {

	/**
	 * Just a class to test stuff before implementing
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try {
			InetAddress site = InetAddress.getByName("www.siliconmtn.com");
			System.out.println(site.getHostAddress());
			SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket)factory.createSocket("www.siliconmtn.com", 443);
			//Socket socket = new Socket(site.getHostAddress(), 443);
			
			sendCookie(socket);
		} catch (Exception e) {
			System.out.println("Exception - " + e);
		}
		

	}
	
	public static void sendCookie(Socket socket) throws IOException {
		
		// write to page
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		
		// input stream to read response
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//
		//writer.write ("GET /index.html HTTP/1.0\n\n");
		
		
		
		
		// use uri to get through a proxy
		writer.println("POST http://www.siliconmtn.com/admintool HTTP/1.1");
		writer.println("Host: www.siliconmtn.com");
		//writer.println("GET /index.html HTTP/1.0");
		//writer.println("Host: www.xyz.com");
		//writer.println("Connection: Keep-Alive");
		//writer.println("Accept: text/html, */*");
		//writer.println("Accept-Language: us-en, fr, cn");
        writer.println();
        writer.flush();

		//writer.println(); //important
		//send your body here
		//writer.flush();//send message
		String line = "";
		while((line = reader.readLine()) != null) {
			System.out.println(line.toString());
		}
		reader.close();
		writer.close();
		System.out.println("outy 5000");
		
		
	}

}
