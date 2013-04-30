package org.icmwind.core.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.icmwind.core.FileProcess;
import org.icmwind.core.OntologyProcess;
import org.icmwind.core.RDFEncoder;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.SimilarityUtility;
import org.icmwind.util.TranslationUtility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;


public class RDFEncoderImpl implements RDFEncoder {

	// Single instance for RDFENcoder Interface
	private static RDFEncoderImpl INSTANCE = new RDFEncoderImpl();
	
	// Singleton instances for Ontology and File processing
	private static OntologyProcess ontoproc = OntologyProcessImpl.getInstance();
	private static FileProcess fileproc = FileProcessImpl.getInstance();
	
	// Lists to store Class names and normalized Class names too
	@SuppressWarnings("unused")
	private static List<String> classeslist; //TODO Check this.
	private static List<String> normclasseslist;
	
	// Lists to store Header names and normalized Header names too
	private static List<String> headerslist;
	private static List<String> normheaderslist;
	
	
	// MatchOrSuggest Map to store matches for Header names of the datasheet files in Class names of Core Ontology : <String headername. List classname>  
	private static Map<String, List<String>> matchOrSuggestMap = new HashMap<String, List<String>>();
	
	// Map to store exact mapping of Header Name to Class Name
	private static Map<String, String> headerToClassNameMap = new HashMap<String, String>();
	
	
	// Boolean to check whether file process started successfully and mapping is completed or not
	private static boolean isInitialised = false;
	
	// Boolean to check whether encoded file storage folder passed exists or not
	private static boolean existsStorageFolder = false;
	
	// Lcation to store the encoded files (ABoxes)
	private static String storeEncodedDataAt;
	
	// URI of core Ontology
	private static final String coreOntologyIri = "http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl";
	
//	private static AbstractStringMetric similaritymetric = new CosineSimilarity();
	
	private RDFEncoderImpl()
	{ 
		// Get resource paths from the ICMWindConfig.config 
		ICMWindConfig.init();

	}
	
	public static RDFEncoderImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void initOntologyProcess()
	{
		// Open  core Ontology from from Core Ontology resource path 
		ontoproc.openFile(ICMWindConfig.getCoreOntologyPath());
		
		// get List of normalized class names of Core Ontology
		normclasseslist = ontoproc.getNormClassNamesList();
	}

	@Override
	public void initFileProcess(String filepath)
	{
		// Open file from the passed file path
		fileproc.readFile(filepath);
		
		// get List of header names of passed Datasheet file
		headerslist = fileproc.getHeadNamesList();
		
		// get List of normalized header names of passed Datasheet file
		normheaderslist = fileproc.getNormHeadNamesList();
		
		// Find class names similar to header name
		// Create MatchOrSuggest map for the matches : <String headername, List classname>
		
//		Utility.setSimilarityMetric(similaritymetric);
		for(String normheader : normheaderslist) {
			List<String> templist = new ArrayList<String>(); 
			for(String normclass : SimilarityUtility.getSimilarTextList(normheader, normclasseslist)) {
				templist.add(ontoproc.getClassNameFor(normclass));
			}
			matchOrSuggestMap.put(fileproc.getHeadNameFor(normheader), templist);
		}
		
		isInitialised = true;
	}
	
	
	// Get MatchOrSuggest Map
	@Override
	public Map<String, List<String>> getMoSMap() {
		if(isInitialised)
			return matchOrSuggestMap;
		else
			return Collections.emptyMap();
	}

	
	// Set HeaderToClass Map
	@Override
	public boolean setHeaderToClassNamesMap(Map<String, String> head2ClassNameMap) {
		headerToClassNameMap = head2ClassNameMap;
		return true;
	}
	
	// Set the location to store the encoded files
	@Override
	public void setEncodeStorage(String folderPath) {
		File encodeStorageFolder = new File(folderPath);

		//if path doesnt exist..create one
		if(!encodeStorageFolder.exists())
			encodeStorageFolder.mkdirs();
		
		storeEncodedDataAt = folderPath;
		
		existsStorageFolder = true;
	}

