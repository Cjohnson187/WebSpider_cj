package com.chris.helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	private static Properties prop;
	private static final String PROPERTIES_LOCATION="config/config.properties";
	public PropertiesLoader() { }
	
	public Properties readPropFile() {
	    Properties prop = null;
	    try (FileInputStream fis = new FileInputStream(PROPERTIES_LOCATION)){
	        prop = new Properties();
	        prop.load(fis);
	    } catch(FileNotFoundException fnfe) {
	         fnfe.printStackTrace();
	    } catch(IOException ioe) {
	         ioe.printStackTrace();
	    }
	    return prop;
	}
	
}
