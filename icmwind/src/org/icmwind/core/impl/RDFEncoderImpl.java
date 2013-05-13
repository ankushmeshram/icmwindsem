package org.icmwind.core.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.icmwind.core.FileProcess;
import org.icmwind.core.RDFEncoder;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.Normalization;
import org.icmwind.util.SimilarityUtility;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Literal;




public class RDFEncoderImpl implements RDFEncoder {

	// Single instance for RDFENcoder Interface
	private static RDFEncoderImpl INSTANCE = new RDFEncoderImpl();
	
	// Singleton instances for Ontology and File processing
//	private static OntologyProcess ontoproc = OntologyProcessImpl.getInstance();
	private static OntologyProcessJenaImpl op = OntologyProcessJenaImpl.getInstance();
	private static FileProcess fileproc = FileProcessImpl.getInstance();
	
	// Lists to store Class names and normalized Class names too
	@SuppressWarnings("unused")
	private static List<String> classeslist; //TODO Check this.
	private static List<String> normclasseslist;
	
	// Lists to store Header names and normalized Header names too
	private static List<String> headerslist;
	private static List<String> normheaderslist;
	
	// Start and End Dates between which the data needs to be encoded
	private static Date startAnalysisPeriod = null;
	private static Date endAnalysisPeriod = null;
	
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
	private static final String coreOntologyIri = "http://www.icmwind.com/WindTurbineOnto.owl";
	
	// Namespace of ontology
	private static final String NS = coreOntologyIri + "#";
	
	private static final String DATASTORE_FOLDER = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store/";
	
	Logger logger = Logger.getLogger(RDFEncoderImpl.class);
	
//	private static AbstractStringMetric similaritymetric = new CosineSimilarity();
	
	private RDFEncoderImpl()
	{ 
		// Get resource paths from the ICMWindConfig.config 
		ICMWindConfig.init();

	}
	
	public static RDFEncoderImpl getInstance() {
		System.out.println("RDFEncoderImpl.getInstance()");
		return INSTANCE;
	}
	
	@Override
	public void initOntologyProcess()
	{
		// Open  core Ontology from from Core Ontology resource path 
//		ontoproc.openFile(ICMWindConfig.getCoreOntologyPath());
		op.init();
		
		// get List of normalized class names of Core Ontology
//		normclasseslist = ontoproc.getNormClassNamesList();
		normclasseslist = op.getNormClassNamesList();
	}

