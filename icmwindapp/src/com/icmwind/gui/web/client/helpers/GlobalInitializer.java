package com.icmwind.gui.web.client.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.icmwind.core.impl.EncoderImpl;
import org.icmwind.core.impl.RDFEncoderImpl;
import org.icmwind.util.ICMWindConfig;

public class GlobalInitializer {
	
	// CONSTANTS
	// Path to SYSTEM_DATA_STORE and its contents
	private final String DATA_STORE = "C:/Users/anme05/sdre_icmiwnd/icmwindapp/war/data store";
	
	public final String DATA_LOCATION = DATA_STORE + "/data/" ;
	public final String WIND_TURBINES_INFO_LOCATION = DATA_STORE + "/wind turbines/" ;
	public final String ONTOLOGIES_LOCATION = DATA_STORE + "/ontologies/" ;
	
	@SuppressWarnings("unused")
	private final String ONTOLOGY_PATH = DATA_STORE + "/ontologies/" + "";
	
	private final String WIND_TURBINES_SOURCE = DATA_STORE + "/wind turbines/" + "WTS.info";
	private String UPLOADED_FILE_PATH;
	
	private final static Map<String, String> mapWT2Source = new HashMap<String, String>();
	
	private static PrintWriter logWriter = null;
	
	private static Properties prop = new Properties();
	
	private static GlobalInitializer INSTANCE;
	
	private GlobalInitializer() {
		System.out.println("**GlobalIntializer() : GlobalInitializer Instance. Load WT Src file to get WT name and src path. ");
		
		try {
			prop.load(new FileInputStream(WIND_TURBINES_SOURCE));
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
		// Since the prop file has standard structure, no need to manipulate it
		// TODO when the rows are empty 
		for(Entry<Object, Object> entry : prop.entrySet()) {
			// DEBUG
			// System.out.println(entry.getKey().toString() +  "--" + entry.getValue().toString());
			mapWT2Source.put(entry.getKey().toString(), entry.getValue().toString());
		}
	}
	
	public static GlobalInitializer get() {
		if(INSTANCE == null)
			INSTANCE = new GlobalInitializer();
		return INSTANCE;
	}
	
	
	/**
	 *	Initialization Module. Initialize all the required Pre-Run Application System Settings.   
	 */
	public void init() {
		// initialise ICMWindConfig
		ICMWindConfig.init();
		
		// Set Log Writer for Encoding initialized from GI 
		ICMWindConfig.setEncodingLogWriter(GlobalInitializer.get().getLogWriter());
		
		// Initialise Ontology Processing
//		RDFEncoderImpl.getInstance().initOntologyProcess();
		EncoderImpl.getInstance().initOntologyProcess();
		
		// Set Path to store encoded triples
//		RDFEncoderImpl.getInstance().setEncodeStorage(GlobalInitializer.get().ONTOLOGIES_LOCATION);
		EncoderImpl.getInstance().setEncodeStorage(GlobalInitializer.get().ONTOLOGIES_LOCATION);
		
		
	}
	
	public List<String> listWindTurbines() {
		System.out.println("**GlobalIntializer.listWT() : Lists Wind Turbine names.");
		
		List<String> listWT = new ArrayList<String>();
		
		for(Object wt : prop.keySet()) {
			listWT.add(wt.toString());
		}
				
		System.out.println(listWT.toString());
		
		return listWT;
	}
	
	public String getSourceForWindTurbine(String wt_name) {
		System.out.println("**GlobalIntializer.getSourceForWindTurbine(name) : Retruns WT src path.");
		
		System.out.println(wt_name + " has path " + mapWT2Source.get(wt_name));
		return mapWT2Source.get(wt_name);
	}

	public String getUploadedFilePath() {
		System.out.println("**GlobalIntializer.getUploadedFilePath() : Retruns uplaod file path.");
		
		if(UPLOADED_FILE_PATH.equals(null)) {
			return "";
		} else {
			return UPLOADED_FILE_PATH;
		}
	}

	public void setUploadedFilePath(String uploadfile_path) {
		UPLOADED_FILE_PATH = uploadfile_path;
	}
	
	public PrintWriter getLogWriter() {
		Date date= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		String timeStamp = sdf.format(date).substring(0, 10).replaceAll("\\.", "-") 
							+ "_" + sdf.format(date).substring(11).replaceAll(":", "-");
		
		try {
			logWriter = new PrintWriter("log-" + timeStamp + ".txt");
			return logWriter;
		} catch (FileNotFoundException e) {
			System.out.println("\n**ERROR GI.getLogWriter() : Error in creating Log file.");
			e.printStackTrace();
			return null;
		}
	}
	

}
