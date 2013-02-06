package org.icmwind.core.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.icmwind.core.ICMWindSetup;
import org.icmwind.core.OntologyProcess;
import org.icmwind.util.Normalization;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * @author anme05
 * 
 *         Singleton OntologyProcess implementation. Overrides methods to open
 *         Ontology from a given path, extract class names of Ontology and
 *         normalize them and many more..
 * 
 *         IMPORTANT: Check whether ICMWindSetup has been initialized or not.
 * 
 */
public class OntologyProcessImpl implements OntologyProcess {

	private static final OntologyProcessImpl INSTANCE = new OntologyProcessImpl();

	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private OWLDataFactory factory = manager.getOWLDataFactory();
	private OWLOntology ontology = null;
	private List<String> classNamesList = new ArrayList<String>();
	private List<String> normClassNamesList = new ArrayList<String>();
	Map<String, String> normClassNamesToClassNames = new HashMap<String, String>();
	boolean openSuccess = false;

	private OntologyProcessImpl() {
	}

	/**
	 * @return OntologyProcessImpl Singelton instance
	 */
	public static OntologyProcessImpl getInstance() {
		if (!ICMWindSetup.isInitialised())
			ICMWindSetup.init();

		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#openFile(java.lang.String)
	 * Open Ontology from the path provided. Extract Class Names list and normalize them to Normalized Class Names list.
	 * 
	 * Returns true if Success
	 */
	@Override
	public boolean openFile(String filepath) {
		try {
			this.ontology = manager.loadOntology(IRI
					.create(OntologyProcessImpl.class.getClassLoader()
							.getResource(ICMWindSetup.getCoreOntologyPath())));

			for (OWLClass aClass : this.getClasses())
				classNamesList.add(aClass.getIRI().getFragment());

			Normalization normalize = new Normalization();
			this.normClassNamesList = normalize.normalizeList(classNamesList);
			this.normClassNamesToClassNames = normalize.getNormalizationMap();
			
			openSuccess = true;
			return openSuccess;
		} catch (OWLOntologyCreationException | URISyntaxException e) {
			e.printStackTrace();
			openSuccess = false;
			return openSuccess;
		}
	}

//	@Override
//	public void openOntologyFromFile(String filePath) {
//		// System.out.println("Loading ontology from file " + filePath);
//		try {
//			ontology = manager.loadOntology(IRI.create(new File(filePath)));
//		} catch (OWLOntologyCreationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void openOntologyFromIri(IRI ontologyIri) {
//		try {
//			ontology = manager.loadOntology(ontologyIri);
//		} catch (OWLOntologyCreationException e) {
//			e.printStackTrace();
//		}
//	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#saveOntologyRDFXML(org.semanticweb.owlapi.model.OWLOntology, java.lang.String)
	 * Save Ontology (RDFied) to given path
	 * 
	 */
	@Override
	public void saveOntologyRDFXML(OWLOntology ontology, String outputFile) {
		try {
			manager.saveOntology(ontology, IRI.create(new File(outputFile)));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}

	@Override
	public OWLOntology getOntology() {
		return ontology;
	}

	@Override
	public IRI getOntologyIRI() {
		return ontology.getOntologyID().getOntologyIRI();
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#getClasses()
	 * 
	 * Returns OWLClasses of the Ontology 
	 */
	@Override
	public Set<OWLClass> getClasses() {
		return ontology.getClassesInSignature();
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#getClassNamesList()
	 * 
	 * Returns list of Class Names
	 */
	@Override
	public List<String> getClassNamesList() {
		if (openSuccess) {
			return classNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#getNormClassNamesList()
	 * 
	 * Returns list of Normalized Class Names
	 */
	@Override
	public List<String> getNormClassNamesList() {
		if (openSuccess) {
			return normClassNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#getNormalizationMap()
	 */
	@Override
	public Map<String, String> getNormalizationMap() {
		if(normClassNamesToClassNames.isEmpty())
			return Collections.emptyMap();
		else
			return normClassNamesToClassNames;
	}


	/* (non-Javadoc)
	 * @see org.icmwind.core.OntologyProcess#getClassNameFor(java.lang.String)
	 * 
	 * Returns Class Name for given Normalized Class Name
	 */
	@Override
	public String getClassNameFor(String normClassName) {
		if (openSuccess)
			if (normClassNamesList.contains(normClassName))
				return normClassNamesToClassNames.get(normClassName);
			else
				return "normClassName not found in system.";
		else
			return "ERROR: File not found.";

	}

	@Override
	public int getClassCount() {
		return getClasses().size();
	}

	@Override
	public OWLClass getClass(String className) {
		OWLClass tempClas = factory.getOWLClass(IRI.create(this
				.getOntologyIRI() + "#" + className));
		if (this.existsClass(tempClas.getIRI())) {
			return tempClas;
		} else {
			return null;
		}
	}

	@Override
	public OWLClass getClass(IRI classIri) {
		OWLClass tempClas = factory.getOWLClass(classIri);
		if (this.existsClass(tempClas.getIRI())) {
			return tempClas;
		} else {
			return null;
		}
	}

	@Override
	public OWLNamedIndividual getInstance(String instanceName) {
		OWLNamedIndividual tempInst = factory.getOWLNamedIndividual(IRI
				.create(this.getOntologyIRI() + "#" + instanceName));
		if (this.existsInstance(tempInst.getIRI())) {
			return tempInst;
		} else {
			return null;
		}
	}

	@Override
	public OWLNamedIndividual getInstance(IRI instanceIri) {
		OWLNamedIndividual tempInst = factory
				.getOWLNamedIndividual(instanceIri);
		if (this.existsInstance(tempInst.getIRI())) {
			return tempInst;
		} else {
			return null;
		}
	}

	@Override
	public OWLObjectProperty getObjectProperty(String propertyName) {
		OWLObjectProperty tempObjProp = factory.getOWLObjectProperty(IRI
				.create(this.getOntologyIRI() + "#" + propertyName));
		if (this.existsObjectProperty(tempObjProp.getIRI())) {
			return tempObjProp;
		} else {
			return null;
		}
	}

	@Override
	public OWLObjectProperty getObjectProperty(IRI propertyIri) {
		OWLObjectProperty tempObjProp = factory
				.getOWLObjectProperty(propertyIri);
		if (this.existsObjectProperty(tempObjProp.getIRI())) {
			return tempObjProp;
		} else {
			return null;
		}
	}

	@Override
	public OWLDataProperty getDataProperty(String propertyName) {
		OWLDataProperty tempDataProp = factory.getOWLDataProperty(IRI
				.create(this.getOntologyIRI() + "#" + propertyName));
		if (this.existsDataProperty(tempDataProp.getIRI())) {
			return tempDataProp;
		} else {
			return null;
		}
	}

	@Override
	public OWLDataProperty getDataProperty(IRI propertyIri) {
		OWLDataProperty tempDataProp = factory.getOWLDataProperty(propertyIri);
		if (this.existsDataProperty(tempDataProp.getIRI())) {
			return tempDataProp;
		} else {
			return null;
		}
	}

	@Override
	public boolean existsClass(IRI classIri) {
		if (this.getOntology().containsClassInSignature(classIri)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existsInstance(IRI instanceIri) {
		if (this.getOntology().containsIndividualInSignature(instanceIri)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existsObjectProperty(IRI propertyIri) {
		if (this.getOntology().containsObjectPropertyInSignature(propertyIri)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existsDataProperty(IRI propertyIri) {
		if (this.getOntology().containsDataPropertyInSignature(propertyIri)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void addAxiomSet(OWLOntology ontology, Set<OWLAxiom> axiomSet) {
		manager.addAxioms(ontology, axiomSet);
	}

	@Override
	public OWLNamedIndividual createInstance(String instanceName) {
		OWLNamedIndividual tempInst = this.getInstance(instanceName);
		if (tempInst != null) {
			return tempInst;
		} else {
			return factory.getOWLNamedIndividual(IRI.create(this
					.getOntologyIRI() + "#" + instanceName));
		}
	}

	@Override
	public OWLAxiom createClassAssertion(OWLClass classRef,
			OWLIndividual instanceRef) {
		return factory.getOWLClassAssertionAxiom(classRef, instanceRef);
	}

	@Override
	public OWLAxiom createObjectPropertyAssertion(
			OWLObjectProperty objectProperty, OWLIndividual instance1,
			OWLIndividual instance2) {
		return factory.getOWLObjectPropertyAssertionAxiom(objectProperty,
				instance1, instance2);
	}

	@Override
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,
			OWLIndividual instance, float value) {
		return factory.getOWLDataPropertyAssertionAxiom(dataProperty, instance,
				value);
	}

	@Override
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,
			OWLIndividual instance, String value) {
		return factory.getOWLDataPropertyAssertionAxiom(dataProperty, instance,
				value);
	}

	// private void normalize() {
	// // change them to lower case
	// for(String className : classNamesList) {
	// String normClassName = cleanText(className).toLowerCase();
	// normClassNamesList.add(normClassName);
	// normClassNamesToClassNames.put(normClassName, className);
	// }
	// }
	//
	// // remove unwanted chars "_"(underscore) to " "(whitespace)
	// private String cleanText(String text) { return text.replaceAll("_",
	// " ");}
	
//	//DEBUG
//	public static void main(String[] args) {
//		OntologyProcess op = OntologyProcessImpl.getInstance();
//		op.openFile(ICMWindSetup.getCoreOntologyPath());
//		for(Map.Entry<String, String> entry : op.getNormalizationMap().entrySet())
//			System.out.println(entry.getKey() + " -- " + entry.getValue());
//	}

	
}
