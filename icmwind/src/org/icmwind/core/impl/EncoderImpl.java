package org.icmwind.core.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.midi.SysexMessage;

import org.apache.log4j.Logger;
import org.icmwind.core.FileProcess;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.Normalization;
import org.icmwind.util.SimilarityUtility;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;





public class EncoderImpl {

	// Single instance for RDFENcoder Interface
	private static EncoderImpl INSTANCE = new EncoderImpl();
	
	// Singleton instances for Ontology and File processing
//	private static OntologyProcess ontoproc = OntologyProcessImpl.getInstance();
	private static OntologyProcessMadnessImpl op = new OntologyProcessMadnessImpl();// OntologyProcessJenaImpl.getInstance();
	private static FileProcess fileproc = FileProcessImpl.getInstance();
	
	// Lists to store Class names and normalized Class names too
	@SuppressWarnings("unused")
	private static List<String> ontoclasseslist; //TODO Check this.
	private static List<String> normontoclasseslist;
	
	// Lists to store Header names and normalized Header names too
	private static List<String> headerslist;
	private static List<String> normheaderslist;
	
	// Start and End Dates between which the data needs to be encoded
	private static Date startAnalysisPeriod = null;
	private static Date endAnalysisPeriod = null;
	
	// MatchOrSuggest Map to store matches for Header names of the datasheet files in Class names of Core Ontology : <String headername. List classname>  
	private static Map<String, List<String>> matchOrSuggestMap = new HashMap<String, List<String>>();
	
	// Map to store exact mapping of Header Name to Class Name
	private static Map<String, String> headerToOntoClassNameMap = new HashMap<String, String>();
	
	
	// Boolean to check whether file process started successfully and mapping is completed or not
	private static boolean isInitialised = false;
	
	// Boolean to check whether encoded file storage folder passed exists or not
	private static boolean existsStorageFolder = false;
	
	// Lcation to store the encoded files (ABoxes)
	private static String storeEncodedDataAt;
	
	// URI of core Ontology
	private static final String coreOntologyIri = ICMWindConfig.getOntologyURI();
	
	// Namespace of ontology
	private static final String NS = coreOntologyIri + "#";
	
	private static final String DATASTORE_FOLDER = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store/";
	
	Logger logger = Logger.getLogger(EncoderImpl.class);
	
	private double triple = 0;
	
//	private static AbstractStringMetric similaritymetric = new CosineSimilarity();
	
	private EncoderImpl()
	{
		System.out.println("**RDFEncoderImpl() : RDFEncoder Instance");
		// Get resource paths from the ICMWindConfig.config 
		ICMWindConfig.init();

	}
	
	public static EncoderImpl getInstance() {
		return INSTANCE;
	}
	
