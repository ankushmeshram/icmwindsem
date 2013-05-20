/**
 * 
 */
package org.icmwind.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.icmwind.core.Madness;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.Normalization;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 * @author anme05
 *
 */
public class OntologyProcessMadnessImpl implements Madness {

	private OWLOntologyManager manager = null;
	private OWLDataFactory factory = null;
	private OWLOntology tboxOntology = null;
	
	private static String tboxUri=  null; 
	private static File tboxFile = null; 
	private static String NS = tboxUri + "#";
	
	IRI ontoIri = null;
	IRI docIri  = null;
	
	PrefixManager pm = null;
	
	private List<String> listClassNames = new ArrayList<String>();
	private List<String> listNormClassNames = new ArrayList<String>();
	private Map<String, String> mapNormClassNamesToClassNames = new HashMap<String, String>();
	
	private Map<String, String> mapABoxURIToFileLocation = new HashMap<String, String>();
	private Map<String, String> mapSensorClassNameToPartsClassName =  new HashMap<String, String>();
	private Map<String, String> mapPartClassNameToSystemClassName = new HashMap<String, String>();
	private Map<String, String> mapPropertyClassNameToSensorClassName = new HashMap<String, String>();
	private Map<String, String> mapClassNameToIndividualURI = new HashMap<String, String>();
	
	
	private static Properties prop = new Properties();
	
	/* (non-Javadoc)
	 * 
	 * @see org.icmwind.core.Madness#init()
	 */
	@Override
	public void init() {
		if(!ICMWindConfig.isInitialised())
			ICMWindConfig.init();
		
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		
		tboxUri	 = ICMWindConfig.getOntologyURI();
		tboxFile = new File("C:/Users/anme05/Dropbox/Thesis/Ontology/icmwindontology.owl");
		
		ontoIri = IRI.create(tboxUri);
		docIri  = IRI.create(tboxFile);
		
		pm = new DefaultPrefixManager(tboxUri + "#");
		
		manager.addIRIMapper(new SimpleIRIMapper(ontoIri, docIri));
		
		loadTBox();
		processTBox();
		populate();
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#loadTBox()
	 */
	@Override
	public void loadTBox() {
		try {
			tboxOntology = manager.loadOntology(ontoIri);
			System.out.println("Axiom Count : " + tboxOntology.getAxiomCount());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#processTBox()
	 */
	@Override
	public void processTBox() {
		for(OWLClass clazz : tboxOntology.getClassesInSignature()) {
			String classname = clazz.getIRI().getFragment();
			listClassNames.add(classname);
		}
		
		Normalization norm = new Normalization();
		listNormClassNames = norm.normalizeList(listClassNames);
		mapNormClassNamesToClassNames = norm.getNormalizationMap();
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

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#createABox(java.lang.String)
	 */
	@Override
	public OWLOntology createABox(String onotlogyIRI) {
		try {
			return manager.createOntology(IRI.create(onotlogyIRI));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace(); return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#saveRDFXMLOntology(org.semanticweb.owlapi.model.OWLOntology, java.lang.String)
	 */
	@Override
	public void saveRDFXMLOntology(OWLOntology ontology, String filepath) {
		try {
			manager.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(new File(filepath)));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addAxioms(OWLOntology ontology, Set<OWLAxiom> axioms) {
		manager.addAxioms(ontology, axioms);
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#getClassFor(java.lang.String)
	 */
	@Override
	public OWLClass getClassFor(String className) {
		OWLClass tempClas = factory.getOWLClass(className, pm);
		if (this.existsClass(tempClas.getIRI())) {
			return tempClas;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#getObjectProperty(java.lang.String)
	 */
	@Override
	public OWLObjectProperty getObjectPropertyFor(String objPropName) {
		OWLObjectProperty tempObjProp = factory.getOWLObjectProperty(objPropName, pm);
		if (this.existsObjectProperty(tempObjProp.getIRI())) {
			return tempObjProp;
		} else {
			return null;
		}		
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#getDataProperty(java.lang.String)
	 */
	@Override
	public OWLDataProperty getDataPropertyFor(String dataPropName) {
		OWLDataProperty tempDataProp = factory.getOWLDataProperty(dataPropName, pm);
		if (this.existsDataProperty(tempDataProp.getIRI())) {
			return tempDataProp;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.Madness#createtNamedIndividual(java.lang.String)
	 */
	@Override
	public OWLNamedIndividual createtNamedIndividualFor(String individualName) {
		OWLNamedIndividual tempInst = factory.getOWLNamedIndividual(individualName, pm);
		if (this.existsInstance(tempInst.getIRI())) {
			return tempInst;
		} else {
			return null;
		}
	}
	
	public List<String> getNormClassNamesList() {
		return listNormClassNames;
	}
	
	public String getClassNameFor(String normClassName) {
		return mapNormClassNamesToClassNames.get(normClassName);
	}
	
	public boolean existsClass(IRI classIri) {
		if (tboxOntology.containsClassInSignature(classIri)) {
			return true;
		} else {
			return false;
		}
	}


	public boolean existsInstance(IRI instanceIri) {
		if (tboxOntology.containsIndividualInSignature(instanceIri)) {
			return true;
		} else {
			return false;
		}
	}


	public boolean existsObjectProperty(IRI propertyIri) {
		if (tboxOntology.containsObjectPropertyInSignature(propertyIri)) {
			return true;
		} else {
			return false;
		}
	}


	public boolean existsDataProperty(IRI propertyIri) {
		if (tboxOntology.containsDataPropertyInSignature(propertyIri)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public OWLAxiom createClassAssertion(OWLClass clazz, OWLIndividual individual) {
		return factory.getOWLClassAssertionAxiom(clazz, individual);
	}

	@Override
	public OWLAxiom createObjectPropertyAssertion(
			OWLObjectProperty objectProperty, OWLIndividual individual1,
			OWLIndividual individual2) {
		return factory.getOWLObjectPropertyAssertionAxiom(objectProperty, individual1, individual2);
	}

	@Override
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,
			OWLIndividual individual, float value) {
		return factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, value);
	}

	@Override
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,
			OWLIndividual individual, Date date) {
		OWLLiteral literal = createDateTimeLiteral(date);
		return factory.getOWLDataPropertyAssertionAxiom(dataProperty, individual, literal);
	}

	@Override
	public OWLLiteral createFloatLiteral(String value) {
		OWLDatatype floatDatatype = factory.getFloatOWLDatatype();
		return factory.getOWLLiteral(value, floatDatatype);		
	}

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

	public OWLLiteral createDateTimeLiteral(Date date) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		
		OWLDatatype dateTimeDatatype = factory.getOWLDatatype(OWL2Datatype.XSD_DATE_TIME.getIRI());
        OWLLiteral literal = factory.getOWLLiteral(cal.getTime().toString(), dateTimeDatatype);
        
        return literal;
	}
	

}
