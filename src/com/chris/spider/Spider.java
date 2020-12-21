package com.chris.spider;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import java.io.*;



/****************************************************************************
 * <b>Title</b>: Spider.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> Web spider class to use for web crawling.
 * <b>Copyright:</b> Copyright (c) 2020
 * <b>Company:</b> Silicon Mountain Technologies
 * 
 * @author Chris Johnson
 * @version 1.0
 * @since Dec 21, 2020
 * @updates:
 ****************************************************************************/
public class Spider {
	private Socket socket;
	private String socketAddress;
	
	
	/**
	 * I was just using this mess to test URL.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String cur = "homeSMT";
		OutputStream out = new FileOutputStream("files/"+cur+ ".txt"); 
		try {
			URL page = new URL("https://www.siliconmtn.com/");
			URLConnection ul = page.openConnection();
			BufferedReader in1  = new BufferedReader(new InputStreamReader(ul.getInputStream()));
			String inputLine;
			byte[] line;
			while ((inputLine = in1.readLine()) !=null) {
				line = inputLine.getBytes();
				out.write(line);
				System.out.println(inputLine);
			}
		
		} catch (MalformedURLException e) {
			System.out.println("Malformed url e- " + e);
			e.printStackTrace();
		}
		
		
	}
	
}
