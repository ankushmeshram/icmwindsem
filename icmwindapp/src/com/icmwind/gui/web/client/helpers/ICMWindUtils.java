package com.icmwind.gui.web.client.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ICMWindUtils {
	// Directory where all the files will be saved
	static String DATA_DIRECTORY = "C:\\ICMWIND_DATA\\data"; 	//TODO Set it via installation.
	
	// Name of Selection File Name
	static String SELECTION_FILENAME = "icmwindmapping";
	
	// Path of Selection File
	static String SELECTION_FILEPATH = DATA_DIRECTORY + File.separator + SELECTION_FILENAME + ".selections";
	
	// Detects whether *.selection file exists
	static boolean hasSelectionFile = false;
	
	// Mapping from selection file
	static Properties selectionsFile = new Properties();
	
	public static void init() {

		// CHECK ONE
		//checks DATA_DIRECTORY exists, if not creates a folder.
		File datadir = new File(DATA_DIRECTORY);
		
		if(!datadir.exists())
			datadir.mkdirs();
			
		// CHECK TWO
		// check whether *.selection file exists in DATA_DIRECTORY
		FindExtension fe = new FindExtension(DATA_DIRECTORY, "*.selections");
		if(fe.existsExtension()) {
			hasSelectionFile = true;

			// load Selection File
			try {
				selectionsFile.load(new FileInputStream(SELECTION_FILEPATH));
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	
	/**
	 * @return the uPLOAD_DIRECTORY
	 */
	public static String getDataDirectory() {
		return DATA_DIRECTORY;
	}
	
	/**
	 * @return the hasSelectionFile
	 */
	public static boolean hasSelectionFile() {
		return hasSelectionFile;
	}

	/**
	 * @return the selectionsFile
	 */
	public static Properties getSelectionsFile() {
		return selectionsFile;
	}
	
	/**
	 * @return the SELECTION_FILEPATH
	 */
	public static String getSelectionFilePath() {
		return SELECTION_FILEPATH;
	}
	

}
