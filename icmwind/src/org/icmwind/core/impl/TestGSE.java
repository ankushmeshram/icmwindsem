package org.icmwind.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import de.dfki.isreal.semantic.impl.GlobalSESControllerImpl;
import de.dfki.isreal.data.Statement;
import de.dfki.isreal.helpers.Profiler;

//ontology uri:http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl

public class TestGSE {
	
	public static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) throws OWLOntologyCreationException, URISyntaxException, IOException {
		Profiler.init();
		GlobalSESControllerImpl gseci = new GlobalSESControllerImpl("E:/ICM-Wind/Code/icmwind/gse_config/sab_2011.isrealomsconfig");
		
		/*
		String ns = "http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#";
		int opt = 0;
		
		String wto = "PREFIX wto: <http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#>\n";
		String rdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n";
		String owl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n";
		String rdf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n";
				
		while(true) {
			System.out.print("\nOptions:\n1.Class Consistency\n2.Class Subsumption\n3.Instance Checking\n4.SPARQL ASK\n5.SPARQL SELECT\n0.Exit\nEnter your option: ");
			opt = in.nextInt();
			
			switch(opt) {
			case 1:
				System.out.println("\n--Class Consistency--");
				System.out.print("Enter class name to check it's consistency - ");
				String queryCls = in.next();
				System.out.println("\'" + queryCls + "\' Class Consistency - " + gseci.checkClassConsistency(new URI(ns+queryCls)));
				break;
			case 2:
				System.out.println("\n--Class Subsumption--");
				System.out.print("Enter query class name (Sub-Class)- ");
				String subCls = in.next();
				System.out.print("Enter class name - ");
				String cls = in.next();
				System.out.println("\'" + subCls + "\' - \'" + cls + "\' Class Subsumption - " + gseci.checkClassSubsumption(new URI(ns+subCls), new URI(ns+cls)));
				break;
			case 3:
				System.out.println("\n--Instance Checking--");
				System.out.print("Enter query instance name - ");
				String instanceName = in.next();
				System.out.print("Enter query class name - ");
				String className = in.next();
				System.out.println("\'" + instanceName + "\' - \'" + className + "\' Instance Checking - " + gseci.instanceChecking(new URI(ns+instanceName), new URI(ns+className)));
				break;
			case 4:
				System.out.println("\n--SPARQL ASK--");
				String askQuery = "ASK  {  ?iso_4_1  wto:hasObservation  wto:Observation_1 . \n" + "?iso_4_1  rdf:type  wto:ISO_4 .  }";
				gseci.runSPARQLAsk(wto+rdfs+owl+rdf+askQuery);
				break;
			case 5:
				System.out.println("\n--SPARQL SELECT--");
				String selectQuery = "SELECT ?sensors WHERE { ?sensors rdfs:subClassOf wto:Sensor }";
				gseci.runSPARQLSelect(wto+rdfs+selectQuery);
				break;
			case 0:
				System.out.println("Exiting program.");
				System.exit(0);
			default:
				System.out.println("Invalid entry!!");
			}
		}*/

		/*
		infoStart("KB Consistency");
		System.out.println("ICMWIND consistency - " + gseci.checkKBConsistency());
		infoEnd("KB Consistency");
	
		URI uri = new URI("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#Sensor");
		
		infoStart("Class Consistency");
		System.out.println("\'Sensor\' class consistency - " + gseci.checkClassConsistency(uri));
		infoEnd("Class Consistency");
		
		infoStart("Class Sumbption");
		System.out.println("\'TemperatureSensor - Sensor\' class subsumption - " + 
				gseci.checkClassSubsumption(new URI("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#TemperatureSensor"),uri));
		infoEnd("Class Sumbption");
		
		infoStart("Instance Checking"); 
		gseci.instanceChecking(new URI("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#VLGW_dP_9"),
					new URI("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#VLGW_dP"));
		infoEnd("Instance Checking");
		
		String wto = "PREFIX wto: <http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#>\n";
		String rdfs = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n";
		String owl = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n";
		String rdf = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"; 
		
		infoStart("ASK QUERY");
		String askQuery = "ASK  {  ?iso_4_1  wto:hasObservation  wto:Observation_1 . " +
						  "?iso_4_1  rdf:type  wto:ISO_4 .  }";
		gseci.runSPARQLAsk(wto+rdfs+owl+rdf+askQuery);
		infoEnd("ASK QUERY");
		
		infoStart("SELECT QUERY");
		String selectQuery = "SELECT ?sensors WHERE { ?sensors rdfs:subClassOf wto:Sensor }";
		gseci.runSPARQLSelect(wto+rdfs+selectQuery);
		infoEnd("SELECT QUERY");
		*/
/*		List<String> entities =new ArrayList<String>();
//		entities.add("http://www.dfki.de/isreal/room_abox.owl#doorEF");
//		entities.add("http://www.dfki.de/isreal/room_abox.owl#NANCY");
//		entities.add("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#VLGW_dP_2");
//		entities.add("http://www.semanticweb.org/ontologies/2012/4/WindTurbineOnto.owl#Observation_2");
		
		List<List<Statement>> answer = gseci.computeTopRelationalTrees(entities, 2, true);
		Iterator<List<Statement>> it = answer.iterator();
		while(it.hasNext()) {
			List<Statement> tempList =  it.next();
			Iterator<Statement> tempIt = tempList.iterator();
			while(tempIt.hasNext()){
				System.out.println(tempIt.next().stringValue());
			}
		}
		
*/		
	}

}
