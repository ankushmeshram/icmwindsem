package org.icmwind.core;

import java.util.List;

/**
 * @author ANKUSH
 *	
 * Interface defining the functionalities:
 * 	a. Open File from filePath / Open CSVFile from CSV File 
 *  b. Process to get HeaderNames List
 *  c. Normalize HeaderNames to NormalizedHeaderNames
 *  	Remove unwanted characters
 *  	Change everything to lower case
 *  	Translate german2english
 *  	Map NormalizedHeaderNames2HeaderNames 
 *  d. Get NormalizedHeaderNames
 *  e. Get record for given column / (Get record for given column at given row) 
 *
 */

public interface FileProcess {
	
	public void setSeparator(char separator);
	
	public boolean openFile(String filepath);
	
	public void closeFile();
	
	// Returns header names list(HNL)
	public List<String> getHeadNamesList();
	
	// Returns normalised header names list(N-HNL) for later comparison
	public List<String> getNormHeadNamesList();

	// 
	public String getHeadNameFor(String normHeadName);
	
	// Returns whether records exist to be read 
	public boolean existsRecord();
	
	// Returns value for given Header
	public String readRecord(String headerName);

}
