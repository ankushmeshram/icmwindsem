package org.icmwind.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;


/**
 * @author anme05
 *
 */
public interface OntologyProcess {
	
	/**
	 * @param filepath Path of Core Ontology
	 * @return true if Success 
	 */
	public boolean openFile(String filepath);
	
	public void openOntologyFromFile(String filePath);
	
	public void openOntologyFromIri(IRI ontologyIri);
	
	/**
	 * @param ontology Ontology to be saved.
	 * @param outputFile Path of file to save the ontology. 
	 */
	public void saveOntologyRDFXML(OWLOntology ontology,String outputFile);
	
	public OWLOntology getOntology();
	
	public IRI getOntologyIRI();
	
	public Set<OWLClass> getClasses();
	
	/**
	 * @return List of Class names in Core Ontology
	 */
	public List<String> getClassNamesList();
	
	/**
	 * @return List of normalized Class names in Core Ontology 
	 */
	public List<String> getNormClassNamesList();
	
	/**
	 * @param normClassName Normalized Class Name for which Class Name is requested
	 * @return Class Name mapped to normClassName
	 */
	public String getClassNameFor(String normClassName);
	
	/**
	 * @return Map<String, String> containing key-value pair "normalized_class_name-class_name"
	 */
	public Map<String, String> getNormalizationMap();
	
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
