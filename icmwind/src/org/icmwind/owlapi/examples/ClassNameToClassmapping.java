package org.icmwind.owlapi.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

import com.csvreader.CsvReader;

public class ClassNameToClassmapping {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/**
		 * COPIED FROM HeadersToClassNameMapping
		 */
		CsvReader reader = new CsvReader(new FileReader("E:/ICM-Wind/Code/icmwind/data_files/TestData.csv"), ';');
		reader.readHeaders();
		String [] headers = reader.getHeaders();
		Set<String> classNames = new HashSet<String>();
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("E:/ICM-Wind/Code/icmwind/config_files/classmapping.properties"));
		
		for(String head : headers) {
			if( (prop.getProperty(head)!=null) && (! prop.getProperty(head).equals("NaN")) ) {
				classNames.add(prop.getProperty(head));
//				System.out.println(prop.getProperty(head));
			}
		}
		
		/**
		 * We have Class Names and we map them to OWLClasses in HashMap<String,OWLClass> 
		 */
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		IRI ontologyIri = IRI.create("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl");
		File file = new File("E:/ICM-Wind/Code/icmwind/ontology/WindTurbineOntoCopy.owl");
		
		manager.addIRIMapper(new SimpleIRIMapper(ontologyIri, IRI.create(file)));
		
		OWLOntology ontology = manager.loadOntology(ontologyIri);
		System.out.println("Loaded ontology: " + ontology);
		
		OWLDataFactory factory = manager.getOWLDataFactory();
		PrefixManager pm = new DefaultPrefixManager("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#");
		
		Map<String, OWLClass> nameToOWLClassMap = new HashMap<String, OWLClass>();
		
		for(String name : classNames) {
			String tempClassName = ":" + name;
			nameToOWLClassMap.put(name, factory.getOWLClass(tempClassName, pm));
		}
		
//		for(String name1 : classNames) {
//			System.out.println("For\t" + name1 + "\tclass\t" + nameToOWLClassMap.get(name1));
//		}
		
		
//		Set<OWLClass> classesInOntology = ontology.getClassesInSignature();
//		
//		for(String name : classNames) {
//			System.out.print(name + " ");
//			for(OWLClass aClass : classesInOntology) {
//				if(aClass.getIRI().getFragment().equals(name)){
//					System.out.println("name equals class " +aClass.getIRI().getFragment());
//				} 
//			}
//		}
		
		
		reader.close();		
	}

}
