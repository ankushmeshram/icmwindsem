package org.icmwind.core;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public interface OntologyProcess {
	
	public boolean openFile(String filepath);
	
	public void openOntologyFromFile(String filePath) throws OWLOntologyCreationException;
	
	public void openOntologyFromIri(IRI ontologyIri) throws OWLOntologyCreationException;
	
	public void saveOntologyRDFXML(OWLOntology ontology,String outputFile) throws OWLOntologyStorageException;
	
	public OWLOntology getOntology();
	
	public IRI getOntologyIRI();
	
	public Set<OWLClass> getClasses();
	
	public List<String> getClassNamesList();
	
	public List<String> getNormClassNamesList();
	
	public String getClassNameFor(String normClassName);
	
	public int getClassCount();
	
	public OWLClass getClass(String className);
	
	public OWLClass getClass(IRI classIri);
	
	public OWLIndividual getInstance(String instanceName);
	
	public OWLIndividual getInstance(IRI instanceIri);
	
	public OWLObjectProperty getObjectProperty(String propertyName);
	
	public OWLObjectProperty getObjectProperty(IRI propertyIri);
	
	public OWLDataProperty getDataProperty(String propertyName);
	
	public OWLDataProperty getDataProperty(IRI propertyIri);
	
	public boolean existsClass(IRI classIri);
	
	public boolean existsInstance(IRI instanceIri);
	
	public boolean existsObjectProperty(IRI propertyIri);
	
	public boolean existsDataProperty(IRI propertyIri);
	
	public OWLNamedIndividual createInstance(String instanceName);
	
	public OWLAxiom createClassAssertion(OWLClass classReference,OWLIndividual instance);
	
	public OWLAxiom createObjectPropertyAssertion(OWLObjectProperty objectProperty,OWLIndividual instance1, OWLIndividual instance2);
	
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,OWLIndividual instance, float value);
	
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty,OWLIndividual instance, String value);
		
	public void addAxiomSet(OWLOntology ontology, Set<OWLAxiom> axiomSet);
		
}
