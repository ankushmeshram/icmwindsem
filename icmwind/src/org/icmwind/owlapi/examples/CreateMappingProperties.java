package org.icmwind.owlapi.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class CreateMappingProperties {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, Exception {
		
		Properties prop = new Properties();
		
		prop.load(new FileInputStream("E:/ICM-Wind/Code/icmwind/config_files/classmapping.properties"));
		
//		HashBiMap<String, String> testBiMap = HashBiMap.create();
//		
//		testBiMap.put("CS Drive", "CS_Drive");
//		testBiMap.put("P Luft", "P_Air");
//		
//		Map<String, String> reversedTestBiMap = testBiMap.inverse();
//		
//		System.out.println(testBiMap.get("CS Drive") + "\t" + reversedTestBiMap.get("CS_Drive"));
		
		System.out.println(prop.getProperty("Zeit"));

	}

}
