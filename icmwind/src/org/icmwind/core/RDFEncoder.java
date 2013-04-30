/**
 * 
 */
package org.icmwind.core;

import java.util.List;
import java.util.Map;

public interface RDFEncoder {
	
	/**
	 * Initialize Ontology Process - parsing Core Ontology, getting class names list and normalizing it.
	 */
	public void initOntologyProcess();
	
	/**
	 * 
	 * @param filepath Path of file to encode.
	 */
	public void initFileProcess(String filepath);
	
	/**
	 * @return
	 */
	public Map<String, List<String>> getMoSMap();
	
	/**
	 * Set the mapping of column names to concept names
	 * 
	 * @param matchMap Map which has mapping of Column name to Ontology Concept name. 
	 * @return true if success
	 */
	public boolean setHeaderToClassNamesMap(Map<String, String> matchMap);
	
	/**
	 * Set the location to store encoded files (ABoxes) 
	 * 
	 * @param folderPath Path to folder to save Encoded file
	 * @return true if folder exists.
	 */
	public void setEncodeStorage(String folderPath);
	
	/**
	 * Encode the file passed in RDFEncoder.initFileProcess(filepath) to RDF triples.
	 * Make each column's data record as Individual of Ontology Concept mapped to Column name.
	 * 
	 * @return true if success
	 */
	public boolean encode();

}
