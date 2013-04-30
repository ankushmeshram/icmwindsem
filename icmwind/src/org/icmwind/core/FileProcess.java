package org.icmwind.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ANKUSH
 * 
 *         Interface defining the functionalities: a. Open File from filePath /
 *         Open CSVFile from CSV File b. Process to get HeaderNames List c.
 *         Normalize HeaderNames to NormalizedHeaderNames Remove unwanted
 *         characters Change everything to lower case Translate german2english
 *         Map NormalizedHeaderNames2HeaderNames d. Get NormalizedHeaderNames e.
 *         Get record for given column / (Get record for given column at given
 *         row)
 * 
 */

public interface FileProcess {

	/**
	 * @param separator
	 *            Data separator for the data file.
	 */
	public void setSeparator(char separator);

	/**
	 * @param filepath
	 *            Path of Data File to process. Default data separator is ";".
	 *            Change it via void setSeparator(char separator) before
	 *            invoking this class. It also extracts Header Names from the
	 *            file.
	 * @return true if success
	 */
	public boolean readFile(String filepath);

	public long getNumberOfRows();
	
	/**
	 * Close data file
	 */
	public void closeFile();

	/**
	 * @return File name of the File passed for analysis.
	 */
	public String getFileName();
	
	/**
	 * @return List of Header Names
	 */
	public List<String> getHeadNamesList();

	/**
	 * @return List of Normalized Header Names i.e. Header Names after applying
	 *         void Normalize()
	 */
	public List<String> getNormHeadNamesList();

	/**
	 * @param normHeadName
	 *            Pass normalized header name
	 * @return Header Name corresponding to given Normalized Header Name
	 */
	public String getHeadNameFor(String normHeadName);
	
	/**
	 * @return Map<String, String> containing key-value pair "normalized_head_name-head_name"
	 */
	public Map<String, String> getNormalizationMap();

	/**
	 * @return true if records are there to read
	 */
	public boolean existsRecord();

	/**
	 * @param headerName
	 *            Header Name for which the record is requested
	 * @return record for the given Header Name at current row
	 */
	public String readRecord(String headerName);
	
	public String getTimeBegin();
	
	public Date getBeginPeriod();
	
	public Date getEndPeriod();
		
	public String getTimeEnd();

}
