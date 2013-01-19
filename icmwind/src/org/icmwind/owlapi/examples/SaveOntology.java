package org.icmwind.owlapi.examples;

import java.io.File;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.io.SystemOutDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;


public class SaveOntology {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

			File loadFile = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(loadFile);
			
			File saveFile = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOntoCopy.owl");
			manager.saveOntology(ontology, IRI.create(saveFile.toURI()));
			
			OWLOntologyFormat format = manager.getOntologyFormat(ontology);
			System.out.println("Ontology format: " + format);
			
			OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
			if(format.isPrefixOWLOntologyFormat()) {
				owlxmlFormat.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
			}
			manager.saveOntology(ontology,owlxmlFormat,IRI.create(saveFile.toURI()));
			
			ManchesterOWLSyntaxOntologyFormat manSyntaxFormat = new ManchesterOWLSyntaxOntologyFormat();
			if(format.isPrefixOWLOntologyFormat()) {
				manSyntaxFormat.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
			}
			manager.saveOntology(ontology, manSyntaxFormat, new SystemOutDocumentTarget() );
		
		} catch (OWLException e) {
			
			e.printStackTrace();
		}

	}

}
