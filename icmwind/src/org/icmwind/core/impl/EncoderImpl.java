package org.icmwind.core.impl;

import java.io.File;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.icmwind.core.FileProcess;
import org.icmwind.core.impl.EncoderImpl.OntologyInformation;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.Normalization;
import org.icmwind.util.SimilarityUtility;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLOntologyMerger;

import foo.TestSpin;


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
	
	// Location to store the encoded files (ABoxes)
	private static String storeEncodedDataAt;
	
	// URI of core Ontology
	private static final String coreOntologyIri = ICMWindConfig.getOntologyURI();
	
	// Path of core Ontology
	private static final String coreOntologyPath = ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getCoreOntologyPath();
	
	// Namespace of ontology
	private static final String NS = coreOntologyIri + "#";
	
	private static final String DATASTORE_FOLDER = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store/";
																										
	Logger logger = Logger.getLogger(EncoderImpl.class);
	
	private PrintWriter logWriter = null;
	
	static int count = 0;
	
	LinkedList<OntologyInformation> aboxes = new LinkedList<OntologyInformation>();
	
	OWLClass fcmClass = null;
	OWLNamedIndividual fcmInstance = null;
	
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
		
		String aboxTimeStamp = null;
		if(startAnalysisPeriod.equals(endAnalysisPeriod)) {
			aboxTimeStamp = sdf.format(startAnalysisPeriod).subSequence(0, 10).toString();
		} else {
			aboxTimeStamp = sdf.format(startAnalysisPeriod).subSequence(0, 10).toString() 
								+ "-" + sdf.format(endAnalysisPeriod).subSequence(0, 10).toString();
		}
		
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
						beginDate = fileproc.readRecord("Zeit").substring(0, 10); System.gc(); System.gc();
					}
					
					OWLClass 		tempObsClass = op.getClassFor("Observation"); 
					OWLNamedIndividual	tempObservationInstance = op.createtNamedIndividualFor("Observation_" + rowsRead); 
					axioms.add(op.createClassAssertion(tempObsClass, tempObservationInstance)); 
							
					
					// Read headers acc to final headerToClassNameMap
					for(Map.Entry<String, String> entry :  headerToOntoClassNameMap.entrySet()) {
						
						String headerName = entry.getKey();
						String propertyName = entry.getValue();
						
						System.out.println("Reading header - " + headerName + " : class - " + propertyName);
						
						// If Sensor measuring a particular property class name is present in existing scenario, only then do this
						if(checkSensorAvailabilityFor(propertyName)) {
							
							// Get mapped Class for Header and create its instance 
							OWLClass tempClass = op.getClassFor(propertyName); 
							OWLNamedIndividual tempInstance = op.createtNamedIndividualFor(propertyName + "_" + rowsRead);
							axioms.add(op.createClassAssertion(tempClass, tempInstance));
							
							// Define Cooler_Temperature_Difference and its hasObservation Observation
							OWLNamedIndividual tempCTDInstance = op.createtNamedIndividualFor("CTD_" + rowsRead); 
							axioms.add(op.createClassAssertion(op.getClassFor("Cooler_Temperature_Difference"), tempCTDInstance));
							
							
							if(headerName.equals("Zeit")) {
								
								System.out.println("This is ZEIt.......................................................................................");
								
								// Time--observedTime--""^^xsd:dateTime
								OWLDataProperty observedTimeDataProperty = op.getDataPropertyFor("observedTime");
								axioms.add(op.createDataPropertyAssertion(observedTimeDataProperty, tempInstance,temp ));
																
								// Observation--hasSamplingTime--Time
								OWLObjectProperty hasSamplingTimeObjectProperty = op.getObjectPropertyFor("hasSamplingTime"); 
								axioms.add(op.createObjectPropertyAssertion(hasSamplingTimeObjectProperty, tempObservationInstance, tempInstance));
								
							} else {
															
								// Property--hasValue--""^^xsd:Float
								OWLDataProperty hasValueDataProperty = op.getDataPropertyFor("hasValue");
								String value = fileproc.readRecord(headerName);
								value = value.equals("NaN") ? "0.0" : value;
								axioms.add(op.createDataPropertyAssertion(hasValueDataProperty, tempInstance, Float.parseFloat(value)));
								
								// Property--hasObservation--Observation
								OWLObjectProperty hasObservationObjectProperty = op.getObjectPropertyFor("hasObservation"); 
								axioms.add(op.createObjectPropertyAssertion(hasObservationObjectProperty, tempInstance, tempObservationInstance)); 
							
								// Cooler_Temperature_Difference--hasObservation--Observation
								axioms.add(op.createObjectPropertyAssertion(hasObservationObjectProperty, tempCTDInstance, tempObservationInstance));
								
								// Sensor--measuresProperty--Property
								String sensorClassName = op.getPropertyClassNameToSensorClassNameMap().get(propertyName);
								String sensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(sensorClassName);
								
								OWLNamedIndividual tempSensorInstance = op.createtNamedIndividualForURI(sensorInstanceURI);
								axioms.add(op.createClassAssertion(op.getClassFor(sensorClassName),tempSensorInstance));
								
								OWLObjectProperty measuresPropertyObjectProperty = op.getObjectPropertyFor("observes");
								axioms.add(op.createObjectPropertyAssertion(measuresPropertyObjectProperty, tempSensorInstance, tempInstance));
								
							}
						} // close checkSensorAvailabilityFor()
					}// finish reading a row
					
					System.out.println("******Observations read : " + rowsRead);
										
					rowsRead++;
					
					
				} else if(temp.equals(endAnalysisPeriod)) {
					endDate = fileproc.readRecord("Zeit").substring(0, 10);

					String timestamp = beginDate + "-" + endDate; 
					
					String aboxUri = "http://www.icmwind.com/instances/iwo-databox" + "-" + timestamp + ".owl";
					String aboxFile = DATASTORE_FOLDER + "ontologies/iwo-databox-" + timestamp + ".owl";
					
					System.out.println("** End of Records.");
					System.out.println("** Saving file please wait......");
					
					OWLOntology tempOntology = op.createABox(aboxUri);
					op.addAxioms(tempOntology, axioms);
					op.saveRDFXMLOntology(tempOntology, aboxFile);
					
					aboxes.add(new OntologyInformation(aboxUri, aboxFile, tempOntology));
					
					count = count + tempOntology.getAxiomCount();
					
					System.out.println("Axiom Count : " + count);
					System.out.println("File saved at " + aboxFile);
					
					axioms.clear(); System.gc(); System.gc();
				}
				
				
				// Write ABox to a File
				if( (rowsRead % 1000) == 0 ) {
					endDate = fileproc.readRecord("Zeit").substring(0, 10);

					String timestamp = beginDate + "-" + endDate; 
					
					String aboxUri = "http://www.icmwind.com/instances/iwo-databox" + "-" + timestamp + ".owl";
					String aboxFile = DATASTORE_FOLDER + "ontologies/iwo-databox-" + timestamp + ".owl";
					
					System.out.println("** More records...");
					System.out.println("** Saving file please wait......");
					
					OWLOntology tempOntology = op.createABox(aboxUri);
					op.addAxioms(tempOntology, axioms);
					op.saveRDFXMLOntology(tempOntology, aboxFile);
					
					aboxes.add(new OntologyInformation(aboxUri, aboxFile, tempOntology));
					
					count = count + tempOntology.getAxiomCount();

					System.out.println("Axiom Count : " + count);
					System.out.println("File saved at " + aboxFile);
					
					axioms.clear();	System.gc(); System.gc();
					reset = true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} // all the rows have been read
	
		fileproc.closeFile();
		
		// TODO Get AboxURI-FileLoc Map 
		System.out.println("@@@ABox URI - File Path : " + op.getABoxURIToFilePathMap().toString());
		
		long axiomcount = count;
		System.out.println("Totals number of axioms : " + axiomcount);
		
		long encodingtime = TimeManagement.get().elapsedTime() / 1000000;
		System.out.println("Total time taken for RDFication : " + encodingtime + " millis" );
		
		logWriter.println();
		logWriter.println("Total Observations: " + ( rowsRead - 1) );
		logWriter.println("Total Axioms: " + axiomcount);
		logWriter.println("Total Encoding Time: " + encodingtime + " ms");
		logWriter.println("ABoxes created: ");
		
		for(OntologyInformation oi : aboxes) {
			logWriter.println("- URI: " + oi.getURI());
			logWriter.println("- Path: " + oi.getFilePath());
			logWriter.println("- Axioms: " + oi.getAxiomCount());
			logWriter.println();
		}
				
		logWriter.flush();

		String aboxOntologyIRI = "http://www.icmwind.com/instances/iwo-abox-" + aboxTimeStamp + ".owl"; 
		String aboxOntologyPath = DATASTORE_FOLDER + "ontologies/iwo-abox-" + aboxTimeStamp + ".owl";
		
		OntologyMerger om = new OntologyMerger(aboxes, aboxOntologyIRI, aboxOntologyPath, logWriter);
		om.merge();
		
		TestSpin spinInf = new TestSpin(aboxOntologyIRI, aboxOntologyPath, logWriter);
		spinInf.spin();

		List<OntologyInformation> mrgOnts = new ArrayList<OntologyInformation>();
		mrgOnts.add(new OntologyInformation(coreOntologyIri, coreOntologyPath, null));
		mrgOnts.add(new OntologyInformation(aboxOntologyIRI, aboxOntologyPath, null));
		mrgOnts.add(new OntologyInformation(spinInf.getInfOntURI(), spinInf.getInfOntPath(), null));
		
		String finalOntologyIRI = "http://www.icmwind.com/instances/iwo-" + aboxTimeStamp + ".owl"; 
		String finalOntologyPath = DATASTORE_FOLDER + "ontologies/iwo-" + aboxTimeStamp + ".owl";
		
		OntologyMerger merger = new OntologyMerger(mrgOnts, finalOntologyIRI, finalOntologyPath, logWriter);
		merger.merge();
		
		logWriter.close();
		
		return true;
	}

	public Map<String, String> getFileSummary() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", fileproc.getFileName());
		map.put("begin", sdf.format(fileproc.getBeginDate()));
		map.put("end", sdf.format(fileproc.getEndDate()));
		
		logWriter.println();
		logWriter.println("Uploaded File: " + fileproc.getFileName());
		logWriter.flush();
		
		return map;
	}

	public void setAnalysisPeriod(Date beginAnalysisPeriod, Date endAnalysisPeriod) {
		EncoderImpl.startAnalysisPeriod = beginAnalysisPeriod;
		EncoderImpl.endAnalysisPeriod = endAnalysisPeriod;
		
		logWriter.println();
		logWriter.println("Selected Analysis Period");
		logWriter.println("- From: " + startAnalysisPeriod.toString());
		logWriter.println("- To: " + endAnalysisPeriod.toString());
		logWriter.flush();
	}

	public void setDataFileSourceInfo(Map<String, String> infoMap) {
		System.out.println("**RDFEncoderImpl.setFileSourceInfo(info_map) : info_map contains info for creating Conceptual ABox.");
		System.out.println("**RDFEncoderImpl.setFileSourceInfo(info_map) : Passing intialising LogWriter.");
		
		logWriter = ICMWindConfig.getEncodingLogWriter();
		
		String wt = null;
		
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
		
		fcmClass = op.getClassFor("Fluid_Condition_Monitoring");
		OWLNamedIndividual gbInstance = null;
				
		for(Map.Entry<String, List<String>> entry : simMap.entrySet()) {
			
			if(entry.getKey().toLowerCase().equals("name")) {
				// create Wind Turbine instance from the value of the key
				String wtClassName = "Wind_Turbine";
							
				// Get OWL Class from class name
//				System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
				OWLClass tempClass = op.getClassFor(wtClassName);
								
				String tempKeyName = mapNormKeyToKey.get(entry.getKey().trim());
				String wtInstncName = infoMap.get(tempKeyName);
				OWLNamedIndividual wtInstance = op.createtNamedIndividualFor(wtInstncName.trim());
				
				wt = wtInstncName;
				
				// Individual URI
				String tempInstncURI = wtInstance.getIRI().toString();
				System.out.println("@ " + tempInstncURI);
				
				// add to ClassName-IndividualURI map
				mapClassToIndividualName.put(wtClassName, tempInstncURI);
				
				// Create OWL Individual using OWL Class & Individual URI
//				op.createInstance(tempClass, tempInstncURI); triple++;
				axioms.add(op.createClassAssertion(tempClass, wtInstance));
				
				// Add information about FCM-FCM instance
				fcmInstance = op.createtNamedIndividualFor("fcm_" + wtInstance.getIRI().getFragment());
				axioms.add(op.createClassAssertion(fcmClass, fcmInstance));
				
				System.out.println("**EncoderImpl.setDataSourceInfo() : FCM instance - " + fcmInstance.getIRI().getFragment());
			}
								
			// if no matching found
			if( !entry.getValue().isEmpty() ) {
				if(entry.getKey().toLowerCase().equals("location")) {
					continue;
				} else  {
					
					if((entry.getKey().toLowerCase().equals("gearbox"))) {
						// get class name from matched norm class name, get its URI
						String gbClassName =  op.getClassNameFor(entry.getValue().get(0));
												
						// Get OWL Class from class name
//							System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
						OWLClass gbClass = op.getClassFor(gbClassName);
							
						// get key name from norm key name, get its value, add NS to value
						String tempKeyName = mapNormKeyToKey.get(entry.getKey().trim());
						String gbInstncName = infoMap.get(tempKeyName);
						gbInstance = op.createtNamedIndividualFor(gbInstncName.trim());
							
						// Individual URI
						String tempInstncURI = gbInstance.getIRI().toString();
						System.out.println("@ " + tempInstncURI);
							
						// add to ClassName-IndividualURI map
						mapClassToIndividualName.put(gbClassName, tempInstncURI);
							
						// Create OWL Individual using OWL Class & Individual URI
						axioms.add(op.createClassAssertion(gbClass, gbInstance));

					} else {
					
						// get class name from matched norm class name, get its URI
						String tempClassName =  op.getClassNameFor(entry.getValue().get(0));
										
						// Get OWL Class from class name
//					System.out.println("^ " + ontoproc.getClassURIFor(tempClassName).toStringID());
						OWLClass tempClass = op.getClassFor(tempClassName);
					
						// get key name from norm key name, get its value, add NS to value
						String tempKeyName = mapNormKeyToKey.get(entry.getKey().trim());
						String tempInstncName = infoMap.get(tempKeyName);
						OWLNamedIndividual tempInstance = op.createtNamedIndividualFor(tempInstncName.trim());
					
						// Individual URI
						String tempInstncURI = tempInstance.getIRI().toString();
						System.out.println("@ " + tempInstncURI);
					
						// add to ClassName-IndividualURI map
						mapClassToIndividualName.put(tempClassName, tempInstncURI);
					
						// Create OWL Individual using OWL Class & Individual URI
						axioms.add(op.createClassAssertion(tempClass, tempInstance));
					}
					
				} 
				
			}
		}
		
		if(gbInstance != null && fcmInstance != null) {
			// Add Fluid_Condition_Monitoring-installedAt-Gearbox Object Property encoding
			OWLObjectProperty installedAtObjectProperty = op.getObjectPropertyFor("installedAt");
			System.out.println(installedAtObjectProperty.getIRI().toString());
			axioms.add(op.createObjectPropertyAssertion(installedAtObjectProperty, fcmInstance, gbInstance));
				
			System.out.println("------**EncoderImpl.setDataSourceInfo() : installedAt Property - " + fcmInstance.getIRI().getFragment() + ", " + gbInstance.getIRI().getFragment());
		}
		
		
		// set the classname-inidividual mappings
		op.setTBoxClassNameToIndividualURIMap(mapClassToIndividualName);
		
		// start "partOf" relations creation
		isPartOfRelationEncoding(axioms);
		
		// start "consistsOf" relations creation
		consistsOfRelationEncoding(axioms);
		
//		// start "isSensorOf" relations creation
//		isSensorOfRelationEncoding(axioms);
		
		
		String aboxUri = "http://www.icmwind.com/instances/iwo-infobox.owl";
		String aboxFile = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store/ontologies/iwo-infobox.owl";
				
		OWLOntology tempOntology = op.createABox(aboxUri);
		op.addAxioms(tempOntology, axioms);
		op.saveRDFXMLOntology(tempOntology, aboxFile);
		
		aboxes.add(new OntologyInformation(aboxUri, aboxFile, tempOntology));
		
		count = count + tempOntology.getAxiomCount();
		
		System.out.println("Axiom Count : " + count);
		System.out.println("File saved at " + aboxFile);
		
//		logWriter.println();
//		logWriter.println("ABox for Information File of Wind Turbine " + wt);
//		logWriter.println("- URI: " + aboxUri);
//		logWriter.println("- Path: " + aboxFile);
//		logWriter.println("- Axiom Count: " + op.axiomCountFor(tempOntology));
//		logWriter.flush();
		
	}

	public void isPartOfRelationEncoding(Set<OWLAxiom> axioms) {
		System.out.println("\n**RDFEncoderImpl.isPartOfRelationEncoding() : encode Component-isPartOf-System relation");
				
		for(Map.Entry<String, String> entry : op.getPartClassNameToSystemClassNameMap().entrySet()) {
			System.out.println("Part - " + entry.getKey().toString() + " : System - " + entry.getValue().toString());
//			
//			System.out.println( "SubSystem class : " + op.getClass(entry.getKey().toString()).getURI());
//			System.out.println( "System class : " + op.getClass(entry.getValue().toString()).getURI());

			String tempSubSystemInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey().trim());
			String tempSystemInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue().trim());
				
			// Check whether there exists Instance for it in Map getTBoxClassNameToIndividualURIMap().
			// if not, alert user about it : else, encode
			if(tempSubSystemInstanceURI != null && tempSystemInstanceURI != null) {
					
				// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
				OWLClass subSysClass = op.getClassFor(entry.getKey().toString().trim());
				OWLNamedIndividual subSysInstance = op.createtNamedIndividualForURI(tempSubSystemInstanceURI);
				axioms.add(op.createClassAssertion(subSysClass, subSysInstance));
				
				OWLClass sysClass = op.getClassFor(entry.getValue().toString().trim());
				OWLNamedIndividual sysInstance = op.createtNamedIndividualForURI(tempSystemInstanceURI);
				axioms.add(op.createClassAssertion(sysClass, sysInstance));
				
				System.out.println(subSysInstance + "--" +sysInstance );
				
				OWLObjectProperty isPartOfObjectProperty = op.getObjectPropertyFor("isPartOf");
				axioms.add(op.createObjectPropertyAssertion(isPartOfObjectProperty, subSysInstance, sysInstance));
						
			} else {
				if(tempSubSystemInstanceURI == null)
					System.out.println("**ERROR : Info file doesn't have information about Sub System - " + entry.getKey().toString());
				else
					System.out.println("**ERROR : Info file doesn't have information about System - " + entry.getValue().toString());
			}
		}
	}

	public void consistsOfRelationEncoding(Set<OWLAxiom> axioms) {
		System.out.println("\n**EncoderImpl.consistsOfRelationEncoding() : encode FCM-consistsOf-Sensor relation");

		for(Map.Entry<String, String> entry : op.getSensorClassNameToPartClassNameMap().entrySet()) {
			System.out.println("Sensor - " + entry.getKey().toString());// + " : Part - " + entry.getValue().toString());
				
			String tempSensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey());
				
			// Check whether there exists Instance for it in Map getTBoxClassNameToIndividualURIMap().
			// if not, alert user about it : else, encode
			if(tempSensorInstanceURI != null && fcmInstance != null) {
				// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
				OWLClass sensClass = op.getClassFor(entry.getKey().toString().trim());
				OWLNamedIndividual sensInstance = op.createtNamedIndividualForURI(tempSensorInstanceURI);
				axioms.add(op.createClassAssertion(sensClass, sensInstance));
					
				System.out.println(fcmInstance + "--" + sensInstance );
					
				// Add FCM-consistsOf-Sensor Object property encoding
				OWLObjectProperty consistsOfObjectProperty = op.getObjectPropertyFor("consistsOf");
				axioms.add(op.createObjectPropertyAssertion(consistsOfObjectProperty, fcmInstance , sensInstance));
				
				System.out.println("**EncoderImpl.setDataSourceInfo() : consistsOf Property - " + fcmInstance.getIRI().getFragment() + ", " + sensInstance.getIRI().getFragment());
					
			} else {
				if(tempSensorInstanceURI == null)
					System.out.println("**ERROR : Info file doesn't have information about Sensor - " + entry.getKey().toString());
				else
					System.out.println("**ERROR : Info file doesn't have information about FCM System.");
			}
		}
	}
	