	public void initOntologyProcess()
	{
		// Open  core Ontology from from Core Ontology resource path 
//		ontoproc.openFile(ICMWindConfig.getCoreOntologyPath());
		op.init();
		
		// get List of normalized class names of Core Ontology
//		normclasseslist = ontoproc.getNormClassNamesList();
		normontoclasseslist = op.getNormClassNamesList();
	}

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
			for(String normclass : SimilarityUtility.getSimilarTextList(normheader, normontoclasseslist)) {
//				templist.add(ontoproc.getClassNameFor(normclass));
				templist.add(op.getClassNameFor(normclass));
			}
			matchOrSuggestMap.put(fileproc.getColumnNameFor(normheader), templist);
		}
		
		isInitialised = true;
	}
	
	
	// Get MatchOrSuggest Map
	public Map<String, List<String>> getMoSMap() {
		if(isInitialised)
			return matchOrSuggestMap;
		else
			return Collections.emptyMap();
	}

	
	// Set HeaderToClass Map
	public boolean setHeaderToClassNamesMap(Map<String, String> head2ClassNameMap) {
		headerToOntoClassNameMap = head2ClassNameMap;
		return true;
	}
	
	// Set the location to store the encoded files
	public void setEncodeStorage(String folderPath) {
		File encodeStorageFolder = new File(folderPath);

		//if path doesnt exist..create one
		if(!encodeStorageFolder.exists())
			encodeStorageFolder.mkdirs();
		
		storeEncodedDataAt = folderPath;
		
		existsStorageFolder = true;
	}

	public boolean encode() {
		TimeManagement.get().startTime();

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
		
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
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
					}
					
					OWLClass 		tempObsClass = op.getClassFor("Observation"); triple++;
					OWLIndividual	tempObservationInstance = op.createtNamedIndividualFor("Observation_" + rowsRead); triple++;
					axioms.add(op.createClassAssertion(tempObsClass, tempObservationInstance)); triple++;
							
					
					// Read headers acc to final headerToClassNameMap
					for(Map.Entry<String, String> entry :  headerToOntoClassNameMap.entrySet()) {
						
						String headerName = entry.getKey();
						String propertyName = entry.getValue();
						
						System.out.println("Reading header - " + headerName + " : class - " + propertyName);
						
						// If Sensor measuring a particular property class name is present in existing scenario, only then do this
						if(checkSensorAvailabilityFor(propertyName)) {
							
							// Get mapped Class for Header and create its instance 
							OWLClass tempClass = op.getClassFor(propertyName); triple++;
							OWLIndividual tempInstance = op.createtNamedIndividualFor(propertyName + "_" + rowsRead); triple++;
							axioms.add(op.createClassAssertion(tempClass, tempInstance));
							
							// Define Cooler_Temperature_Difference and its hasObservation Observation
							OWLIndividual tempCTDInstance = op.createtNamedIndividualFor("CTD_" + rowsRead); triple++;
							axioms.add(op.createClassAssertion(op.getClassFor("Cooler_Temperature_Difference"), tempCTDInstance));
							
							
							if(headerName.equals("Zeit")) {
								
								System.out.println("This is ZEIt.......................................................................................");
								
								// Time--observedTime--""^^xsd:dateTime
								OWLDataProperty observedTimeDataProperty = op.getDataPropertyFor("observedTime");
								axioms.add(op.createDataPropertyAssertion(observedTimeDataProperty, tempInstance,temp ));
																
								// Observation--hasSamplingTime--Time
								OWLObjectProperty hasSamplingTimeObjectProperty = op.getObjectPropertyFor("hasSamplingTime"); triple++;
								axioms.add(op.createObjectPropertyAssertion(hasSamplingTimeObjectProperty, tempObservationInstance, tempInstance));
								
							} else {
															
								// Property--hasValue--""^^xsd:Float
								OWLDataProperty hasValueDataProperty = op.getDataPropertyFor("hasValue");
								String value = fileproc.readRecord(headerName);
								axioms.add(op.createDataPropertyAssertion(hasValueDataProperty, tempInstance, Float.parseFloat(value)));
								
								// Property--hasObservation--Observation
								OWLObjectProperty hasObservationObjectProperty = op.getObjectPropertyFor("hasObservation"); triple++;
								axioms.add(op.createObjectPropertyAssertion(hasObservationObjectProperty, tempInstance, tempObservationInstance)); triple++;
							
								// Cooler_Temperature_Difference--hasObservation--Observation
								axioms.add(op.createObjectPropertyAssertion(hasObservationObjectProperty, tempCTDInstance, tempObservationInstance)); triple++;
								
								// Sensor--measuresProperty--Property
								String sensorClassName = op.getPropertyClassNameToSensorClassNameMap().get(propertyName);
								String sensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(sensorClassName);
								
								OWLIndividual tempSensorInstance = op.createtNamedIndividualFor(sensorInstanceURI);
								axioms.add(op.createClassAssertion(op.getClassFor(sensorClassName),tempSensorInstance));
								
								OWLObjectProperty measuresPropertyObjectProperty = op.getObjectPropertyFor("measuresProperty"); triple++;
								axioms.add(op.createObjectPropertyAssertion(measuresPropertyObjectProperty, tempSensorInstance, tempInstance)); triple++;
								
							}
						} // close checkSensorAvailabilityFor()
					}// finish reading a row
					
					System.out.println("******Observations read : " + rowsRead);
										
					rowsRead++;
					
					
				} else if(temp.equals(endAnalysisPeriod)) {
					endDate = fileproc.readRecord("Zeit").substring(0, 10);

					String timestamp = beginDate + "-" + endDate; 
					
					String aboxUri = "http://www.icmwind.com/instances/IWO" + "-" + timestamp + ".owl";
					String aboxFile = DATASTORE_FOLDER + "ontologies/iwo-abox-" + timestamp + ".owl";
					
					System.out.println("** Saving file please wait......");
					
					OWLOntology tempOntology = op.createABox(aboxUri);
					op.addAxioms(tempOntology, axioms);
					op.saveRDFXMLOntology(tempOntology, aboxFile);
					
					System.out.println("** End of Records.");
				}
				
				
				// Write ABox to a File
				if( (rowsRead % 10000) == 0 ) {
					endDate = fileproc.readRecord("Zeit").substring(0, 10);

					String timestamp = beginDate + "-" + endDate; 
					
					String aboxUri = "http://www.icmwind.com/instances/IWO" + "-" + timestamp + ".owl";
					String aboxFile = DATASTORE_FOLDER + "ontologies/iwo-abox-" + timestamp + ".owl";
					
					System.out.println("** Saving file please wait......");
					
					OWLOntology tempOntology = op.createABox(aboxUri);
					op.addAxioms(tempOntology, axioms);
					op.saveRDFXMLOntology(tempOntology, aboxFile);
					
					
					System.out.println("File saved at " + aboxFile);
					System.out.println("** More records...");
					
					axioms.clear();				
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
		
		// TODO Get AboxURI-FileLoc Map 
		System.out.println("@@@ABox URI - File Path : " + op.getABoxURIToFilePathMap().toString());
		System.out.println("Totals number of assertions for the seleceted time period : " + triple);
		System.out.println("Total time taken for RDFication : " + TimeManagement.get().elapsedTime() / 1000000 + " millis" );
		
		return true;
	}

	public Map<String, String> getFileSummary() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", fileproc.getFileName());
		map.put("begin", sdf.format(fileproc.getBeginDate()));
		map.put("end", sdf.format(fileproc.getEndDate()));
		
		return map;
	}

	public void setAnalysisPeriod(Date beginAnalysisPeriod, Date endAnalysisPeriod) {
		EncoderImpl.startAnalysisPeriod = beginAnalysisPeriod;
		EncoderImpl.endAnalysisPeriod = endAnalysisPeriod;
	}

	public void setDataFileSourceInfo(Map<String, String> infoMap) {
		System.out.println("**RDFEncoderImpl.setFileSourceInfo(info_map) : info_map contains info for creating Conceptual ABox.");

		String aboxUri = "http://www.icmwind.com/instance/iwo_abox.owl";
		String aboxFile = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store/ontologies/iwo_abox.owl";
		
		Normalization normalization = new Normalization();
		
		// map to store classname-individual mappings
		Map<String, String> mapClassToIndividualName = new HashMap<String, String>();
		
		// convert key set to key list, normalise key list, get normKeyMap
		List<String> normKeyList = normalization.normalizeList(new ArrayList<>(infoMap.keySet()));
		Map<String,String> mapNormKeyToKey = normalization.getNormalizationMap();
				
		// get normalised class names list i.e. normclasseslist
		System.out.println("Normalized Class Names list : " + normontoclasseslist.toString());
				
		// get similarity of normCNL, normKL as mapResult
		Map<String, List<String>> simMap = SimilarityUtility.getSimilarityBetweenLists(normKeyList, normontoclasseslist);
		
		
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		for(Map.Entry<String, List<String>> entry : simMap.entrySet()) {
			
			if(entry.getKey().toLowerCase().equals("name")) {
				// create Wind Turbine instance from the value of the key
				String tempClassName = "Wind_Turbine";
				// Get OntClass from URI
//				String tempClassURI = op.getClassURIFor(tempClassName);
//				OntClass tempClass = op.getClassForURI(tempClassURI);
//				OntClass tempClass = op.getClass(tempClassName);
				

				
				// Get OWL Class from class name
//				System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
				OWLClass tempClass = op.getClassFor(tempClassName);
				triple++;
				
				String tempKeyName = mapNormKeyToKey.get(entry.getKey().trim());
				String tempInstncName = infoMap.get(tempKeyName);
				OWLIndividual tempInstance = op.createtNamedIndividualFor(tempInstncName.trim());
								
				// Individual URI
				String tempInstncURI = tempInstance.toString();
				System.out.println("@ " + tempInstncURI);
				
				// add to ClassName-IndividualURI map
				mapClassToIndividualName.put(tempClassName, tempInstncURI);
				
				// Create OWL Individual using OWL Class & Individual URI
//				op.createInstance(tempClass, tempInstncURI); triple++;
				axioms.add(op.createClassAssertion(tempClass, tempInstance));
				
				
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
//					String tempClassURI = op.getClassURIFor(tempClassName);
//					OntClass tempClass = op.getClassForURI(tempClassURI);
									
					OWLClass tempClass = op.getClassFor(tempClassName);
					triple++;
					
					// Get OWL Class from class name
//					System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
										
					// get key name from norm key name, get its value, add NS to value
					String tempKeyName = mapNormKeyToKey.get(entry.getKey().trim());
					String tempInstncName = infoMap.get(tempKeyName);
					OWLIndividual tempInstance = op.createtNamedIndividualFor(tempInstncName.trim());
					
					// Individual URI
					String tempInstncURI = tempInstance.toString();
					System.out.println("@ " + tempInstncURI);
					
					// add to ClassName-IndividualURI map
					mapClassToIndividualName.put(tempClassName, tempInstncURI);
					
					// Create OWL Individual using OWL Class & Individual URI
					axioms.add(op.createClassAssertion(tempClass, tempInstance));
				} 
				
			}
		}
		
		
		
		// set the classname-inidividual mappings
