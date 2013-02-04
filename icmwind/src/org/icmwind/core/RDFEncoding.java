/**
 * 
 */
package org.icmwind.core;

import java.util.List;
import java.util.Map;


/**
 * @author ANKUSH
 * 
 * FileProcessor
 * 	Get Header Names from Input File = HN
 * 	Normalization
 * 		Remove unwanted characters from them
 *  	Change everything to lower case
 * 		Use Dictionary(*.properties with everything in lowercase) to change German to English
 *  Save changes as Normalised Header Name = NHN and map NHN->HN via some implementation
 *
 *  [ HN: 	CS Drive  HLB %S  VLGW  dP  T-Ein  Zeit ] 			 
 *  [ NHN:	cs drive  hlb s   vlgw dp   t ein  time ]
 *  
 *  
 * OntologyProcessor
 * 	Get Class Names from Core Ontology = CN
 *  Normalization
 *  	Remove unwanted characters
 *  	Change everything to lower case
 *  Save changes as Normalized Class Names = NCN and map NCN->CN via some implementation
 *  
 *  [ CN:	CS_Drive  VLGW_dP  T_Air]
 *  [ NCN:	cs drive  vlgw dp  t air]
 *  
 *  
 * Mapper
 *  Map NHN to NCN i.e. search HeaderNames into ClassNames
 *  	if match found
 *  		K<-getMappedHN(NHN)i.e.HN, V<-getMappedCN(NCN)i.e.CN		
 *  		HN2CNMap.add(K,V)
 *  	else
 *  		get Suggestions i.e. list of NCN for given NHN
 *  		selected_Suggestion <- select one of the Suggestions from User
 *  		K<-getMappedHN(NHN), V<-getMappedCN(selected_Suggestion)
 *			HN2CNMap.add(K,V)
 *	
 *
 * "Create a separate layer of processed header files over data sheet which can be mapped to Ontology Names"
 * 		List<String> mappedHN <- HN2CNMap.get(HN)
 *  [ HN: 	CS Drive  HLB %S  Zeit ]
 *  [ MHN:	CS_Drive  HLB_S   Time ]
 *  
 *  
 * 
 *
 */

public interface RDFEncoding {
	
	public void initFileProcess(String filepath);
	
	public void initOntologyProcess();
	
	public Map<String, List<String>> returnMapping();
	

}
