package org.icmwind.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.csvreader.CsvReader;
import com.google.common.collect.HashBiMap;

public class MainProgram {

	/**
	 * @param args
	 * 
	 * 1.  Get headers from CSV and also ontology_classes from OWLOntology
	 * 
	 * 2.  Create HashBiMap <HeaderName,ClassName> for headers in CSV using classMapping.properties [HeaderName=OntologyClassName].
	 *     USAGE: To refer "CS Drive" => "CS_Drive" and (reverse map to refer "CS_Drive" => "CS Drive").will be used CsvReader.get(CS Drive)
	 *     
	 * 3.  Create classNames Set<String>
	 * 	   CONDITION: if ClassName is contained in Set<String> OWLOntology.getOWLClass.getIRI.getFragment() then add to set. 
	 *     USAGE: To create reference to ClassURI
	 *     
	 * 4.  Create Map <classNames,OWLClass> "CS_Drive" => "www.onotlogy.owl/#CS_Drive" 
	 *     CONDITION: ONLY FOR HEADERS PRESENT IN FILE THEREFORE DO THIS UNDER 2a while checking the present headers.
	 *     
	 * 5.  i=0
	 * 	   Set<OWLAxioms> axiomSet
	 * 	   while reading record
	 * 		get header in String head : (String head : headers)
	 * 		make observation individual
	 * 				get reference to Observation Class
	 * 				create Observation Individual
	 * 				create reference to data property "hasSamplingTime"
	 *              DataProperty(hasSamplingTime,ObservationIndividual,Time)
	 *              add Axiom to AxiomSet : Set<OWLAxioms> axiomSet.add(Axiom1)
	 * 		for every head 
	 * 			get record: String record = CsvReader.get(head)
	 * 			invoke reference to Class: OWLClass tempClass = factory.getOWLClass(map.get(reverseBiMap.get(head)))
	 *          String individualName = ":" + head + "_" + i
	 * 			create new individual: OWLNamedIndividual tempIndi = factory.getNamedIndividual(IRI.create("individualName",pm))
	 * 			make individual instance of class : OWLClassAssertionAxiom = ClassAssertionAxiom(tempClass,tempIndi) [CS_Drive_1 is a CS_Drive]
	 *          create reference to data property "hasValue"
	 *          make DataProperty assertion: OWLDataPropertyAssertionAxiom = DataPropertyAssertionAxiom(tempIndi,record)
	 *          add Axiom to AxiomSet : Set<OWLAxioms> axiomSet.add(Axiom1) 
	 * 	   manager.addAxiom(OWLOntology,axiomSet)
	 * 	   CLEAN axiomSet
	 * 	 
	 * 6. Save ontology to new file.
	 * @throws IOException 
	 * @throws OWLOntologyCreationException 
	 * @throws OWLOntologyStorageException 
	 *             
	 * 
	 */
	
	
	public static void main(String[] args) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {

		long time;
		
		time = System.currentTimeMillis();
		//**Get "headers" from CSV file.
//		CsvReader reader = new CsvReader(new FileReader("E:/ICM-Wind/Code/icmwind/data_files/TestData.csv"), ';');
		CsvReader reader = new CsvReader(new FileReader("E:/ICM-Wind/Code/icmwind/data_files/Marpingen1_2Lac.csv"), ';');
		reader.readHeaders();
		String [] headers = reader.getHeaders();
		
				
		//**Get "ontologyClassNames" from Ontology.
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory factory = manager.getOWLDataFactory();

		//*Get ontology from hard disk.
		File ontologyFile = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
		IRI ontologyIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
		manager.addIRIMapper(new SimpleIRIMapper(ontologyIri, IRI.create(ontologyFile)));
		
		//*Get classes
		OWLOntology ontology = manager.loadOntology(ontologyIri);
		Set<OWLClass> ontologyClasses = ontology.getClassesInSignature();
		
		//*Get class names from ontology
		Set<String> ontologyClassNames = new HashSet<String>();
		for(OWLClass aClass : ontologyClasses) {
			ontologyClassNames.add(aClass.getIRI().getFragment());
		}
		
		
		//**HashBiMap <HeaderName,ClassName>
		//*Get property file
		Properties prop = new Properties();
		prop.load(new FileInputStream("E:/ICM-Wind/Code/icmwind/config_files/classmapping.properties"));
		
		//*Map Header_Name => Class_Name
		HashBiMap<String, String> mapNamesHeaderToClass = HashBiMap.create();
		
		//*Map Class_Name => Header_Name
		Map<String, String> mapNamesClassToHeader = mapNamesHeaderToClass.inverse();
		
		//* Add only elements which are mapped (i.e. ignore Null) and have mapping in Properties (i.e. ignore mappings with "NaN"). 
		for(String head : headers) {
			if( (prop.getProperty(head)!=null) && (! prop.getProperty(head).equals("NaN")) ) {
				mapNamesHeaderToClass.put(head, prop.getProperty(head));
			}
		}
		
		
		//** Create Set<String> classNames and add ClassName to it 
		Set<String> classNames = new HashSet<String>();
		
		//* Header name => MappedHeader name. IF MappedHeader name == OntologyClass name ONLY THEN add.
		for(String headerName : headers){
			for(String ontClassName : ontologyClassNames) {
				if(ontClassName.equals(prop.getProperty(headerName))) {
					classNames.add(prop.getProperty(headerName));
				}
			}
		}
		
		
		//**Map <classNames, OWLClass>
		Map<String, OWLClass> mapNameToURI = new HashMap<String, OWLClass>();
		for(String className : classNames) {
			mapNameToURI.put(className, factory.getOWLClass(IRI.create(ontologyIri + "#" + className)));
		}
		
		
		//**Read record for which we have mapped class names 
		
		int i = 1;
		
//		//*Set<OWLAxiom> Creation
//		Set<OWLAxiom> axiomsSet = new HashSet<OWLAxiom>();
		
		//*OWLClass Observation Reference
		OWLClass observation = factory.getOWLClass(IRI.create(ontologyIri+"#Observation"));
		
		//*OWLClass tempClass
		OWLClass tempClass = null;

		//*OWLIndividual tempObservationIndividual 
		OWLIndividual tempObservationIndividual = null;
		
		//*OWLIndividual tempIndividual
		OWLIndividual tempIndividual = null;
		
		//*ObjectProperty hasObservation Reference 
		OWLObjectProperty hasObservation = factory.getOWLObjectProperty(IRI.create(ontologyIri + "#hasObservation"));
		
		//*ObjectProperty hasSamplingTime Reference 
		OWLObjectProperty hasSamplingTime = factory.getOWLObjectProperty(IRI.create(ontologyIri + "#hasSamplingTime"));		
		
		//*DataProperty hasValue Reference
		OWLDataProperty hasValue = factory.getOWLDataProperty(IRI.create(ontologyIri + "#hasValue"));
		
		//*DataProperty observedTime Reference
		OWLDataProperty observedTime = factory.getOWLDataProperty(IRI.create(ontologyIri + "#observedTime"));		
		
		
		while(reader.readRecord() && i<20001){
			
			//*Observation OWLIndividual Creation 
			tempObservationIndividual = factory.getOWLNamedIndividual(IRI.create(ontologyIri + "#Observation" + "_" + i));
			
			for(String cls : classNames){
				
				//*OWLClass Reference
				tempClass = mapNameToURI.get(cls);
				
				//*OWLIndividual Creation
				tempIndividual = factory.getOWLNamedIndividual(IRI.create(ontologyIri + "#" + cls + "_" + i));

				//**ASSERTIONS
				//*Class Assertions
//				axiomsSet.add(factory.getOWLClassAssertionAxiom(tempClass, tempIndividual));
				manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(tempClass, tempIndividual));
								
				if(cls.equals("Time")){
					
					//*Read every column record of a row and for NaN
					String timeRecord = reader.get(mapNamesClassToHeader.get(cls));
					
					//*hasSamplingTime Object Property Assertion
//					axiomsSet.add(factory.getOWLObjectPropertyAssertionAxiom(hasSamplingTime, tempObservationIndividual, tempIndividual));
					manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasSamplingTime, tempObservationIndividual, tempIndividual));

					//*observedTime Data Property Assertion
//					axiomsSet.add(factory.getOWLDataPropertyAssertionAxiom(observedTime, tempIndividual, timeRecord));
					manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(observedTime, tempIndividual, timeRecord));
				} else {
					
					//*Read every column record of a row and for NaN
					float record = Float.parseFloat(reader.get(mapNamesClassToHeader.get(cls)));
					
					//*hasObservation Object Property Assertion					
//					axiomsSet.add(factory.getOWLObjectPropertyAssertionAxiom(hasObservation, tempIndividual, tempObservationIndividual));
					manager.addAxiom(ontology, factory.getOWLObjectPropertyAssertionAxiom(hasObservation, tempIndividual, tempObservationIndividual));
					
					//*hasValue Data Property Assertion
//					axiomsSet.add(factory.getOWLDataPropertyAssertionAxiom(hasValue, tempIndividual, record));
					manager.addAxiom(ontology, factory.getOWLDataPropertyAssertionAxiom(hasValue, tempIndividual, record));
				}
			}
			
