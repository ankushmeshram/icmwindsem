package org.icmwind.owlapi.examples;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import com.csvreader.CsvReader;

public class ReadFromCSV {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			CsvReader reader = new CsvReader(new FileReader("E:/ICM-Wind/Code/icmwind/data_files/TestData.csv"), ';');
			
			reader.readHeaders();
			
			while(reader.readRecord()) {
				
				String zeit = reader.get("Zeit");
				String cs_drive = reader.get("CS Drive");
				String mcs_a = reader.get("MCS A");
				
				System.out.println("Zeit: " + zeit + " CS_DRIVE: " + cs_drive + " MCS_: " + mcs_a);
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
