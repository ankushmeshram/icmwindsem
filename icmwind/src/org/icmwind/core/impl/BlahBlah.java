package org.icmwind.core.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;
import org.icmwind.core.FileProcess;
import org.icmwind.core.OntologyProcess;


public class BlahBlah {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ParseException 
	 */

	
	

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		
//		CsvReader read = new CsvReader(new FileReader("C:\\ICMWind\\eclipse_workspace\\icmwind\\data_files\\TestData.csv"), ';');
//		read.readHeaders();
//		System.out.println(read.getHeaders().toString());
		
		/*
		
		FileProcess fileProc =  FileProcessImpl.getInstance();
		fileProc.openFile("C:\\ICMWind\\eclipse_workspace\\icmwind\\data_files\\TestData.csv");
//		System.out.println(fileProc.getHeadNamesList().toString());
		System.out.println(fileProc.getNormHeadNamesList().toString());
		
		OntologyProcess ontoProc = OntologyProcessImpl.getInstance();
		ontoProc.openFile("C:\\ICMWind\\eclipse_workspace\\icmwind\\gse_config\\ontologies\\WindTurbineOnto.owl");
//		System.out.println(ontoProc.getClassNamesList().toString());
		System.out.println(ontoProc.getNormClassNamesList().toString());
		
		// Search for header names in Dictionary
		Search search = new Search();
		search.intialiseIndexing();
		search.indexItems("class", ontoProc.getNormClassNamesList());
		search.closeIndexing();
		search.initialiseSearching(10);
		
		for(String header : fileProc.getNormHeadNamesList()) {
			List<String> result = search.wildCardSearch("class", header);

			System.out.println("\n**Head: " + fileProc.getHeadNameFor(header) + "\nClass: ");
			for(String res : result)
				System.out.println("\t" + ontoProc.getClassNameFor(res));
			
		}
		*/
		
		System.out.println(BlahBlah.class.getResourceAsStream("./semconfig.properties"));
		
//		String[] a = {"A","B"};
//		System.out.println(a.length);
////		String text = "A B D B J";
//		String text = "A";
////		System.out.println(text.length());
//		String [] res = text.split(" ");
//		System.out.println(res.length);
//		System.out.println(res.length);
//		StringBuilder r =  new StringBuilder();
//		for(int i = 0; i < res.length; i++) {
//			if(res[i].equals("B"))
//				r.append("C");
//			else
//				r.append(res[i]);
//			
//			r.append(" ");
//	
//		}
//		
//		System.out.println(text.length());
//		System.out.println(r.length());
//		System.out.println(r.toString().trim());
//		System.out.println(r.toString().trim().length());
//		
	

	}

}