//			axiomsSet.add(factory.getOWLClassAssertionAxiom(observation, tempObservationIndividual));
			manager.addAxiom(ontology, factory.getOWLClassAssertionAxiom(observation, tempObservationIndividual));
			
//			if(i%500==0){
//				//**Add all the axioms
//				manager.addAxioms(ontology, axiomsSet);
//				
//				//**Clear AxiomSet
//				axiomsSet.clear();
//				
//				System.out.println(i);
//			}
			
			System.out.println(i);
			i++;
		}
		
		System.out.println("Axioms added to ontology. Now saving file.");
//		//**Add all the axioms
//		manager.addAxioms(ontology, axiomsSet);
		
		reader.close();
		//** Save ontology with axioms to OutPut File
//		File outOntologyFile = new File("E:/ICM-Wind/Code/icmwind/ontology/with_individuals/WindTurbineOntoTest.owl");
		File outOntologyFile = new File("E:/ICM-Wind/Code/icmwind/ontology/with_individuals/WindTurbineOnto.owl");
		manager.saveOntology(ontology,IRI.create(outOntologyFile));
		
		System.out.println( "Time taken: " + (System.currentTimeMillis() - time) + " milliseconds." );
		System.out.println("DONE.");
	}
	
	/**
	 * Time taken: 91863 milliseconds.
	 */
}
