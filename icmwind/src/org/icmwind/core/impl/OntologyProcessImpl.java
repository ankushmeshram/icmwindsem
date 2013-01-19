package org.icmwind.core.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.icmwind.core.OntologyProcess;
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


public class OntologyProcessImpl implements OntologyProcess {

	private static final OntologyProcessImpl INSTANCE =  new OntologyProcessImpl();
	
	private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	private OWLDataFactory factory = manager.getOWLDataFactory();
	private OWLOntology ontology = null;
	private List<String> classNamesList = new ArrayList<String>();
	private List<String> normClassNamesList = new ArrayList<String>();
	Map<String,String> normClassNamesToClassNames = new HashMap<String, String>();
	boolean openSuccess = false;

	private OntologyProcessImpl() {}
	
	public static OntologyProcessImpl getInstance() { return INSTANCE; }
	
	private boolean setFile(String filepath) {
		try {
			this.ontology = manager.loadOntology(IRI.create(new File(filepath)));
			return true;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean openFile(String filepath) {
		if(setFile(filepath)) {
			for(OWLClass aClass : this.getClasses()) {
				classNamesList.add(aClass.getIRI().getFragment());
			}
			openSuccess = true;
			this.normalize();
			return openSuccess;
		} else {
			openSuccess = false;
			return openSuccess;
		}
	}
	
	@Override
	public void openOntologyFromFile(String filePath) throws OWLOntologyCreationException {
		//System.out.println("Loading ontology from file " + filePath);
		ontology = manager.loadOntology(IRI.create(new File(filePath)));
	}

	@Override
	public void openOntologyFromIri(IRI ontologyIri) throws OWLOntologyCreationException {
		ontology = manager.loadOntology(ontologyIri);
	}

	@Override
	public void saveOntologyRDFXML(OWLOntology ontology, String outputFile) throws OWLOntologyStorageException {
		manager.saveOntology(ontology, IRI.create(new File(outputFile)));
	}

	@Override
	public OWLOntology getOntology() {
		return ontology;
	}

	@Override
	public IRI getOntologyIRI() {
		return ontology.getOntologyID().getOntologyIRI();
	}

	@Override
	public Set<OWLClass> getClasses() {
		return ontology.getClassesInSignature();
	}
	
	@Override
	public List<String> getClassNamesList() {
		if(openSuccess) {
			return classNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}

	@Override
	public List<String> getNormClassNamesList() {
		if(openSuccess) {
			return normClassNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}	
	
	@Override
	public String getClassNameFor(String normClassName) {
		if(openSuccess)
			if(normClassNamesList.contains(normClassName))
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
		OWLClass tempClas = factory.getOWLClass(IRI.create(this.getOntologyIRI()+"#"+className));
		if(this.existsClass(tempClas.getIRI())) {
			return tempClas;
		} else {
			return null;			
		}
	}

	@Override
	public OWLClass getClass(IRI classIri) {
		OWLClass tempClas = factory.getOWLClass(classIri); 
		if(this.existsClass(tempClas.getIRI())) {
			return tempClas;
		} else {
			return null;			
		}
	}

	@Override
	public OWLNamedIndividual getInstance(String instanceName) {
		OWLNamedIndividual tempInst = factory.getOWLNamedIndividual(IRI.create(this.getOntologyIRI()+"#"+instanceName));
		if(this.existsInstance(tempInst.getIRI())){
			return tempInst;
		} else {
			return null;
		}
	}

	@Override
	public OWLNamedIndividual getInstance(IRI instanceIri) {
		OWLNamedIndividual tempInst = factory.getOWLNamedIndividual(instanceIri);
		if(this.existsInstance(tempInst.getIRI())){
			return tempInst;
		} else {
			return null;
		}
	}

	@Override
	public OWLObjectProperty getObjectProperty(String propertyName) {
		OWLObjectProperty tempObjProp = factory.getOWLObjectProperty(IRI.create(this.getOntologyIRI()+ "#" + propertyName));
		if(this.existsObjectProperty(tempObjProp.getIRI())){
			return tempObjProp;
		} else {
			return null;
		}
	}

	@Override
	public OWLObjectProperty getObjectProperty(IRI propertyIri) {
		OWLObjectProperty tempObjProp = factory.getOWLObjectProperty(propertyIri);
		if(this.existsObjectProperty(tempObjProp.getIRI())){
			return tempObjProp;
		} else {
			return null;
		} 
	}

	@Override
	public OWLDataProperty getDataProperty(String propertyName) {
		OWLDataProperty tempDataProp = factory.getOWLDataProperty(IRI.create(this.getOntologyIRI()+ "#" + propertyName)); 
		if(this.existsDataProperty(tempDataProp.getIRI())) {
			return tempDataProp;
		} else {
			return null;
		}
	}

	@Override
	public OWLDataProperty getDataProperty(IRI propertyIri) {
		OWLDataProperty tempDataProp = factory.getOWLDataProperty(propertyIri);
		if(this.existsDataProperty(tempDataProp.getIRI())) {
			return tempDataProp;
		} else {
			return null;
		} 
	}

	@Override
	public boolean existsClass(IRI classIri) {
		if(this.getOntology().containsClassInSignature(classIri)){
			return true;
		} else {
			return false;
		}
	}
	

	@Override
	public boolean existsInstance(IRI instanceIri) {
		if(this.getOntology().containsIndividualInSignature(instanceIri)){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existsObjectProperty(IRI propertyIri) {
		if(this.getOntology().containsObjectPropertyInSignature(propertyIri)){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existsDataProperty(IRI propertyIri) {
		if(this.getOntology().containsDataPropertyInSignature(propertyIri)){
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
		if(tempInst!=null){
			return tempInst;
		} else {
			return factory.getOWLNamedIndividual(IRI.create(this.getOntologyIRI()+"#"+instanceName));
		}
	}

	@Override
	public OWLAxiom createClassAssertion(OWLClass classRef, OWLIndividual instanceRef) {
		return factory.getOWLClassAssertionAxiom(classRef, instanceRef);
	}

	@Override
	public OWLAxiom createObjectPropertyAssertion(OWLObjectProperty objectProperty, OWLIndividual instance1, OWLIndividual instance2) {
		return factory.getOWLObjectPropertyAssertionAxiom(objectProperty, instance1, instance2);
	}

	@Override
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,OWLIndividual instance, float value) {
		return factory.getOWLDataPropertyAssertionAxiom(dataProperty, instance, value);
	}

	@Override
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,OWLIndividual instance, String value) {
		return factory.getOWLDataPropertyAssertionAxiom(dataProperty, instance, value);
	}

	private void normalize() {
		// change them to lower case
		for(String className : classNamesList) {
			String normClassName = cleanText(className).toLowerCase();
			normClassNamesList.add(normClassName);
			normClassNamesToClassNames.put(normClassName, className);
		}
	}
	
	// remove unwanted chars "_"(underscore) to " "(whitespace)
	private String cleanText(String text) { return text.replaceAll("_", " ");}


}
