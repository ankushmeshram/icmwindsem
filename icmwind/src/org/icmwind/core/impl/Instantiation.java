package org.icmwind.core.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Instantiation {

	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
		long time = System.currentTimeMillis();
		
		FileProcessImpl fpi = new FileProcessImpl();
		fpi.openFile("E:/ICM-Wind/Code/icmwind/data_files/Marpingen1_2Lac.csv", ';');
		
		String [] headerNames = fpi.getHeaderNames();
		
		OntologyProcessImpl opi = new OntologyProcessImpl();
		opi.openOntologyFromFile("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
		
		Set<OWLClass> classes = opi.getClasses();
		Set<String> classNames = opi.getClassNames();
		
		MappersImpl mi = new MappersImpl();
		
		Map<String, String> headerToClassNameMap = mi.getHeaderToClassMap("E:/ICM-Wind/Code/icmwind/config_files/classmapping.properties", headerNames);
//		Map<String, String> classToHeaderNameMap = mi.getClassToHeaderMap();
		Map<String, OWLClass> classToUriMap = mi.getClassToURIMap(classNames, classes);
		
		int i = 1;
		Set<OWLAxiom> axiomsSet = new HashSet<OWLAxiom>();
		
		OWLClass observation = opi.getClass("Observation");
		OWLClass tempClass = null;
		
		OWLIndividual tempObservationIndividual = null;
		OWLNamedIndividual tempIndividual = null;
 
		OWLObjectProperty hasObservation = opi.getObjectProperty("hasObservation");
		OWLObjectProperty hasSamplingTime = opi.getObjectProperty("hasSamplingTime");		

		OWLDataProperty hasValue = opi.getDataProperty("hasValue");
		OWLDataProperty observedTime = opi.getDataProperty("observedTime");		

		String mappedClassName = null;
		
		while(fpi.getReader().readRecord() && i<16) {
			
			tempObservationIndividual = opi.createInstance("Observation" + "_" + i);
			axiomsSet.add(opi.createClassAssertion(observation, tempObservationIndividual));
			
			for(String head : headerNames) {
				if(headerToClassNameMap.containsKey(head)) {
					
					mappedClassName = headerToClassNameMap.get(head);
					
					tempClass = classToUriMap.get(mappedClassName);
					
					tempIndividual = opi.createInstance(mappedClassName + "_" + i);
					
					axiomsSet.add(opi.createClassAssertion(tempClass, tempIndividual));
					
					if(mappedClassName.equals("Time")){
						String timeRecord = fpi.getRecord(head);
						axiomsSet.add(opi.createObjectPropertyAssertion(hasSamplingTime, tempObservationIndividual, tempIndividual));
						axiomsSet.add(opi.createDataPropertyAssertion(observedTime, tempIndividual, timeRecord));
					} else {
						float record = Float.parseFloat(fpi.getRecord(head));
						axiomsSet.add(opi.createObjectPropertyAssertion(hasObservation, tempIndividual, tempObservationIndividual));
						axiomsSet.add(opi.createDataPropertyAssertion(hasValue, tempIndividual, record));
					}
				}
			}
			
			if(i%2==0){
				opi.addAxiomSet(opi.getOntology(), axiomsSet);
				axiomsSet.clear();
				System.out.println(i);
			}
			
			i++;
		}
		
		fpi.closeFile();
		opi.saveOntologyRDFXML(opi.getOntology(), "E:/ICM-Wind/Code/icmwind/ontology/individuals/WindTurbineOnto15.owl");
		
		System.out.println("Ontology saved.");
		System.out.println("DONE.");		
		System.out.println( "Time taken: " + (System.currentTimeMillis() - time) + " milliseconds." );
	}

}