//		ontoproc.setClassNameToIndividualURIMap(map);
		op.setTBoxClassNameToIndividualURIMap(mapClassToIndividualName);
		
		// start "partOf" relations creation
//		isPartOfRelationEncoding();
		
		// start "isSensorOf" relations creation
//		isSensorOfRelationEncoding();
				
		OWLOntology tempOntology = op.createABox(aboxUri);
		op.addAxioms(tempOntology, axioms);
		op.saveRDFXMLOntology(tempOntology, aboxFile);
		
		System.out.println("File saved at " + aboxFile);
		

	}

	public void isPartOfRelationEncoding() {
		System.out.println("**RDFEncoderImpl.isPartOfRelationEncoding() : encode SubSystem-isPartOf-System relation");
		
//		for(Map.Entry<String, String> entry : ontoproc.getPartClassNameToSystemClassNameMap().entrySet()) {
//			String tempSubSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getKey());
//			String tempSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getValue());
//			String isPartOfPropertyURI = "";
//			
//			// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
//		}
		
		for(Map.Entry<String, String> entry : op.getPartClassNameToSystemClassNameMap().entrySet()) {
			System.out.println("Part - " + entry.getKey().toString() + " : System - " + entry.getValue().toString());
//			
//			System.out.println( "SubSystem class : " + op.getClass(entry.getKey().toString()).getURI());
//			System.out.println( "System class : " + op.getClass(entry.getValue().toString()).getURI());

				String tempSubSystemInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey().trim());
				String tempSystemInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue().trim());
				String isPartOfPropertyURI = NS + "isPartOf";
				
				// Check whether there exists Instance for it in Map getTBoxClassNameToIndividualURIMap().
				// if not, alert user about it : else, encode
				if(tempSubSystemInstanceURI != null && tempSystemInstanceURI != null) {
					
					// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
					Individual subSysInstance = op.createInstance(op.getClass(entry.getKey().toString().trim()), tempSubSystemInstanceURI); triple++; // op.getInstanceForURI(tempSubSystemInstanceURI);
					Individual sysInstance = op.createInstance(op.getClass(entry.getValue().toString().trim()), tempSystemInstanceURI); triple++;
					ObjectProperty isPartOfObjectProperty = op.createObjectPropertyForURI(isPartOfPropertyURI); triple++;  //op.getObjectPropertyForURI(isPartOfPropertyURI);
				
					System.out.println(subSysInstance.getURI() + "--" +sysInstance );
					
					subSysInstance.addProperty(isPartOfObjectProperty, sysInstance); triple++;
					
				} else {
					if(tempSubSystemInstanceURI == null)
						System.out.println("**ERROR : Info file doesn't have information about Sub System - " + entry.getKey().toString());
					else
						System.out.println("**ERROR : Info file doesn't have information about System - " + entry.getValue().toString());
				}
		}
	}

	public void isSensorOfRelationEncoding() {
		System.out.println("**RDFEncoderImpl.isPartOfRelationEncoding() : encode Sensor-isSensorOf-Sub_System relation");
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
				
				// Check whether there exists Instance for it in Map getTBoxClassNameToIndividualURIMap().
				// if not, alert user about it : else, encode
				if(tempSensorInstanceURI != null && tempPartInstanceURI != null) {
					
					// use these URIs for assertions :	(tempSubSystemClassURI,isSensorOfPropertyURI,tempSystemClassURI)
					Individual sensInstance = op.createInstance(op.getClass(entry.getKey().toString()), tempSensorInstanceURI); triple++; // op.getInstanceForURI(tempSensorInstanceURI);
					Individual partInstance = op.createInstance(op.getClass(entry.getValue().toString()), tempPartInstanceURI); triple++; // op.getInstanceForURI(tempPartInstanceURI);
					ObjectProperty isSensorOfObjectProperty = op.createObjectPropertyForURI(isSensorOfPropertyURI); triple++; // op.getObjectPropertyForURI(isSensorOfPropertyURI);
					
					sensInstance.addProperty(isSensorOfObjectProperty, partInstance); triple++;
					
				} else {
					if(tempSensorInstanceURI == null)
						System.out.println("**ERROR : Info file doesn't have information about Sensor - " + entry.getKey().toString());
					else
						System.out.println("**ERROR : Info file doesn't have information about Sub System - " + entry.getValue().toString());
				}
		}
	}
	
	private boolean checkSensorAvailabilityFor(String propertyName) {
		if(propertyName.equals("Time"))
			return true;
		
		String measuringSensor = op.getPropertyClassNameToSensorClassNameMap().get(propertyName);
		String installedSensor = op.getTBoxClassNameToIndividualURIMap().get(measuringSensor);
		
		if(installedSensor == null ) {
			System.out.println("Sensor Information not available for property" + propertyName);
			return false;
		}
		else {
			return true;
		}
	}

	// TODO REMOVE because it is UNUSED
