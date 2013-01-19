package org.icmwind.core.impl;

import org.icmwind.core.OntologyProcess;

/*
 * Stuff which is needed to be initialized when system starts.
 * 
 * 1. OntologyProcess
 *    If needed...Ontology file can be changed from here.
 *    Creates list of class names,normalized class names and mapping between them to be used for RDFEncoding.initMapping()
 *    Creates map of class names to class URIs to be sued for creating Individuals RDFEncoding.createIndividual()
 *    
 *    
 *    
 *   
 * 
 */

public class SemInitializer {
	
	private SemInitializer() {}
	
	private static void init() {
		Utility.init();
		
		OntologyProcess ontoproc = OntologyProcessImpl.getInstance();
		ontoproc.openFile(Utility.getOntologyPath());
	}

	public static void main(String[] args){
		init();
	}

}
