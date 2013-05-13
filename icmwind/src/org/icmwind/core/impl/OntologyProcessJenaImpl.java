package org.icmwind.core.impl;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.Normalization;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;



public class OntologyProcessJenaImpl {

	private static OntologyProcessJenaImpl instance = new OntologyProcessJenaImpl();
	private static OntModel tboxModel = null;
	private static OntModel aboxModel = null;

	private List<String> listClassNames = new ArrayList<String>();
	private List<String> listNormClassNames = new ArrayList<String>();
	private Map<String, String> mapNormClassNamesToClassNames = new HashMap<String, String>();
	
	private Map<String, String> mapClassNameToURI = new HashMap<String, String> ();
	private Map<String, String> mapABoxURIToFileLocation = new HashMap<String, String>();
	private Map<String, String> mapSensorClassNameToPartsClassName =  new HashMap<String, String>();
	private Map<String, String> mapPartClassNameToSystemClassName = new HashMap<String, String>();
	private Map<String, String> mapPropertyClassNameToSensorClassName = new HashMap<String, String>();
	private Map<String, String> mapClassNameToIndividualURI = new HashMap<String, String>();
	
	final String tboxUri = "http://www.icmwind.com/WindTurbineOnto.owl";
	static String tboxFile = null; //"file:" + ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getCoreOntologyPath();
	
	final String NS = tboxUri + "#";
	
	Logger logger = Logger.getLogger(OntologyProcessJenaImpl.class); 
	
	private OntologyProcessJenaImpl() {
	}
	
	public static OntologyProcessJenaImpl getInstance() {
		if(!ICMWindConfig.isInitialised())
			ICMWindConfig.init();
		
		tboxFile = "file:" + ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getCoreOntologyPath();
		
		tboxModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		return instance;
	}
	
