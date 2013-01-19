package org.icmwind.owlapi.examples;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.csvreader.CsvReader;

public class OntologyInfo {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, IOException {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		IRI ontologyIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
		File file = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOnto.owl");
		manager.addIRIMapper(new SimpleIRIMapper(ontologyIri,IRI.create(file)));
		
		OWLOntology localOntology = manager.loadOntology(ontologyIri);
		
		Set<OWLClass> classes = new HashSet<OWLClass> ();
		Set<String> ontClass = new HashSet<String>() ;
		classes = localOntology.getClassesInSignature();
		for(OWLClass aClass : classes){
			ontClass.add(aClass.getIRI().getFragment());
		}
		
		CsvReader reader = new CsvReader(new FileReader("E:/ICM-Wind/Code/icmwind/data_files/TestData.csv"), ';');
		Set<String> dataClass = new HashSet<String>();
		reader.readHeaders();
		String [] headers = reader.getHeaders();
		for(String data : headers) {
			dataClass.add(data.replace(' ' , '_'));
		}
		
		for(String classData : dataClass){
			if(ontClass.contains(classData)){
				System.out.println(classData);
			}
			else {
				System.out.println(classData + " not present in Ontology");
			}
		}
		
		
		

	}

}
