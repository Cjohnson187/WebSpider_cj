package com.chris.helper;

import java.io.File;

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
	 * @param key
	 * @param file
	 */
	public Pair(String key, File file) {
		this.response = key;
		this.file = file;
	}
	
	/**
	 * Get String.
	 * @return
	 */
	public String getResponse() {
		return this.response;
	}
	
	/**
	 * Get File.
	 * @return
	 */
	public File getFile() {
		return this.file;
	}
}
