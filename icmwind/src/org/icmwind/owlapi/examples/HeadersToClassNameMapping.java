package org.icmwind.owlapi.examples;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.csvreader.CsvReader;

public class HeadersToClassNameMapping {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		CsvReader reader = new CsvReader(new FileReader("E:/ICM-Wind/Code/icmwind/data_files/TestData.csv"), ';');
		reader.readHeaders();
		String [] headers = reader.getHeaders();
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("E:/ICM-Wind/Code/icmwind/config_files/classmapping.properties"));
		
		System.out.println("*****CSV Header --> Ontology Class******");
		
		for(String head : headers) {
			if( (prop.getProperty(head)!=null) && (! prop.getProperty(head).equals("NaN")) ) {
					System.out.println(head + " --> " + prop.getProperty(head));
			}
		}
		
		reader.close();

	}

}
