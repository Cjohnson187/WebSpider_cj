package com.chris.spider;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


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
		// TODO Auto-generated method stub

	}
	
	public static void sendCoookie(Socket socket) throws IOException {
		PrintWriter writer = new PrintWriter(socket.getOutputStream());
		writer.println("HTTP/1.0 200 Ok");
		writer.println("Set-Cookie key=value Max-Age=8640");
		writer.println("Content-Type text/html");
		writer.println(); //important
		//send your body here
		writer.flush();//send message
	}

}
