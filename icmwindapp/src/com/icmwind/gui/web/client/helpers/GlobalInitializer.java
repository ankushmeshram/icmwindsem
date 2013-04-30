package com.icmwind.gui.web.client.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class GlobalInitializer {
	
	// CONSTANTS
	// Path to SYSTEM_DATA_STORE and its contents
	private final String DATA_STORE = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store";
	
	public final String DATA_LOCATION = DATA_STORE + "/data/" ;
	public final String WIND_TURBINES_INFO_LOCATION = DATA_STORE + "/wind turbines/" ;
	public final String ONTOLOGIES_LOCATION = DATA_STORE + "/ontologies/" ;
	
	@SuppressWarnings("unused")
	private final String ONTOLOGY_PATH = DATA_STORE + "/ontologies/" + "";
	
	private final String WIND_TURBINES_SOURCE = DATA_STORE + "/wind turbines/" + "WTS.info";
	private String UPLOADED_FILE_PATH;
	
	private final static Map<String, String> mapWT2Source = new HashMap<String, String>();
	
	private static Properties prop = new Properties();
	
	private static GlobalInitializer INSTANCE;
	
	private GlobalInitializer() {
		
		try {
			prop.load(new FileInputStream(WIND_TURBINES_SOURCE));
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
		// Since the prop file has standard structure, no need to manipulate it
		// TODO when the rows are empty 
		for(Entry<Object, Object> entry : prop.entrySet()) {
			System.out.println("GlobalIntializer()..");
			System.out.println(entry.getKey().toString() +  "--" + entry.getValue().toString());
			mapWT2Source.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}
	
	public static GlobalInitializer get() {
		if(INSTANCE == null)
			INSTANCE = new GlobalInitializer();
		return INSTANCE;
	}
	
	public List<String> listWindTurbines() {
		List<String> listWT = new ArrayList<String>();
		
		for(Object wt : prop.keySet()) {
			listWT.add(wt.toString());
		}
		
		System.out.println("GlobalIntializer.listWT()");
		System.out.println(listWT.toString());
		
		return listWT;
	}
	
	public String getSourceForWindTurbine(String wt_name) {
		System.out.println(wt_name + " has path " + mapWT2Source.get(wt_name));
		return mapWT2Source.get(wt_name);
	}

	public String getUploadedFilePath() {
		if(UPLOADED_FILE_PATH.equals(null)) {
			return "";
		} else {
			return UPLOADED_FILE_PATH;
		}
	}

	public void setUploadedFilePath(String uploadfile_path) {
		UPLOADED_FILE_PATH = uploadfile_path;
	}
	
	
	

}
