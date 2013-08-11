/**
 * ICMWindSetup class to set up all the global constants which will be used in the system.
 * Pass the path of ICMWindConifg.config and extracts all the necessary paths from the file.
 * 
 * It has static methods.
 * 
 */
package org.icmwind.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author anme05
 * 
 */
public final class ICMWindConfig {

	private static String DATA_FILE_PATH;
	private static String CORE_ONTOLOGY_PATH;
	private static String GSE_CONFIG_PATH;
	private static String GER2ENG_DICT_PATH;
	
	private static String RESOURCE_FOLDER_PATH = "C:/Users/anme05/sdre_icmiwnd/icmwind/res/";
		
	private static boolean isInitialised = false;

	private static String PROPERTY2SENSOR_CONFIG_FILE_PATH = "conf/property2sensor.info";
	private static String SENSOR2SYSTEM_CONFIG_FILE_PATH = "conf/sensor2system.info";
	
	private static final String CONFIG_FILE_PATH = "conf/ICMWindConfig.config";

	private static final String ONTOLOGY_URI = "http://www.icmwind.com/icmwindontology.owl";

	private static Properties icmwindconfig = new Properties();
	
	private static PrintWriter writer = null;

	private ICMWindConfig() {
	}

	/**
	 * Load the configuration file ICMWindConfig.config and extract all the
	 * necessary path values.
	 * 
	 */
	public static void init() {
		System.out.println("**ICMWindConfig.init() : Initializing. Get all the path variables value from config file.");
		
		try {
			icmwindconfig.load(ICMWindConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
			isInitialised = true;
			DATA_FILE_PATH = icmwindconfig.getProperty("DATA_FILE",
					"! Data File not found.");
			CORE_ONTOLOGY_PATH = icmwindconfig.getProperty("CORE_ONTOLOGY",
					"! Ontology File not found.");
			GSE_CONFIG_PATH = icmwindconfig.getProperty("GSE_CONFIG",
					"! GSE Configuration File not found.");
			GER2ENG_DICT_PATH = icmwindconfig.getProperty("GER2ENG_DICTIONARY",
					"! Ger-Eng Dictionary not found.");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check whether ICMWindSetup has been initialized and all the path values
	 * being parsed?
	 * 
	 * @return whether ICMWindSetup is initialized ?
	 */
	public static boolean isInitialised() {
		return isInitialised;
	}

	/**
	 * @return Path of Data File (*.csv)
	 */
	public static String getDataFilePath() {
		return DATA_FILE_PATH;
	}

	/**
	 * @return Path of Core Ontology (*.owl)
	 */
	public static String getCoreOntologyPath() {
		return CORE_ONTOLOGY_PATH;
	}

	/**
	 * @return Path of GSE Configuration File (*.isrealomsconfig)
	 */
	public static String getGSEConfigPath() {
		return GSE_CONFIG_PATH;
	}

	/**
	 * @return Path of Ger-Eng Dictionary (*.properties)
	 */
	public static String getDictionaryPath() {
		return GER2ENG_DICT_PATH;
	}

	/**
	 * @return
	 */
	public static String getResourceFolderPath() {
		return RESOURCE_FOLDER_PATH;
	}

	/**
	 * @return the pROPERTY2STRING_CONFIG_FILE_PATH
	 */
	public static String getProp2SenConfigFilePath() {
		return PROPERTY2SENSOR_CONFIG_FILE_PATH;
	}

	/**
	 * @return the sENSOR2SYSTEM_CONFIG_FILE_PATH
	 */
	public static String getSen2SysConfigFilePath() {
		return SENSOR2SYSTEM_CONFIG_FILE_PATH;
	}

	public static String getOntologyURI() {
		return ONTOLOGY_URI;
	}
	
	public static void setEncodingLogWriter(PrintWriter pw) {
		writer = pw;
	}
	
	public static PrintWriter getEncodingLogWriter() {
		if(writer!=null) {
			return writer;
		} else {
			System.out.println("\n**ERROR - ICMWindConfig.getEncodingLogWriter() : Log Writer not initialised.");
			return null;
		}
	}
}