//	public void isSensorOfRelationEncoding(Set<OWLAxiom> axioms) {
//		System.out.println("\n**RDFEncoderImpl.isPartOfRelationEncoding() : encode Sensor-isSensorOf-Sub_System relation");
//
//		for(Map.Entry<String, String> entry : op.getSensorClassNameToPartClassNameMap().entrySet()) {
//			System.out.println("Sensor - " + entry.getKey().toString() + " : Part - " + entry.getValue().toString());
//				
//			String tempSensorInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getKey());
//			String tempPartInstanceURI = op.getTBoxClassNameToIndividualURIMap().get(entry.getValue());
//				
//			// Check whether there exists Instance for it in Map getTBoxClassNameToIndividualURIMap().
//			// if not, alert user about it : else, encode
//			if(tempSensorInstanceURI != null && tempPartInstanceURI != null) {
//			
//				// use these URIs for assertions :	(tempSubSystemClassURI,isPartOfPropertyURI,tempSystemClassURI)
//				OWLClass sensClass = op.getClassFor(entry.getKey().toString().trim());
//				OWLNamedIndividual sensInstance = op.createtNamedIndividualForURI(tempSensorInstanceURI);
//				axioms.add(op.createClassAssertion(sensClass, sensInstance));
//					
//				OWLClass partClass = op.getClassFor(entry.getValue().toString().trim());
//				OWLNamedIndividual partInstance = op.createtNamedIndividualForURI(tempPartInstanceURI);
//				axioms.add(op.createClassAssertion(partClass, partInstance));
//					
//				System.out.println(sensInstance + "--" + partInstance );
//					
//				OWLObjectProperty isSensorOfObjectProperty = op.getObjectPropertyFor("isSensorOf");
//				axioms.add(op.createObjectPropertyAssertion(isSensorOfObjectProperty, sensInstance, partInstance));
//					
//			} else {
//				if(tempSensorInstanceURI == null)
//					System.out.println("**ERROR : Info file doesn't have information about Sensor - " + entry.getKey().toString());
//				else
//					System.out.println("**ERROR : Info file doesn't have information about Sub System - " + entry.getValue().toString());
//			}
//		}
//	}
//	
	
	
		
		
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
			System.out.println("**------------------TimeManagement.startTime() : Timer has started. ");
		}
		
		public long elapsedTime() {
			elapsedTime = System.nanoTime() - startTime;
			startTime = 0;
			System.out.println("**------------------TimeManagement.startTime() : Timer has stopped and reset. ");
			return elapsedTime;
		}
		
	}

	class OntologyInformation {
		
		private String ontUri = null;
		private String ontFile = null;
		private OWLOntology ont = null;
		
		public OntologyInformation(String uri, String filepath, OWLOntology ontology) {
			this.ontUri = uri;
			this.ontFile = filepath;
			this.ont = ontology;
		}
		
		public String getURI() {
			return ontUri;
		}
		
		public String getFilePath() {
			return ontFile;
		}
		
		public int getAxiomCount() {
			return ont.getAxiomCount();
		}
		
	}
		
	class OntologyMerger {
		OWLOntologyManager man = OWLManager.createOWLOntologyManager();
		String mergedOntologyIRI = null;
		String mergedOntologyPath = null;
		List<OntologyInformation> ontologies =  null;
		
//		String coreOntoIRI = ICMWindConfig.getOntologyURI();
//		String coreDocIRI = ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getCoreOntologyPath();
		
		PrintWriter logger = null;
				
		public OntologyMerger(List<OntologyInformation> ontologies, String ontologyIRI, String ontologyPath, PrintWriter logWriter) {
			this.ontologies = ontologies;
			this.mergedOntologyIRI = ontologyIRI;
			this.mergedOntologyPath = ontologyPath;
			this.logger = logWriter;
		}
		
		public void merge() {
			
			TimeManagement.get().startTime();
			
			IRI ontoIRI = null;
			IRI docIRI = null;
			
			logger.println();
			logger.println("Merge Ontologies: ");

			int i = 1;
			
			for(OntologyInformation oi : ontologies) {
				
				logger.println("- Ontology " + i + ": " + oi.getURI());
				
				ontoIRI = IRI.create(oi.getURI());
				docIRI = IRI.create("file:///" + oi.getFilePath().replaceAll(" ","%20"));
				
				try {
					man.loadOntologyFromOntologyDocument(docIRI);
				} catch (OWLOntologyCreationException e) {
					e.printStackTrace();
				}
				
				i++;
			}

			OWLOntologyMerger merger = new OWLOntologyMerger(man);
			OWLOntology mergedOntology = null;
			try {
				logger.println("Merging...");
				mergedOntology = merger.createMergedOntology(man, IRI.create(mergedOntologyIRI));
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
			
			try {
				logger.println("Saving...");
				man.saveOntology(mergedOntology, new RDFXMLOntologyFormat(), IRI.create( new File(mergedOntologyPath)) );
				
				long mergingtime = TimeManagement.get().elapsedTime() / 1000000;
				System.out.println("Total time taken for Merging : " + mergingtime + " millis" );

				OntologyInformation oi = new OntologyInformation(mergedOntologyIRI, mergedOntologyPath, mergedOntology);
				logger.println();
				logger.println("Merged Ontology Information. " );
				logger.println("- URI: " + oi.getURI());
				logger.println("- Path: " + oi.getFilePath());
				logger.println("- Axiom Count: " + oi.getAxiomCount());
				logger.println("- Total Merging Time: " + mergingtime + "ms");
				
			} catch (OWLOntologyStorageException e) {
				e.printStackTrace();
			}
									
			logger.flush();
			
			System.out.println("**OntologyMerger.merge() : Files successfully merged and saved at " + mergedOntologyPath);
			
		}
		
	}
		
}