	@Override
	public boolean encode() {
		
		//create New Ontology to store ABox
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology abox = null;

		//create iri for Abox
		IRI ontoiri = IRI.create(coreOntologyIri);
		IRI documentIri = null;
		
		//create file to save ABox with name pattern <FILE_NAME>_<TIMESTAMP>
		String aboxPath = fileproc.getFileName() + "_" + new Date().getTime() + ".owl";
		
		File aboxFile = null;
		
		//if folder exists create file with default name
		if(existsStorageFolder) {
			aboxFile = new File(storeEncodedDataAt, aboxPath);
			System.out.println("SYSTEM: File to save " + aboxFile.toString());
			documentIri = IRI.create(aboxFile);
		} else {
			System.out.println("ERROR: Storage Folder not defined. Check RDFEncoder.setEncodeStorage(folderpath)");
			System.exit(1);
		}
	
		//Map ontoiri to physical file
		manager.addIRIMapper(new SimpleIRIMapper(ontoiri, documentIri));
		
		//create ontology with ontoiri and which is saved to file
		try {
			abox = manager.createOntology(ontoiri);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
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
			
			//DEBUG
			System.out.println(tempObservationIndividual.toString() + " is_a" + observation.getIRI().getFragment().toString());
			
			//for every column of the record (row)
			for(String header : headerslist) {

				//DEBUG
				System.out.print("CHECK Header - " + header);
				
				//if column name has a corresponding ontology concept name in the mappingMap
				if(!headerToClassNameMap.get(header).equals("NA")) {
					
					//get mapped concept name
					mappedClassName = headerToClassNameMap.get(header);
					
					//get Ontlogy Concept URI for the concept name
					tempClass = ontoproc.getClassURIFor(mappedClassName);
					//DEBUG
					System.out.print(" ---- " + tempClass.getIRI().getFragment().toString());
					
					//create Ontology Individual for the mapped Ontology Class
					tempIndividual = ontoproc.createInstance(mappedClassName + "_" + i); 
					
					// add Assertion to Axioms holder
					axiomsSet.add(ontoproc.createClassAssertion(tempClass, tempIndividual));
					//DEBUG
					System.out.print(" ~~~~ " + tempIndividual.getIRI().getFragment().toString());
					
					//if it's 'Time' Ontology Class 
					//then connect 'Observation' Individual to 'Time' Individual via 'hasSamplingTime' Ontology Object Property
					//and connect 'Time' Individual to value of sampling time via 'observedTime' Ontology Data Property
					//Add to Axioms holder
					if(mappedClassName.equals("Time")){
						//get Sampling Time value
						String timeRecord = fileproc.readRecord(header);
						axiomsSet.add(ontoproc.createObjectPropertyAssertion(hasSamplingTime, tempObservationIndividual, tempIndividual));
						//DEBUG
						System.out.print(" **** " + tempObservationIndividual.toString() + "-" + hasSamplingTime.getIRI().getFragment().toString() + "-" + tempIndividual.getIRI().getFragment().toString());
						
						axiomsSet.add(ontoproc.createDataPropertyAssertion(observedTime, tempIndividual, timeRecord));
						//DEBUG
						System.out.print(" @@@@ " + tempIndividual.getIRI().getFragment().toString() + "-" + observedTime.getIRI().getFragment().toString() + "-" + timeRecord);
					} else {
						//Connnect 'Observation' Individual to temporary Individual via 'hasObservation' Ontology Object Property
						//Connect temporary Individual to value of it's record time via 'hasValue' Ontology Data Property
						//Add to Axioms holder
						float record = Float.parseFloat(fileproc.readRecord(header));
						axiomsSet.add(ontoproc.createObjectPropertyAssertion(hasObservation, tempIndividual, tempObservationIndividual));
						//DEBUG
						System.out.print(" **** " + tempIndividual.getIRI().getFragment().toString() + "-" + hasObservation.getIRI().getFragment().toString() + "-" + tempObservationIndividual.toString());
						
						axiomsSet.add(ontoproc.createDataPropertyAssertion(hasValue, tempIndividual, record));
						//DEBUG
						System.out.print(" @@@@ " + tempIndividual.getIRI().getFragment().toString() + "-" + hasValue.getIRI().getFragment().toString() + "-" + record);
					}
					
				}
				
				//DEBUG
				System.out.println();
			}
			
			//To avoid exhausting Heap Space,
			//after even number of iterations, append newly created Axioms to provided Ontology  and clear Axioms holder
			//TODO Add Axioms to new Ontology, we need ABox separately
			if(i%2==0){
				// add Axioms to abox Ontology
				ontoproc.addAxiomSet(abox, axiomsSet);
				axiomsSet.clear();
				System.out.println(i);
			}
			
			i++;
		}
		
		fileproc.closeFile();
		//TODO Add FileName to store ABox
		ontoproc.saveOntologyRDFXML(abox, aboxFile.getPath());
		
		return true;
	}

	

	
//	public static void main(String args[]) {
//		RDFEncoderImpl re = RDFEncoderImpl.getInstance();
//		re.initOntologyProcess();
//		re.initFileProcess(filepath);
//	}
}
