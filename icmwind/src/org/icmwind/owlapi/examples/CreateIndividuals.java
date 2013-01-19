package org.icmwind.owlapi.examples;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class CreateIndividuals {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLDataFactory factory = manager.getOWLDataFactory();

			File file = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOntoChanged.owl");
			
			manager.addIRIMapper(new SimpleIRIMapper(IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl"),IRI.create(file)));

			IRI ontoIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");

			OWLOntology ontology = manager.loadOntology(ontoIri);
			
			PrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#");
			
			OWLClass sensor = factory.getOWLClass(":Sensor", pm);
			
			System.out.println(sensor.getReferencingAxioms(ontology));
				
			OWLNamedIndividual sensor1 = factory.getOWLNamedIndividual(IRI.create(pm.getDefaultPrefix() + "sensor1"));
			
			OWLClassAssertionAxiom sensorInstances = factory.getOWLClassAssertionAxiom(sensor, sensor1);
			
			manager.addAxiom(ontology, sensorInstances);
			
			System.out.println("Sensor class: " + sensor.toString());
			System.out.println("Sensor object: " + sensor1.toString());
			
//			manager.saveOntology(ontology,IRI.create(newFile.toURI()));
			
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

}
