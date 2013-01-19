package org.icmwind.owlapi.examples;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

public class LoadOntology {

	/**
	 * @param args
	 * 
	 * 1. Create an IRI: IRI.create("")
	 * 2. Connect it to File: IRIMapper(iri,iri.create(file))
	 * 3. Load ontology using same iri: loadOntology(iri)
	 * 4. Print data.
	 * 
	 */
	public static void main(String[] args) {

		try {

			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			
			IRI ontologyIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
						
			File file = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
			
			manager.addIRIMapper(new SimpleIRIMapper(ontologyIri,IRI.create(file)));
			
//			IRI ontologyIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
			OWLOntology localOntology = manager.loadOntology(ontologyIri);
			System.out.println("Loaded ontology: " + localOntology);
			System.out.println("  from: " + manager.getOntologyDocumentIRI(localOntology));
			System.out.println(manager.getOntology(ontologyIri).getClassesInSignature().size());
			
//			manager.removeOntology(localOntology);
//			
//			IRI mappedIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
//			OWLOntology mappedOntology = manager.loadOntology(mappedIri);
//			System.out.println("Loaded ontology: " + mappedOntology);
//			System.out.println("  from: " + manager.getOntologyDocumentIRI(mappedOntology));
			
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

	}

}