	private void populate() {
		System.out.println("OntologyProcessJenaImpl.populate()");
		
		//TODO use   
		mapPartClassNameToSystemClassName.put("Cooler", "Wind_Turbine");
		mapPartClassNameToSystemClassName.put("Gearbox", "Wind_Turbine" );
		mapPartClassNameToSystemClassName.put("Oil_Pump", "Wind_Turbine");
		mapPartClassNameToSystemClassName.put("Rotor_Hub", "Wind_Turbine");
		mapPartClassNameToSystemClassName.put("Thermo_Bypass_Valve", "Wind_Turbine");

		//TODO use sensor2system.config to populate mapSensorClassNameToPartsClassName
		mapSensorClassNameToPartsClassName.put("HYDAC_Lab","Gearbox");
		mapSensorClassNameToPartsClassName.put("CS_1000","Gearbox");
		mapSensorClassNameToPartsClassName.put("MCS_1000","Gearbox");
		mapSensorClassNameToPartsClassName.put("Pressure_Sensor","Gearbox");

		//TODO use property2sensor.config to populate mapPropertyClassNameToSensorClassName
		mapPropertyClassNameToSensorClassName.put("CS_Drive", "CS_1000" );
		mapPropertyClassNameToSensorClassName.put("CS_Flow", "CS_1000" );
		mapPropertyClassNameToSensorClassName.put("CSM_ON", "CS_1000" );
		mapPropertyClassNameToSensorClassName.put("ISO_14", "CS_1000" );
		mapPropertyClassNameToSensorClassName.put("ISO_6", "CS_1000" );
		mapPropertyClassNameToSensorClassName.put("ISO_4", "CS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_Temp", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_A", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_B", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_C", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_D", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_E", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("MCS_F", "MCS_1000" );
		mapPropertyClassNameToSensorClassName.put("P_In", "Pressure_Sensor" );
		mapPropertyClassNameToSensorClassName.put("HLB_DK", "HYDAC_Lab");
		mapPropertyClassNameToSensorClassName.put("HLB_S", "HYDAC_Lab");
		mapPropertyClassNameToSensorClassName.put("HLB_Temp", "HYDAC_Lab");

	}
	
	
	public void init() {
		System.out.println("OntologyProcessJenaImpl.init()");
		
		loadTbox();
		normalizeClassNames();
		populate();
	}
	
	private void loadTbox() {
		System.out.println("OntologyProcessJenaImpl.loadTbox()");
				
		tboxModel.getDocumentManager().addAltEntry(tboxUri, tboxFile);
		tboxModel.read(tboxUri);
			
		ExtendedIterator<OntClass> extIt = tboxModel.listNamedClasses();
		while(extIt.hasNext()) {
			String classUri = extIt.next().getURI();
			String className = classUri.substring( classUri.indexOf("#") + 1 );
			
			listClassNames.add(className);
			mapClassNameToURI.put(className, classUri);
		}
	}
	
//	public OntModel createAbox() {
////		if(aboxModel != null)
////			aboxModel.close();
//		
//		aboxModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//		aboxModel.addSubModel(tboxModel);
//		aboxModel.setNsPrefix("wto", tboxUri);
//		
//		return aboxModel;
//	}
	
	public void createAbox() {
		if(aboxModel != null)
			aboxModel.reset();
//			aboxModel.close();
		
		aboxModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		aboxModel.addSubModel(tboxModel);
		aboxModel.setNsPrefix("wto", NS);
	}
	
	public boolean saveAboxToFile(String aboxUri, String aboxFile) {
		if(aboxModel == null) {
			return false;
		} else{
			PrintWriter writer;
			try {
				writer = new PrintWriter(aboxFile);
				aboxModel.write(writer, "RDF/XML", aboxUri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
			
			aboxModel.reset();
//			aboxModel.close();
			mapABoxURIToFileLocation.put(aboxUri, aboxFile);
			return true;
		}
	}
	
	private void normalizeClassNames() {
		Normalization normalization = new Normalization();
		listNormClassNames = normalization.normalizeList(listClassNames);
		mapNormClassNamesToClassNames = normalization.getNormalizationMap();
	}
	
	/**
	 * @return the listClassNames
	 */
	public List<String> getClassNamesList() {
		return listClassNames;
	}

	/**
	 * @return the listNormClassNames
	 */
	public List<String> getNormClassNamesList() {
		return listNormClassNames;
	}
	
	public String getClassNameFor(String normClassName) {
		return mapNormClassNamesToClassNames.get(normClassName);
	}
	
	public String getClassURIFor(String className) {
		return mapClassNameToURI.get(className);
	}
	
	/**
	 * @return the mapABoxURIToFileLocation
	 */
	public Map<String, String> getABoxURIToFilePathMap() {
		return mapABoxURIToFileLocation;
	}
	
	public void setTBoxClassNameToIndividualURIMap(Map<String, String> classNameToIndividualURIMap) {
		mapClassNameToIndividualURI =  classNameToIndividualURIMap;
	}
	
	public Map<String, String> getTBoxClassNameToIndividualURIMap() {
		return mapClassNameToIndividualURI;
	}
	
	public Map<String, String> getPartClassNameToSystemClassNameMap() {
		return mapPartClassNameToSystemClassName;
	}
	
	public Map<String, String> getSensorClassNameToPartClassNameMap() {
		return mapSensorClassNameToPartsClassName;
	}

	public Map<String, String> getPropertyClassNameToSensorClassNameMap() {
		return mapPropertyClassNameToSensorClassName;
	}

	/**
	 * @return the nS
	 */
	public String getNS() {
		return NS;
	}

	public OntClass getClassForURI(String classUri) {
//		return aboxModel.getOntClass(classUri);
		OntClass tempOntClass = tboxModel.getOntClass(classUri);
		return tempOntClass;
	}
	
	public Individual createInstance(OntClass clas, String instanceUri) {
		return aboxModel.createIndividual(instanceUri, clas);
	}
	
	public Individual getInstanceForURI(String instanceUri) {
		System.out.println("Get Instance for URI : " + instanceUri);
		
		return aboxModel.getIndividual(instanceUri);
	}
	
	public ObjectProperty getObjectPropertyForURI(String objectPropertyUri) {
//		return aboxModel.getObjectProperty(objectPropertyUri);
		return tboxModel.getObjectProperty(objectPropertyUri);
	}
	
	public DatatypeProperty getDataPropertyForURI(String dataPropertyUri) {
//		return aboxModel.getDatatypeProperty(dataPropertyUri);
		return tboxModel.getDatatypeProperty(dataPropertyUri);
	}
	
	public Literal createTimeLiteral(Date d) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(d);
		
		return aboxModel.createTypedLiteral(cal);
	}
	
	public Literal createValueLiteral(float value) {
		return aboxModel.createTypedLiteral(value);
		
	}
	
	public OntClass getClass(String className) {
		return tboxModel.getOntClass(mapClassNameToURI.get(className));
	}

//	public static void main(String[] args) {
//		OntologyProcessJenaImpl opji = OntologyProcessJenaImpl.getInstance();
//		opji.init();
//	}

}
 