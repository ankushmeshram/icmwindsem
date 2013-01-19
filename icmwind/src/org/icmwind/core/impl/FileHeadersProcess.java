package org.icmwind.core.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.lucene.queryParser.ParseException;

public class FileHeadersProcess {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
		// Get german-english dictionary
		Properties prop = new Properties();
		prop.load(new FileInputStream("C:\\ICMWind\\eclipse_workspace\\icmwind\\ger2engDictionary.properties"));
		
		Set<Object> keys = prop.keySet();
		Set<String> germanWords = new HashSet<String>();
		for(Object key : keys) {
			System.out.println(key.toString());
			germanWords.add(key.toString());
		}
		
		// Get Header names
		FileProcessImpl fpi = FileProcessImpl.getInstance();
		fpi.openFile("C:\\ICMWind\\eclipse_workspace\\icmwind\\data_files\\TestData.csv");
		
		List<String> headerNames = fpi.getHeadNamesList();
		
		/*
		Set<String> headersSet = new HashSet<String>(Arrays.asList(headerNames));
		
		Search search = new Search();
		search.intialiseIndexing();
		search.indexItems("headers", headersSet);
		search.closeIndexing();
		search.initialiseSearching(10);
		System.out.println(search.wildCardSearch("headers", "Temp").toString());
		search.closeSearching();
		
		*/
		
//		/*
		
		// Search for header names in Dictionary
		Search search = new Search();
		search.intialiseIndexing();
		search.indexItems("german", germanWords);
		search.closeIndexing();
		search.initialiseSearching(10);
		
		for(String header : headerNames) {
			Object[] result = (Object[]) search.wildCardSearch("german", header).toArray();
			String germanWord = result.length>0?result[0].toString():"";
			System.out.println(germanWord);
			if(!germanWord.equals("NA")) {
				System.out.println(germanWord + " = " + prop.getProperty(germanWord));
				System.out.println(header + "-->" + header.replace(germanWord, prop.getProperty(germanWord)).toString());
			} else if (header.contains("%")) {
				System.out.println(header + "-->" + header.replace("%","").toString());
			} else if (header.contains("  ")) {
				System.out.println( header + "-->" + header.replace("  ","").toString());
			} else if (header.contains("-")) {
				System.out.println(header + "-->" + header.replace("-"," ").toString());
			}
		}
		search.closeSearching();
		
//		*/
	}

}
