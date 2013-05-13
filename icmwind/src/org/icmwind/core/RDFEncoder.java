/**
 * 
 */
package org.icmwind.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RDFEncoder {
	
	/**
	 * Initialize Ontology Process - parsing Core Ontology, getting class names list and normalizing it.
	 */
	public void initOntologyProcess();
	
	/**
	 * Pass Data file source information as a Map. (Use this map to match Map.Key names to class names and link Map.Values to create instances of matched class name)  
	 * Use this Map to create instances
	 * 
	 * @param infoMap
	 */
	public void setDataFileSourceInfo(Map<String,String> infoMap);
	
	/**
	 * 
	 * @param filepath Path of file to encode.
	 */
	public void initFileProcess(String filepath);
	
	/**
	 * Get summary of the file being uploaded - Name,Number of observations, Begin/End Date
	 */
	public Map<String, String> getFileSummary();	
	
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
	 * Set the Analysis Period 
	 * 
	 * @param beginAnalysisPeriod
	 * @param endAnalysisPeriod
	 */
	public void setAnalysisPeriod(Date beginAnalysisPeriod, Date endAnalysisPeriod);
	
	/**
	 * Encode the file passed in RDFEncoder.initFileProcess(filepath) to RDF triples.
	 * Make each column's data record as Individual of Ontology Concept mapped to Column name.
	 * 
	 * @return true if success
	 */
	public boolean encode();

	/**
	 * Join SUB_SYSTEMs and SYSTEMs. MayBe pass OntModel(ABox) where we want to these relations  
	 * 
	 */
	public void isPartOfRelationEncoding();
	
	/**
	 * Join SENSORs and SUB_SYSTEMs. MayBe pass OntModel(ABox) where we want to these relations
	 * 
	 */
	public void isSensorOfRelationEncoding();
	
	/**
	 * Join PROPERTYs and SENSORs. MayBe pass OntModel(ABox) where we want to these relations
	 * 
	 */
	public void propertyMeasuredByRelationEncoding();
	
}
