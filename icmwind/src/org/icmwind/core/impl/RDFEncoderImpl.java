package org.icmwind.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.icmwind.core.FileProcess;
import org.icmwind.core.OntologyProcess;
import org.icmwind.core.RDFEncoder;
import org.icmwind.util.ICMWindSetup;
import org.icmwind.util.Utility;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;


public class RDFEncoderImpl implements RDFEncoder {

	private static RDFEncoderImpl INSTANCE = new RDFEncoderImpl();
	
	private static OntologyProcess ontoproc = OntologyProcessImpl.getInstance();
	private static FileProcess fileproc = FileProcessImpl.getInstance();
	
	private static List<String> headerslist;
	private static List<String> classeslist;
	
	private static List<String> normheaderslist;
	private static List<String> normclasseslist;
	
//	private static AbstractStringMetric similaritymetric = new CosineSimilarity();
	
	private static Map<String, List<String>> mosMap = new HashMap<String, List<String>>();
	private static Map<String, String> mappingMap = new HashMap<String, String>();
	
	
	private static boolean isInitialised = false;
	
	
	private RDFEncoderImpl()
	{ 
		ICMWindSetup.init();
		
		try {
			Utility.initDictionary(ICMWindSetup.getDictionaryPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static RDFEncoderImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void initOntologyProcess()
	{
		ontoproc.openFile(ICMWindSetup.getCoreOntologyPath());
		normclasseslist = ontoproc.getNormClassNamesList();
	}

	@Override
	public void initFileProcess(String filepath)
	{
		fileproc.openFile(filepath);
		headerslist = fileproc.getHeadNamesList();
		normheaderslist = fileproc.getNormHeadNamesList();
		
//		Utility.setSimilarityMetric(similaritymetric);
		for(String normheader : normheaderslist) {
			List<String> templist = new ArrayList<String>(); 
			for(String normclass : Utility.getSimilarTextList(normheader, normclasseslist)) {
				templist.add(ontoproc.getClassNameFor(normclass));
			}
			mosMap.put(fileproc.getHeadNameFor(normheader), templist);
		}
		
		isInitialised = true;
	}

	@Override
	public Map<String, List<String>> getMoSMap() {
		if(isInitialised)
			return mosMap;
		else
			return Collections.emptyMap();
	}

	@Override
	public boolean setMapping(Map<String, String> matchMap) {
		mappingMap = matchMap;
		return true;
	}

	@Override
	public boolean encode() {
		
		//holder for all the Ontology Axioms
		Set<OWLAxiom> axiomsSet = new HashSet<OWLAxiom>();
		
		//'Observation' Ontology Class to be instantiated
		OWLClass observation = ontoproc.getClass("Observation");
		
		//temporary Ontology Class holder
		OWLClass tempClass = null;
		
		//temporary Ontology Individual holders 
		OWLIndividual tempObservationIndividual = null;
		OWLNamedIndividual tempIndividual = null;
 
		//'hasObservation' Ontology Object Property 
		OWLObjectProperty hasObservation = ontoproc.getObjectProperty("hasObservation");
		
		//'hasSamplingTime' Ontology Object Property
		OWLObjectProperty hasSamplingTime = ontoproc.getObjectProperty("hasSamplingTime");		

		//'hasValue' Ontology Data Property
		OWLDataProperty hasValue = ontoproc.getDataProperty("hasValue");
		
		//'observedTime' Ontology Data Property
		OWLDataProperty observedTime = ontoproc.getDataProperty("observedTime");		

		String mappedClassName = null;

		//counter for records read
		int i = 1;
		
		//while records(rows) exists in file 
		while(fileproc.existsRecord()) {
			
			//create instance of 'Observation' Ontology Class and add Assertion to Axioms holder
			tempObservationIndividual = ontoproc.createInstance("Observation" + "_" + i);
			axiomsSet.add(ontoproc.createClassAssertion(observation, tempObservationIndividual));
			
			//for every column of the record (row)
			for(String header : headerslist) {
				
				//if column name has a corresponding ontology concept name in the mappingMap
				if(mappingMap.containsKey(header)) {
					
					//get mapped concept name
					mappedClassName = mappingMap.get(header);
					
					//get Ontlogy Concept URI for the concept name

					
					
				}
				
			}
			
		}
		
		
		return false;
	}

	

}
