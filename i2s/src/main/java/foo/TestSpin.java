package foo;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.topbraid.spin.inference.SPINExplanations;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.progress.SimpleProgressMonitor;
import org.topbraid.spin.system.SPINModuleRegistry;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class TestSpin {

//	Map<String, String> mapABoxToFile = new HashMap<String, String>();
	private static OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	private static OntModel infModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	
	private PrintWriter logger = null;
	private String infOntURI = null;
	private String infOntPath = null;
	
	public TestSpin(String ontoURI, String ontoFilePath, PrintWriter logWriter) {
		infOntURI = "http://www.icmwind.com/instances/iwo-spinbox-" 
						+ ontoURI.substring("http://www.icmwind.com/instances/iwo-abox-".length());

		infOntPath = "C:/Users/anme05/git/icmwindsem/icmwindapp/war/data store/ontologies/iwo-spinbox-"
						+ ontoURI.substring("http://www.icmwind.com/instances/iwo-abox-".length());
				
		logger = logWriter;
		
		model.getDocumentManager().addAltEntry("http://www.icmwind.com/icmwindontology.owl", "file:C:/Users/anme05/git/icmwindsem/i2s/icmwindontology.owl");
		
		
		OntModel abox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		abox.getDocumentManager().addAltEntry(ontoURI, "file:" + ontoFilePath);

		System.out.println("**TestSpin.TestSpin() : Reading ABox - " + ontoURI);
		abox.read(ontoURI);
		
		model.addSubModel(abox);
		
		System.out.println("**TestSpin.TestSpin() : Reading OWL/SPIN-TBox - " + "http://www.icmwind.com/icmwindontology.owl");
		model.read("http://www.icmwind.com/icmwindontology.owl");
		
		logger.println();
		logger.println("TopSPIN Inferencing.");
		logger.println("Ontology To Infer");
		logger.println("- URI: " + ontoURI);
		logger.println("- Path: " + ontoFilePath);
	}
	
	public void spin() {
		
		long startTime = System.nanoTime();
		
		SPINModuleRegistry.get().init();
		
		SPINExplanations se = new SPINExplanations();
		
		model.addSubModel(infModel);
		
		SPINModuleRegistry.get().registerAll(model, null);
		
		SimpleProgressMonitor spm = new SimpleProgressMonitor("Progresser");
		
		logger.println("Running inferencing...");
		System.out.println("**TestSpin.spin() : Running inferencing...");
				
		SPINInferences.run(model, infModel, se, null, false, spm);
				
		DatatypeProperty hasvalue = infModel.createDatatypeProperty("http://www.icmwind.com/icmwindontology.owl#hasValue");
		DatatypeProperty isValveOpen = infModel.createDatatypeProperty("http://www.icmwind.com/icmwindontology.owl#isValveOpen");
		ObjectProperty hasObservation = infModel.createObjectProperty("http://www.icmwind.com/icmwindontology.owl#hasObservation");
		ObjectProperty hasStatus = infModel.createObjectProperty("http://www.icmwind.com/icmwindontology.owl#hasStatus");
		
		logger.println("Triples added: " + infModel.size());
		System.out.println("**TestSpin.spin() : Triples Created - " + infModel.size());
		
		infModel.setNsPrefix("iwo", "http://www.icmwind.com/icmwindontology.owl#");
		infModel.setNsPrefix("base", infOntURI + "#");
		
		logger.println("Writing to file..");
		System.out.println("**TestSpin.spin() : Writing Infered ABox - " + infOntURI);
		
		try {
			infModel.write(new FileWriter(infOntPath), "RDF/XML", null);
			
			long elapsedTime = (System.nanoTime() - startTime) / 1000000;
			System.out.println("**TestSpin.spin() : Time for inferencing - " + elapsedTime  + " ms" );
			
			System.out.println("**TestSpin.spin() : Saved Infered ABox - " + infOntPath);
						
			logger.println("Inferred Ontology");
			logger.println("- URI: " + infOntURI);
			logger.println("- Path: " + infOntPath);
			logger.println("- SPIN Inferencing Time: " + elapsedTime + "ms");
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		

		logger.flush();
	}
		
	public String getInfOntURI() {
		return infOntURI;
	}
	
	public String getInfOntPath() {
		return infOntPath;
	}
	
	public static void addDataBox(String modelUri) {
		OntModel databox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		databox.getDocumentManager().addAltEntry("http://www.icmwind.com/instances/" + modelUri , "file:C:/Users/anme05/git/icmwindsem/i2s/" + modelUri);
		databox.read("http://www.icmwind.com/instances/" +  modelUri);

		model.addSubModel(databox);
	}	
	public static void main(String[] args) throws IOException {
	
		
		long startTime = System.nanoTime();
		
		SPINModuleRegistry.get().init();
		
		model.getDocumentManager().addAltEntry("http://www.icmwind.com/icmwindontology.owl", "file:C:/Users/anme05/git/icmwindsem/i2s/icmwindontology.owl");
		
//		OntModel infobox = ModelFactory.createOntologyModel();
//		infobox.getDocumentManager().addAltEntry("http://www.icmwind.com/instance/iwo-infobox.owl", "file:C:/Users/anme05/git/icmwindsem/i2s/iwo-infobox.owl");
//		infobox.read("http://www.icmwind.com/instance/iwo-infobox.owl");
//		
//		model.addSubModel(infobox);
//		
//		addDataBox("iwo-databox-04.09.2008-05.09.2008.owl");
//		addDataBox("iwo-databox-05.09.2008-05.09.2008.owl");
//		addDataBox("iwo-databox-05.09.2008-06.09.2008.owl");
//		addDataBox("iwo-databox-06.09.2008-07.09.2008.owl");

		addDataBox("iwo-abox-04.09-06.09.2008.owl");
		
		model.read("http://www.icmwind.com/icmwindontology.owl");
		
//		Model newTriples = ModelFactory.createDefaultModel(ReificationStyle.Minimal);
		OntModel newTriples = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		model.addSubModel(newTriples);
		
		
		SPINExplanations se = new SPINExplanations();
		
		SPINModuleRegistry.get().registerAll(model, null);
		
		SPINInferences.run(model, newTriples, se, null, false, null);
		System.out.println("Inferred triples: " + newTriples.size());
		
		long elapsedTime = System.nanoTime() - startTime;
		System.out.println("Time for inferencing: " + elapsedTime / 1000000 + " ms" );
		
		DatatypeProperty hasvalue = newTriples.createDatatypeProperty("http://www.icmwind.com/icmwindontology.owl#hasValue");
		DatatypeProperty isValveOpen = newTriples.createDatatypeProperty("http://www.icmwind.com/icmwindontology.owl#isValveOpen");
		ObjectProperty hasObservation = newTriples.createObjectProperty("http://www.icmwind.com/icmwindontology.owl#hasObservation");
		ObjectProperty hasStatus = newTriples.createObjectProperty("http://www.icmwind.com/icmwindontology.owl#hasStatus");
		
				
		newTriples.setNsPrefix("iwo", "http://www.icmwind.com/icmwindontology.owl#");
		newTriples.write(new FileWriter("iwo-spinbox-04.09.2008-06.09.2008.owl"), "RDF/XML", null);
		
	
//		newTriples.write(new FileWriter("iwo-infobox-10.10.2008-13.10.2008.owl"));
		
//		newTriples.write(System.out);
		
//		ExtendedIterator<Individual> it = model.listIndividuals();
//		
//		while(it.hasNext()) {
//			Individual t = it.next();
//			String i = t.getURI().toString();
//			if(i.equals("http://www.icmwind.com/icmwindontology.owl#tbv_marpI"))
//				System.out.println(i	}
	
				
	}
	
		
}

