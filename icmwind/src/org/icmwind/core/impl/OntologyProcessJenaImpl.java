package org.icmwind.core.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;
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
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;



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
	
	final String tboxUri = ICMWindConfig.getOntologyURI();
	static String tboxFile = null; //"file:" + ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getCoreOntologyPath();
	
	final String NS = tboxUri + "#";
	
	private Logger logger = Logger.getLogger(OntologyProcessJenaImpl.class); 
	
	private static Properties prop = new Properties();
		
	private OntologyProcessJenaImpl() {
	}
	
	public static OntologyProcessJenaImpl getInstance() {
		if(!ICMWindConfig.isInitialised())
			ICMWindConfig.init();
		
		tboxFile = "file:" + ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getCoreOntologyPath();
		
		tboxModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		return instance;
	}
	
	public void init() {
		System.out.println("**OntologyProcessJenaImpl.init() : Initializing ");
				
		loadTbox();
		normalizeClassNames();
		populate();
	}
	
	private void populate() {
		System.out.println("**OntologyProcessJenaImpl.populate() : Populating concept name mappings maps.");
									
		// adding Sub_System Name - System Name mappings to Map mapPartClassNameToSystemClassName
		mapPartClassNameToSystemClassName.put("Cooler", "Wind_Turbine");
		mapPartClassNameToSystemClassName.put("Gearbox", "Wind_Turbine" );
		mapPartClassNameToSystemClassName.put("Oil_Pump", "Wind_Turbine");
		mapPartClassNameToSystemClassName.put("Rotor_Hub", "Wind_Turbine");
		mapPartClassNameToSystemClassName.put("Thermo_Bypass_Valve", "Wind_Turbine");
		
		// reading Sensor Name - Sub_System Name mapping from given config file path in ICMWindConfig
		// adding the mappings to Map mapSensorClassNameToPartsClassName 
		try {
			prop.load(new FileInputStream(ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getSen2SysConfigFilePath()));
			for(Map.Entry<Object, Object> entry : prop.entrySet()) {
				mapSensorClassNameToPartsClassName.put( (String)entry.getKey(), (String)entry.getValue() );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// reading Property Name - Sensor Name mapping from given config file path in ICMWindConfig
		// adding the mappings to Map mapPropertyClassNameToSensorClassName
		try {
			prop.load(new FileInputStream(ICMWindConfig.getResourceFolderPath() + ICMWindConfig.getProp2SenConfigFilePath()));
			for(Map.Entry<Object, Object> entry : prop.entrySet()) {
				mapPropertyClassNameToSensorClassName.put( (String)entry.getKey(), (String)entry.getValue() );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	private void loadTbox() {
		System.out.println("**OntologyProcessJenaImpl.loadTbox() : Loading TBox.");
				
		tboxModel.getDocumentManager().addAltEntry(tboxUri, tboxFile);
		tboxModel.read(tboxUri);
			
		ExtendedIterator<OntClass> extIt = tboxModel.listNamedClasses();
		while(extIt.hasNext()) {
			String classUri = extIt.next().getURI();
			String className = classUri.substring( classUri.indexOf("#") + 1 );
			
			//System.out.println(classUri);
			
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
		System.out.println("**OntologyProcessJenaImpl.createAbox() : Creating new ABox.");
		
		if(aboxModel != null)
			aboxModel.reset();
//			aboxModel.close();
		
		aboxModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		aboxModel.addSubModel(tboxModel);
		aboxModel.setNsPrefix("wto", NS);
	}
	
	public boolean saveAboxToFile(String aboxUri, String aboxFile) {
		System.out.println("**OntologyProcessJenaImpl.saveAboxToFile() : Saving ABox.");
		
		if(aboxModel == null) {
			return false;
		} else{
			PrintWriter writer;
//			RDFWriter writer = aboxModel.getWriter("RDF/XML");
//			writer.setProperty("xmlbase", ICMWindConfig.getOntologyURI());
			try {
				writer = new PrintWriter(aboxFile);
//				aboxModel.write(writer, "RDF/XML", aboxUri);
				aboxModel.write(writer, "RDF/XML-ABBREV", null);
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
		Individual individual = aboxModel.createIndividual(instanceUri, clas);
		individual.addProperty(RDF.type, OWL.NS + "NamedIndividual");
		return individual;

	}
	
	public Individual getInstanceForURI(String instanceUri) {
		System.out.println("Get Instance for URI : " + instanceUri);
		
		return aboxModel.getIndividual(instanceUri);
	}
	
	public ObjectProperty getObjectPropertyForURI(String objectPropertyUri) {
//		return aboxModel.getObjectProperty(objectPropertyUri);
		return tboxModel.getObjectProperty(objectPropertyUri);
	}
	
	public ObjectProperty createObjectPropertyForURI(String objectPropertyUri) {
		return aboxModel.createObjectProperty(objectPropertyUri);
	}
	
	public DatatypeProperty getDataPropertyForURI(String dataPropertyUri) {
//		return aboxModel.getDatatypeProperty(dataPropertyUri);
		return tboxModel.getDatatypeProperty(dataPropertyUri);
	}
	
	public DatatypeProperty createDataPropertyForURI(String dataPropertyUri) {
		return aboxModel.createDatatypeProperty(dataPropertyUri);
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
 