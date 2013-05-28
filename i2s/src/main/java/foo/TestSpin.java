package foo;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.topbraid.spin.inference.SPINExplanations;
import org.topbraid.spin.inference.SPINInferences;
import org.topbraid.spin.system.SPINModuleRegistry;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.AddDeniedException;
import com.hp.hpl.jena.shared.ReificationStyle;


public class TestSpin {

//	Map<String, String> mapABoxToFile = new HashMap<String, String>();
	static OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	
	public static void addDataBox(String modelUri) {
		OntModel databox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		databox.getDocumentManager().addAltEntry("http://www.icmwind.com/instances/" + modelUri , "file:C:/workspace_sdre/i2s/" + modelUri);
		databox.read("http://www.icmwind.com/instances/" +  modelUri);

		model.addSubModel(databox);
	}
		
	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		
		SPINModuleRegistry.get().init();
		
		model.getDocumentManager().addAltEntry("http://www.icmwind.com/icmwindontology.owl", "file:C:/workspace_sdre/i2s/icmwindontology.owl");
		
		OntModel infobox = ModelFactory.createOntologyModel();
		infobox.getDocumentManager().addAltEntry("http://www.icmwind.com/instance/iwo-infobox.owl", "file:C:/workspace_sdre//i2s/iwo-infobox.owl");
		infobox.read("http://www.icmwind.com/instance/iwo-infobox.owl");
		
		model.addSubModel(infobox);

		
//		OntModel databox = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//		databox.getDocumentManager().addAltEntry("http://www.icmwind.com/instances/iwo-databox-04.09.2008-05.09.2008.owl", "file:C:/workspace_sdre/i2s/iwo-databox-04.09.2008-05.09.2008.owl");
//		databox.read("http://www.icmwind.com/instances/iwo-databox-04.09.2008-05.09.2008.owl");
//
//		model.addSubModel(databox);
		
		addDataBox("iwo-databox-04.09.2008-05.09.2008.owl");
		addDataBox("iwo-databox-05.09.2008-05.09.2008.owl");
		addDataBox("iwo-databox-05.09.2008-06.09.2008.owl");
		addDataBox("iwo-databox-06.09.2008-07.09.2008.owl");
		addDataBox("iwo-databox-07.09.2008-08.09.2008.owl");
		addDataBox("iwo-databox-08.09.2008-10.09.2008.owl");
		addDataBox("iwo-databox-10.09.2008-11.09.2008.owl");
		addDataBox("iwo-databox-11.09.2008-12.09.2008.owl");
		addDataBox("iwo-databox-12.09.2008-12.09.2008.owl");
		addDataBox("iwo-databox-12.09.2008-13.09.2008.owl");
		addDataBox("iwo-databox-13.09.2008-14.09.2008.owl");
		
		
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
		newTriples.write(new FileWriter("iwo-spinbox-04.09.2008-13.09.2008.owl"), "RDF/XML", null);
		
	
//		newTriples.write(new FileWriter("iwo-infobox-10.10.2008-13.10.2008.owl"));
		
//		newTriples.write(System.out);
		
//		ExtendedIterator<Individual> it = model.listIndividuals();
//		
//		while(it.hasNext()) {
//			Individual t = it.next();
//			String i = t.getURI().toString();
//			if(i.equals("http://www.icmwind.com/icmwindontology.owl#tbv_marpI"))
//				System.out.println(i);
//		}
		
		
		
	}
	
}

