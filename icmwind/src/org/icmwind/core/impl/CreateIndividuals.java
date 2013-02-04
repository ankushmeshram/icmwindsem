package org.icmwind.core.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class CreateIndividuals {

	public static void main(String[] args) throws IOException{
		
		//Open csv file. Get headers.
		
		FileProcessImpl fpi = FileProcessImpl.getInstance();
		fpi.openFile("E:/ICM-Wind/Code/icmwind/data_files/Marpingen1_2Lac.csv");
		
		List<String> headList = fpi.getHeadNamesList();
		String [] headerNames = new String[headList.size()];
		headerNames = headList.toArray(headerNames);
				
		//Jena model. Connect to ontology on disk. Get all classes and class names.
		
		String src = "http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl";
		String ns = src + "#";
		
		OntModel model = ModelFactory.createOntologyModel();
		OntDocumentManager manager = model.getDocumentManager();
		model.setNsPrefix("wto", src);
		manager.addAltEntry(src, "file:E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
		model.read(src, "RDF/XML");
		
		Set<OntClass> classes = model.listNamedClasses().toSet();
		Set<String> classNames = new HashSet<String>();
		
		for(OntClass clas : classes) {
			classNames.add(clas.getURI().substring(ns.length()));
		}
		
		// Maps Header-Class Name. ClassName-ClassURI.
		
		MappersImpl mi = new MappersImpl();
		
		Map<String, String> headerToClassNameMap = mi.getHeaderToClassMap("E:/ICM-Wind/Code/icmwind/config_files/classmapping.properties", headerNames);
		Map<String, OntClass> classToURIMap = new HashMap<String, OntClass>();
		
		for(String className : classNames) {
			for(OntClass aClass : classes) {
				if(className.equals(aClass.getURI().substring(ns.length()))) {
					classToURIMap.put(className, aClass);
				}
			}
		}
		
		// Create reference to Class: Observation, tempClass(null); ObjectProp: hasObservation,hasSamplingTime; DataProp: hasValue,observedTime;
		//		 			   Individual(String) : observationInst; Individual: tempObservationInstance(null), tempInstance(null).
		
		OntClass observation = model.getOntClass(ns + "Observation");
		OntClass tempClass = null;
		
		Individual tempObservationInstance = null;
		Individual tempInstance = null;
		
		ObjectProperty hasObservation = model.getObjectProperty(ns + "hasObservation");
		ObjectProperty hasSamplingTime = model.getObjectProperty(ns + "hasSamplingTime");
		
		DatatypeProperty hasValue = model.getDatatypeProperty(ns + "hasValue");
		DatatypeProperty observedTime = model.getDatatypeProperty(ns + "observedTime");
		
		String mappedClassName = null;
		String observationInst = ns + "Observation" + "_";
		
		// read records. create Observation instance. Read each row -> getRecord() 
		
		int i = 1;
		
		while(fpi.existsRecord() && i<21) {
			
			if(i%1000 == 0) {
				System.out.println(i);
			}
			
			tempObservationInstance = observation.createIndividual(observationInst + i);
			
			// for every element of a row, map head -> class name. tempClass = classname -> OntClassURI. tempInsatnce = tempClass.createInstance. Add Properties
			
			for(String head : headerNames) {
				if(headerToClassNameMap.containsKey(head)) {
					mappedClassName = headerToClassNameMap.get(head);
					tempClass = classToURIMap.get(mappedClassName);
					tempInstance = tempClass.createIndividual(ns + mappedClassName + "_" + i);
					if(mappedClassName.equals("Time")){
						String timeRecord = fpi.readRecord(head);
						tempInstance.addProperty(observedTime, timeRecord);
						tempObservationInstance.addProperty(hasSamplingTime, tempInstance);
					} else {
						float record = Float.parseFloat(fpi.readRecord(head));
						tempInstance.addProperty(hasObservation, tempObservationInstance);
						tempInstance.addLiteral(hasValue, record);
					}
				}
			}
	
			i++;
		}
				
		//close the fpi. write to a file/output stream.
		
		fpi.closeFile();
		model.write(new FileWriter(new File("WindTurbineOnto20.owl")), "RDF/XML", ns);
		model.close();

	}

}
