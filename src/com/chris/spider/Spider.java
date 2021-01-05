package com.chris.spider;

import java.net.MalformedURLException; 

import java.net.Socket; 

import java.net.URL; // not using these
import java.net.URLConnection; // not using these

import java.io.*; // choose specific imports



/****************************************************************************
 * <b>Title</b>: Spider.java
 * <b>Project</b>: WebSpider
 * <b>Description: </b> This is the main runner class for my web crawler,
 * It will use a connection manager, parser, and link manager. The Connection
 * Manager will make and handle responses to and from the web site. The 
 * Parser will make a buffer to read from the page and write to a file.
 * The link manager will accept links/cookies from the parser and connection 
 * manager. The process will continue until the link manager is out of new 
 * links.
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
	

	
	public static void main(String... args) {
		
		// make request to page   -- connect manager
		// handle response
		//  	save cookie in hashmap key
		//		save webpages in values for cookie
		// make buffer for reading page 
		// make buffer for writing page
		// ~~ while the page is not null
		// 		~ read char /write to file
		// 		* if <a href> 
		// 			save link / write to file
		// check for new links in manager
		// 		* yes- repeat
		// 		* no- end

	}
	
	
	
	
	
	
	
	
	/**
	 * I was just using this mess to test URL.
	 * @param args
	 * @throws IOException
	 */
//	public static void main(String[] args) throws IOException {
//		String cur = "homeSMT";
//		OutputStream out = new FileOutputStream("files/"+cur+ ".txt"); 
//		try {
//			URL page = new URL("https://www.siliconmtn.com/");
//			URLConnection ul = page.openConnection();
//			BufferedReader in1  = new BufferedReader(new InputStreamReader(ul.getInputStream()));
//			String inputLine;
//			byte[] line;
//			while ((inputLine = in1.readLine()) !=null) {
//				line = inputLine.getBytes();
//				out.write(line);
//				System.out.println(inputLine);
//			}
//		
//		} catch (MalformedURLException e) {
//			System.out.println("Malformed url e- " + e);
//			e.printStackTrace();
//		}
//		
//		
//	}
	
}
