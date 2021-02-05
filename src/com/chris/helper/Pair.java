package com.chris.helper;

import java.io.File;

/****************************************************************************
 * <b>Title</b>: Pair.java <b>Project</b>: WebSpider
 * <b>Description: </b> The pair is used to store the response in a string 
 * and the corresponding file. 
 * <b>Copyright:</b> Copyright (c) 2021 <b>Company:</b> Silicon Mountain
 * Technologies
 * 
 * @author Chris Johnson
 * @version 2.0
 * @since Feb 04, 2021
 * @updates:
 ****************************************************************************/

public class Pair {
	
	private String response;
	private File file;
	
	/**
	 * Empty constructor.
	 */
	public Pair() {
	}
	
	/**
	 * Constructor for response and file pair.
	 * @param response
	 * @param file
	 */
	public Pair(String response, File file) {
		this.response = response;
		this.file = file;
	}
	
	/**
	 * Get response String.
	 * @return
	 */
	public String getResponse() {
		return this.response;
	}
	
	/**
	 * Get HTML file from request File.
	 * @return
	 */
	public File getFile() {
		return this.file;
	}
}
