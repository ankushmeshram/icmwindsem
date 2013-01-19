package org.icmwind.owlapi.examples;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;


public class Entities {

	/**
	 * @param args
	 * @throws OWLException 
	 */
	public static void main(String[] args) throws OWLException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		OWLDataFactory factory =  manager.getOWLDataFactory();
		
		File loadFile = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
		IRI ontoIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
		manager.addIRIMapper(new SimpleIRIMapper(ontoIri, IRI.create(loadFile)));
		
		PrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#");
		OWLClass sensor = factory.getOWLClass(":Sensor", pm);
		
		System.out.println("Sensor class " + sensor.toString());
		
		

	}

}