	@Override
	public void initFileProcess(String filepath)
	{
		// Open file from the passed file path
		fileproc.readFile(filepath);
		
		// get List of header names of passed Datasheet file
		headerslist = fileproc.getColumnNamesList();
		
		// get List of normalized header names of passed Datasheet file
		normheaderslist = fileproc.getNormColumnNamesList();
		
		// Find class names similar to header name
		// Create MatchOrSuggest map for the matches : <String headername, List classname>
		
//		Utility.setSimilarityMetric(similaritymetric);
		for(String normheader : normheaderslist) {
			List<String> templist = new ArrayList<String>(); 
			for(String normclass : SimilarityUtility.getSimilarTextList(normheader, normclasseslist)) {
//				templist.add(ontoproc.getClassNameFor(normclass));
				templist.add(op.getClassNameFor(normclass));
			}
			matchOrSuggestMap.put(fileproc.getColumnNameFor(normheader), templist);
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
		/*
		 * createTBoxInstancesAndRelations();
		 * 
		 * isWithinAnalysisPeriod();
		 * 	int i = 0;
		 * 
		 * createObservationInstance();
		 * createTimeInstance(); // date to xsd:dateTime
		 * create hasSamplingTimeRelationEncoding()
		 * 
		 * create
		 * 
		 * 
		 *  		
		*/
		
		
/*		
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
*/		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		Calendar c = Calendar.getInstance();
		c.setTime(endAnalysisPeriod);
		c.add(Calendar.DATE, 1);
		endAnalysisPeriod = c.getTime();
		
		System.out.println("endAnalysis : " + endAnalysisPeriod);
		
		int rowsRead = 1;
		
		String beginDate = null;
		String endDate = null;
		
		boolean reset = false;
	
		OntModel abox = null;
		
		//while records(rows) exists in file 
		while(fileproc.existsRecord()) {
			try {
				
				Date temp = sdf.parse(fileproc.readRecord("Zeit"));
				
				// read when the observation date is between startAnalysisPeriod and endAnalysisPeriod
				if(temp.equals(startAnalysisPeriod) || (temp.after(startAnalysisPeriod) && temp.before(endAnalysisPeriod)) ) {
				
					// Create ABox
					if(rowsRead == 1 || reset) {
						reset = false;
						beginDate = fileproc.readRecord("Zeit").substring(0, 10);
//						abox =
//						op.createAbox();
						System.out.println("ABox Created.");
					}
///*
//					OntClass tempObservationClass = op.getClass("Observation");
					OntClass tempObservationClass = op.getClassForURI(op.getNS() + "Observation");
//					OntClass tempObservationClass = abox.getOntClass(op.getNS() + "Observation");
					Individual tempObservationInstance = op.createInstance(tempObservationClass, op.getNS() + "Observation_" + rowsRead);
					
					
					// Read headers acc to final headerToClassNameMap
					for(Map.Entry<String, String> entry :  headerToClassNameMap.entrySet()) {
						
						String headerName = entry.getKey();
						String propertyName = entry.getValue();
						
						System.out.println("Reading header - " + headerName + " : class - " + propertyName);
						
						// If Sensor measuring a particular property class name is present in existing scenario, only then do this
						if(checkSensorAvailabilityFor(propertyName)) {
							
							// Get mapped Class for Header and create its instance 
							OntClass tempClass = op.getClassForURI(op.getClassURIFor(propertyName));
							Individual tempInstance = op.createInstance(tempClass, op.getNS() + propertyName + "_" + rowsRead);
							
							if(headerName.equals("Zeit")) {
								
								// Time--observedTime--""^^xsd:dateTime
								DatatypeProperty observedTimeDataProperty = op.getDataPropertyForURI(op.getNS() + "observedTime");
								Literal tempTimeLiteral = op.createTimeLiteral(temp);
								tempInstance.addProperty(observedTimeDataProperty, tempTimeLiteral);
								
								// Observation--hasSamplingTime--Time
								ObjectProperty hasSamplingTimeObjectProperty = op.getObjectPropertyForURI(op.getNS() + "hasSamplingTime");
								tempObservationInstance.addProperty(hasSamplingTimeObjectProperty, tempInstance);
							} else {
															
								// Property--hasValue--""^^xsd:Float
								DatatypeProperty hasValueDataProperty = op.getDataPropertyForURI(op.getNS() + "hasValue");
								String value = fileproc.readRecord(headerName);
								System.out.println("record value : " + value);
								Literal tempValueLiteral = op.createValueLiteral(Float.parseFloat(value));
								tempInstance.addProperty(hasValueDataProperty, tempValueLiteral);
								
								// Property--hasObservation--Observation
								ObjectProperty hasObservationObjectProperty = op.getObjectPropertyForURI(op.getNS() + "hasObservation");
								tempInstance.addProperty(hasObservationObjectProperty, tempObservationInstance);
							
								// Sensor--measuresProperty--Property
								String sensorClassName = op.getPropertyClassNameToSensorClassNameMap().get(propertyName);
								String sensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(sensorClassName);
								Individual tempSensorInstance = op.getInstanceForURI(sensorInstanceURI);
								ObjectProperty measuresPropertyObjectProperty = op.getObjectPropertyForURI(op.getNS() + "measuresProperty");
								tempSensorInstance.addProperty(measuresPropertyObjectProperty, tempInstance);
							}
						} // close checkSensorAvailabilityFor()
					}// finish reading a row
					
					System.out.println("******Observations read : " + rowsRead);
										
					rowsRead++;
					
					
				} else if(temp.equals(endAnalysisPeriod)) {
					endDate = fileproc.readRecord("Zeit").substring(0, 10);

					String timestamp = beginDate + "-" + endDate; 
					
					String aboxUri = "http://www.icmwind.com/instances/WTO" + "-" + timestamp + ".owl";
					String aboxFile = DATASTORE_FOLDER + "ontologies/wto-abox-" + timestamp + ".owl";
					
					if(op.saveAboxToFile(aboxUri, aboxFile))
						System.out.println("File saved at " + aboxFile);
					else
						System.out.println("Error in saving file " + aboxFile);
				}
				
				
				// Write ABox to a File
				if( (rowsRead % 10000) == 0 ) {
					endDate = fileproc.readRecord("Zeit").substring(0, 10);

					String timestamp = beginDate + "-" + endDate; 
					
					String aboxUri = "http://www.icmwind.com/instances/WTO" + "-" + timestamp + ".owl";
					String aboxFile = DATASTORE_FOLDER + "ontologies/wto-abox-" + timestamp + ".owl";
					
					if(op.saveAboxToFile(aboxUri, aboxFile))
						System.out.println("File saved at " + aboxFile);
					else
						System.out.println("Error in saving file " + aboxFile);
					
					
					reset = true;
				}
					
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			

			
/*			
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
*/	
		} // all the rows have been read
	
		fileproc.closeFile();
		//TODO Add FileName to store ABox
//		ontoproc.saveOntologyRDFXML(abox, aboxFile.getPath());
		
		return true;
	}

	@Override
	public Map<String, String> getFileSummary() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", fileproc.getFileName());
		map.put("begin", sdf.format(fileproc.getBeginDate()));
		map.put("end", sdf.format(fileproc.getEndDate()));
		
		return map;
	}

	@Override
	public void setAnalysisPeriod(Date beginAnalysisPeriod, Date endAnalysisPeriod) {
		RDFEncoderImpl.startAnalysisPeriod = beginAnalysisPeriod;
		RDFEncoderImpl.endAnalysisPeriod = endAnalysisPeriod;
	}

	@Override
	public void setDataFileSourceInfo(Map<String, String> infoMap) {
		System.out.println("RDFEncoderImpl.setFileSourceInfo(infoMap)");
		
		// Create ABox Model
		op.createAbox();
		String aboxUri = "http://www.icmwind.com/instance/wto_abox.owl";
		String aboxFile = "C:/Users/anme05/Desktop/wto_abox.owl";
		
		Normalization normalization = new Normalization();
		
		// map to store classname-individual mappings
		Map<String, String> map = new HashMap<String, String>();
		
		// convert key set to key list, normalise key list, get normKeyMap
		List<String> normKeyList = normalization.normalizeList(new ArrayList<>(infoMap.keySet()));
		Map<String,String> mapNormKeyToKey = normalization.getNormalizationMap();
				
		// get normalised class names list i.e. normclasseslist
		System.out.println("\n" + normclasseslist.toString());
				
		// get similarity of normCNL, normKL as mapResult
		Map<String, List<String>> simMap = SimilarityUtility.getSimilarityBetweenLists(normKeyList, normclasseslist);
		
		for(Map.Entry<String, List<String>> entry : simMap.entrySet()) {
			
			if(entry.getKey().toLowerCase().equals("name")) {
				// create Wind Turbine instance from the value of the key
				String tempClassName = "Wind_Turbine";
				// Get OntClass from URI
				String tempClassURI = op.getClassURIFor(tempClassName);
				OntClass tempClass = op.getClassForURI(tempClassURI);
				
				// Get OWL Class from class name
//				System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
				
				String tempKeyName = mapNormKeyToKey.get(entry.getKey());
				String tempInstncName = infoMap.get(tempKeyName);
				String tempInstncURI = NS + tempInstncName;
				// Individual URI
				System.out.println("@ " + tempInstncURI);
				
				// add to ClassName-IndividualURI map
				map.put(tempClassName, tempInstncURI);
				
				// Create OWL Individual using OWL Class & Individual URI
				op.createInstance(tempClass, tempInstncURI);
			}
			
			// if no matching found
			if(!entry.getValue().isEmpty()) {
				if(entry.getKey().toLowerCase().equals("location")) {
					continue;
				} else {
					// get class name from matched norm class name, get its URI
//					String tempClassName =  ontoproc.getClassNameFor(entry.getValue().get(0));
					String tempClassName =  op.getClassNameFor(entry.getValue().get(0));
					// Get OntClass from URI
					String tempClassURI = op.getClassURIFor(tempClassName);
					OntClass tempClass = op.getClassForURI(tempClassURI);
					
					// Get OWL Class from class name
//					System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
										
					// get key name from norm key name, get its value, add NS to value
					String tempKeyName = mapNormKeyToKey.get(entry.getKey());
					String tempInstncName = infoMap.get(tempKeyName);
					String tempInstncURI = NS + tempInstncName;
					// Individual URI
					System.out.println("@ " + tempInstncURI);
					
					// add to ClassName-IndividualURI map
					map.put(tempClassName, tempInstncURI);
					
					// Create OWL Individual using OWL Class & Individual URI
					op.createInstance(tempClass, tempInstncURI);
				}
			}
		}
		
		
		
		// set the classname-inidividual mappings
//		ontoproc.setClassNameToIndividualURIMap(map);
		op.setTBoxClassNameToIndividualURIMap(map);
		
		// start "partOf" relations creation
		isPartOfRelationEncoding();
		
		// start "isSensorOf" relations creation
		isSensorOfRelationEncoding();
		
		op.saveAboxToFile(aboxUri, aboxFile);
	}

	@Override
	public void isPartOfRelationEncoding() {
		
//		for(Map.Entry<String, String> entry : ontoproc.getPartClassNameToSystemClassNameMap().entrySet()) {
//			String tempSubSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getKey());
//			String tempSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getValue());
//			String isPartOfPropertyURI = "";
//			
//			// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
//		}
		
		for(Map.Entry<String, String> entry : op.getPartClassNameToSystemClassNameMap().entrySet()) {
			System.out.println("Part - " + entry.getKey().toString() + " : System - " + entry.getValue().toString());
			
			String tempSubSystemInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey());
			String tempSystemInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue());
			String isPartOfPropertyURI = NS + "isPartOf";
			
			// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
			Individual subSysInstance = op.getInstanceForURI(tempSubSystemInstanceURI);
			Individual sysInstance = op.getInstanceForURI(tempSystemInstanceURI);
			ObjectProperty isPartOfObjectProperty = op.getObjectPropertyForURI(isPartOfPropertyURI);
			
			subSysInstance.addProperty(isPartOfObjectProperty, sysInstance);
			
		}
	}

	@Override
	public void isSensorOfRelationEncoding() {
		
//		for(Map.Entry<String, String> entry : ontoproc.getSensorClassNameToPartClassNameMap().entrySet()) {
//			String tempSubSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getKey());
//			String tempSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getValue());
//			String isSensorOfPropertyURI = "";
//			
//			// use these URIs for assertions :	(tempSubSystemClassURI,isSensorOfPropertyURI,tempSystemClassURI)
//		}
		for(Map.Entry<String, String> entry : op.getSensorClassNameToPartClassNameMap().entrySet()) {
			System.out.println("Sensor - " + entry.getKey().toString() + " : Part - " + entry.getValue().toString());
			
			String tempSensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey());
			String tempPartInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue());
			String isSensorOfPropertyURI = NS + "isSensorOf";
			
			// use these URIs for assertions :	(tempSubSystemClassURI,isSensorOfPropertyURI,tempSystemClassURI)
			Individual sensInstance = op.getInstanceForURI(tempSensorInstanceURI);
			Individual partInstance = op.getInstanceForURI(tempPartInstanceURI);
			ObjectProperty isSensorOfObjectProperty = op.getObjectPropertyForURI(isSensorOfPropertyURI);
			
			sensInstance.addProperty(isSensorOfObjectProperty, partInstance);
			
		}
		
	}

	@Override
	public void propertyMeasuredByRelationEncoding() {

//		for(Map.Entry<String, String> entry : ontoproc.getPropertyClassNameToSensorClassNameMap().entrySet()) {
//			String tempSubSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getKey());
//			String tempSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getValue());
//			String propertyMeasuredByPropertyURI = "";
//			
//			// use these URIs for assertions :	(tempSubSystemClassURI,propertyMeasuredByPropertyURI,tempSystemClassURI)
//		}
		for(Map.Entry<String, String> entry : op.getPropertyClassNameToSensorClassNameMap().entrySet()) {
			String tempSubSystemClassURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey());
			String tempSystemClassURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue());
			String propertyMeasuredByPropertyURI = "";
			
			// use these URIs for assertions :	(tempSubSystemClassURI,propertyMeasuredByPropertyURI,tempSystemClassURI)
		}
		
	}
	
	private boolean checkSensorAvailabilityFor(String propertyName) {
		String measuringSensor = op.getPropertyClassNameToSensorClassNameMap().get(propertyName);
		String installedSensor = op.getTBoxClassNameToIndividualURIMap().get(measuringSensor);
		
		if(installedSensor == null) {
			System.out.println("Sensor Information not available for property" + propertyName);
			return false;
		}
		else {
			return true;
		}
	}

	
//	public static void main(String args[]) {
//		RDFEncoderImpl re = RDFEncoderImpl.getInstance();
//		re.initOntologyProcess();
//		re.initFileProcess(filepath);
//	}
}