//	public void propertyMeasuredByRelationEncoding() {
//		System.out.println("**RDFEncoderImpl.isPartOfRelationEncoding() : encode Property-propertyMeasuredBy-Sensor relation");
//
////		for(Map.Entry<String, String> entry : ontoproc.getPropertyClassNameToSensorClassNameMap().entrySet()) {
////			String tempSubSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getKey());
////			String tempSystemClassURI = ontoproc.getClassNameToIndividualURIMap().get(entry.getValue());
////			String propertyMeasuredByPropertyURI = "";
////			
////			// use these URIs for assertions :	(tempSubSystemClassURI,propertyMeasuredByPropertyURI,tempSystemClassURI)
////		}
//		for(Map.Entry<String, String> entry : op.getPropertyClassNameToSensorClassNameMap().entrySet()) {
//			String tempPropertyInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey());
//			String tempSensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue());
//			String propertyMeasuredByPropertyURI = NS + "propertyMeasuredBy";
//			
//			// Check whether there exists Instance for it in Map getTBoxClassNameToIndividualURIMap().
//			// if not, alert user about it : else, encode
//			if(tempPropertyInstanceURI != null && tempSensorInstanceURI != null) {
//				
//				// use these URIs for assertions :	(tempPropertyInstanceURI,propertyMeasuredByPropertyURI,tempSensorInstanceURI)
//				Individual propInstance = op.createInstance(op.getClass(entry.getKey().toString()), tempPropertyInstanceURI); // op.getInstanceForURI(tempSensorInstanceURI);
//				Individual sensInstance = op.createInstance(op.getClass(entry.getValue().toString()), tempSensorInstanceURI); // op.getInstanceForURI(tempPartInstanceURI);
//				ObjectProperty proertyMeasuredByObjectProperty = op.createObjectPropertyForURI(propertyMeasuredByPropertyURI); // op.getObjectPropertyForURI(isSensorOfPropertyURI);
//										
//				propInstance.addProperty(proertyMeasuredByObjectProperty, sensInstance);
//				
//			} else {
//				if(tempPropertyInstanceURI == null)
//					System.out.println("**ERROR : Info file doesn't have information about Property - " + entry.getKey().toString());
//				else
//					System.out.println("**ERROR : Info file doesn't have information about Sensor - " + entry.getValue().toString());
//			}
//		}
//	}
	
	

	
//	public static void main(String args[]) {
//		RDFEncoderImpl re = RDFEncoderImpl.getInstance();
//		re.initOntologyProcess();
//		re.initFileProcess(filepath);
//	}
	
	static class TimeManagement {
		
		private long startTime = 0;
		private long elapsedTime = 0;
		
		static TimeManagement tm = new TimeManagement();
		
		public TimeManagement() {
			startTime();
		}
		public static TimeManagement get() {
			return tm;
		}
		
		public void startTime() {
			startTime = System.nanoTime();
			System.out.println("**------------------TimeManagement.startTime() : Timer has start. ");
		}
		
		public long elapsedTime() {
			elapsedTime = System.nanoTime() - startTime;
			startTime = 0;
			System.out.println("**------------------TimeManagement.startTime() : Timer has stopped and reset. ");
			return elapsedTime;
		}
		
	}
}



