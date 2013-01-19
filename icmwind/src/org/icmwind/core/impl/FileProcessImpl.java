package org.icmwind.core.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.icmwind.core.FileProcess;

import com.csvreader.CsvReader;

public class FileProcessImpl implements FileProcess {

//	private String filepath = null;
	private static final FileProcessImpl INSTANCE =  new FileProcessImpl();
	private char separator = ';';
	private CsvReader reader = null;
	boolean isFileOpened = false;
	private List<String> headNamesList = new ArrayList<String>();
	private List<String> normHeadNamesList = new ArrayList<String>();
	private Map<String, String> normHeadNamesToHeadNames =  new HashMap<String,String>();

	private FileProcessImpl() {}
	
	public static FileProcessImpl getInstance() { return INSTANCE; }
	
	@Override
	public void setSeparator(char separator) { this.separator = separator; }
	
	
	private boolean setFile(String filepath) {
		try {
			this.reader = new CsvReader(new FileReader(new File(filepath)), separator);
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public boolean openFile(String path) {
		if(this.setFile(path)) {
			try {
				this.reader.readHeaders();
				this.headNamesList = Arrays.asList(this.reader.getHeaders());
				isFileOpened = true;
				this.normalize();
				return isFileOpened;
			} catch (IOException e) {
				e.printStackTrace();
				isFileOpened = false;
				return isFileOpened;
			}
		} else {
			isFileOpened = false;
			return isFileOpened;
		}
	}	
	
	
	@Override
	public void closeFile() { this.reader.close(); }

	
	// Checks if records exist further or not
	@Override
	public boolean existsRecord() { 
		try {
			return reader.readRecord();
		} catch (IOException e) {
			e.printStackTrace();
			return false; 
		}
	}
	
	
	// Returns value for the given Header for current row.
	@Override
	public String readRecord(String headerName) {
		try {
			return reader.get(headerName);
		} catch (IOException e) {
			e.printStackTrace();
			return "NA";
		}
	}

	
	@Override
	public List<String> getHeadNamesList() {
		if(isFileOpened)
			return headNamesList;
		else
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
	}
	
	
	@Override
	public List<String> getNormHeadNamesList() {
		if(isFileOpened)
			return normHeadNamesList;
		else
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
	}
	

	@Override
	public String getHeadNameFor(String normHeadName) {
		if(isFileOpened)
			if(normHeadNamesList.contains(normHeadName))
				return normHeadNamesToHeadNames.get(normHeadName);
			else
				return "normHeadName not found in system.";
		else
			return "ERROR: File not found.";
	}
	
	
	/*
	 * Normalize HeaderNames to NormalizedHeaderNames
	 *  	Remove unwanted characters
	 *  	Change everything to lower case
	 *  		eg: text.replaceAll("\\s{2,}+|-", " ").replaceAll("%", "").toLowerCase()
	 *  	Translate german2english
	 *  	Map NormalizedHeaderNames2HeaderNames
	 */ 
	private void normalize() {

		// Initialise german-english dictionary by providing path of "ger2engDictionary.properties"
		try {
			Utility.initDictionary("C:\\ICMWind\\eclipse_workspace\\icmwind\\ger2engDictionary.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String normhead = null;
		
		for(String head : headNamesList) {
			// clean text to remove unwanted chars
			normhead = cleanText(head).toLowerCase();
			
			// translate text ,if any
			try {
				normhead = translateText(normhead);
			} catch ( IOException e) {
				e.printStackTrace();
			}
			
			// add normalised head to normalised headers list.
			normHeadNamesList.add(normhead);
			
			// map for normhead (Normalised Header Names) to head (Headers Names)
			normHeadNamesToHeadNames.put(normhead, head);
		}




	}

	
	// remove unwanted chars like "-" and >=2 whitespaces with " "
	// remove unwanted chars like "%" with ""
	private String cleanText(String text) { return text.replaceAll("\\s{2,}+|-", " ").replaceAll("%", ""); }
	
	
	// split text and find their english translations, if any
	private String translateText(String query) throws FileNotFoundException, IOException {
		String[] splitText = query.split(" ");
		StringBuilder result = new StringBuilder();
		String dictResult = null;

		// if more than one word in the query
		if(splitText.length > 1) {
			for(int i = 0; i < splitText.length; i++) {
				dictResult = Utility.searchInDict(splitText[i]);
				
				// if translated word found
				if(!dictResult.equals("NA"))
					result.append(dictResult);
				else
					result.append(splitText[i]);
				
				result.append(" ");
			}
		} else {
			dictResult = Utility.searchInDict(query);
									
			// if translated word found
			if(!dictResult.equals("NA"))
				result.append(dictResult);
			else
				result.append(query);
		}
		
		// return result
		return result.toString().trim();
	}


}
