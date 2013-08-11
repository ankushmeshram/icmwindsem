package org.icmwind.core;

import java.util.Date;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;


public interface Madness {
	
	public void init();
	
	public void loadTBox();
	
	public void processTBox();
	
	public OWLOntology createABox(String onotlogyIRI);
	
	public void saveRDFXMLOntology(OWLOntology ontology, String filepath);
	
	public void addAxioms(OWLOntology ontology, Set<OWLAxiom> axioms);
	
	public OWLClass getClassFor(String className);
	
	public OWLObjectProperty getObjectPropertyFor(String objPropName);
	
	public OWLDataProperty getDataPropertyFor(String dataPropName);
	
	public OWLNamedIndividual createtNamedIndividualFor(String individualName);
	
	public OWLNamedIndividual createtNamedIndividualForURI(String individualURI);
	
	public OWLAxiom createClassAssertion(OWLClass clazz, OWLIndividual individual);
	
	public OWLAxiom createObjectPropertyAssertion(OWLObjectProperty objectProperty, OWLIndividual individual1,
			OWLIndividual individual2);
	
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty, OWLIndividual individual, float value);
	
	public OWLAxiom createDataPropertyAssertion(OWLDataProperty dataProperty, OWLIndividual individual, Date date);
	
	public OWLLiteral createFloatLiteral(String value);
	
	public int axiomCountFor(OWLOntology ontology);

}
