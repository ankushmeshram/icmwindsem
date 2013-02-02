package org.icmwind.core;

import java.util.List;

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
	public boolean openFile(String filepath);

	/**
	 * Close data file
	 */
	public void closeFile();

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
	 * @return true if records are there to read
	 */
	public boolean existsRecord();

	/**
	 * @param headerName
	 *            Header Name for which the record is requested
	 * @return record for the given Header Name at current row
	 */
	public String readRecord(String headerName);

}
