package org.icmwind.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

public class TestOWLAPI {
	
	private static String tboxUri= "http://www.icmwind.com/icmwindontology.owl";
	private static File tboxFile = new File("C:/Users/anme05/Dropbox/Thesis/Ontology/icmwindontology.owl");
	private static String NS = tboxUri + "#";
	
	IRI ontoIri 	= IRI.create(tboxUri);
	IRI docIri = IRI.create(tboxFile); 
	
	private static String aboxUri= "http://www.icmwind.com/instances/iwo-abox.owl";
	private static File aboxFile = new File("iwo-abox.owl");
	private static String aNS = aboxUri + "#";
	
	private OWLOntologyManager manager = null;
	private OWLDataFactory factory = null;
	public OWLOntology tboxOntology = null;
	
	private void init() {
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
			
		manager.addIRIMapper(new SimpleIRIMapper(ontoIri, docIri));
	}
	
	private void loadTbox() {
		try {
			tboxOntology = manager.loadOntology(ontoIri);
			System.out.println("Axiom Count : " + tboxOntology.getAxiomCount());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	}
	
	private OWLClass getClassFor(String classname) {
		return factory.getOWLClass(IRI.create(NS + classname));
	}
	
	private OWLNamedIndividual createNamedIndividualFor(String instance_name) {
		return factory.getOWLNamedIndividual(IRI.create(NS + instance_name));
	}
	
	private OWLAxiom createClassAssertion(OWLClass clazz, OWLNamedIndividual individual) {
		return factory.getOWLClassAssertionAxiom(clazz, individual);
	}
	
	private OWLOntology createABoxOntology(String ontologyURI) {
		try {
			return manager.createOntology(IRI.create(ontologyURI));
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void addAxioms(OWLOntology onotlogy, Set<OWLAxiom> axioms) {
		manager.addAxioms(onotlogy, axioms);
	}
	
	private void saveRDFXMLOntology(OWLOntology ontology, String filepath ) {
		try {
			manager.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create(new File(filepath)));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}
	}
	
	private OWLDataFactory getFactory() {
		return factory;
	}
	
	private void saveToStream(OWLOntology ontology) {
		try {
			manager.saveOntology(ontology, System.out);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws ParseException {
		TestOWLAPI test = new TestOWLAPI();
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		
		test.init();
		test.loadTbox();
		OWLClass obs = test.getClassFor("Observation");
		OWLNamedIndividual indi = test.createNamedIndividualFor("Observation_1");
		axioms.add(test.createClassAssertion(obs, indi));
		
		OWLClass time = test.getClassFor("Time");
		OWLNamedIndividual indi2 = test.createNamedIndividualFor("Time_1");
		axioms.add(test.createClassAssertion(time, indi2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date date = sdf.parse("12.09.2008 00:13:00");
		
				
		System.out.println(sdf.format(date).toString());
		
		OWLDataProperty observedTime = test.getFactory().getOWLDataProperty(IRI.create("http://www.icmwind.com/icmwindontology.owl#observedTime"));
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(date);
		OWLLiteral literal = test.getFactory().getOWLLiteral(DatatypeConverter.printDateTime(cal), OWL2Datatype.XSD_DATE_TIME);
		OWLDataPropertyAssertionAxiom dpa = test.getFactory().getOWLDataPropertyAssertionAxiom(observedTime, indi2, literal);
		axioms.add(dpa);
		
		OWLOntology abox = test.createABoxOntology("http://www.icmwind.com/instances/iwo-abox.owl");		
		test.addAxioms(abox, axioms);
		
		OWLClass sensor = test.getClassFor("Sensor");
		System.out.println(sensor.getIRI());
		System.out.println(sensor.getSuperClasses(test.tboxOntology).toString());
		
		Set<OWLObjectProperty> OP = sensor.getObjectPropertiesInSignature();
		for(OWLObjectProperty o : OP)
			System.out.println(o.getIRI());
		

//		test.saveToStream(abox);
		
//		test.saveRDFXMLOntology(abox, "iwo-aboxw.owl");
		
		
		
		
	}

	
}
