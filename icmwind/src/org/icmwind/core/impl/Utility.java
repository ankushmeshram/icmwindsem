package org.icmwind.core.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Utility {
	private static final Utility INSTANCE = new Utility();
	
	private static Properties prop = null;
	private static Properties semConfig = new Properties();

	// To-Do: change path to project relative path
	private static final String SEM_CONFIG_PATH = "C:\\ICMWind\\eclipse_workspace\\icmwind\\config_files\\semconfig.properties";

	
	private Utility() { }
	
	public static void init() {
		try {
			// To-Do: load from realtive path
			semConfig.load(new FileInputStream(SEM_CONFIG_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		return INSTANCE;
	}
	
	// return Ontology file path
	public static String getOntologyPath() { 
		return semConfig.getProperty("CORE_ONTOLOGY");
	}
	
	
	// Initialize Dictionary with path of the dictionary file (*.properties)
	public static void initDictionary(String propFilePath) throws FileNotFoundException, IOException {
		prop = new Properties();
		prop.load(new FileInputStream(propFilePath));
	}
	
	/*
	 * If Dicitionary not initialised via initDictionary() print error.
	 * If word is found return its english ransaltion 
	 * Else return "NA" - Not Available 
	 * 
	 */
	public static String searchInDict(String query){
		if(prop == null) {
			System.out.println("Error: Dicitionary not initialised.");
			return null;
		}
		
		if(prop.getProperty(query) != null ) {
			return prop.getProperty(query);
		} else {
			return "NA";
		}
		
	}
}
