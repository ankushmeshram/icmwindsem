package foo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.HermiT.Reasoner;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;


public class RunReasoner {

	static Map<String, String> map = new HashMap<String, String>();	
	
	
//	static String uri = "http://www.icmwind.com/instances/abox-04.09.2008.owl";
//	static String docUri = "file:/C:/Users/anme05/Dropbox/abox-04.09.2008.owl";
//	
//	static IRI iri = IRI.create(uri);
//	static IRI docIri = IRI.create(docUri);
	
	static long ca = 0;
	static long ch= 0;
	static long dpa = 0;
	static long opa = 0;
	static long oph = 0;
	static long cons = 0;
	
	public static void populate() {
//		map.put("http://www.icmwind.com/instances/iwo-abox-04.09-13.09.2008.owl", "file:/C:/workspace_sdre/i2s/iwo-abox-04.09-13.09.2008.owl");
		map.put("http://www.icmwind.com/instances/iwo-abox-04.09-11.09.2008.owl", "file:/C:/workspace_sdre/i2s/iwo-abox-04.09-11.09.2008.owl");
//		map.put("http://www.icmwind.com/instances/iwo-abox-04.09-06.09.2008.owl", "file:/C:/workspace_sdre/i2s/iwo-abox-04.09-06.09.2008.owl");
//		map.put("http://www.icmwind.com/instances/iwo-abox-04.09.2008.owl", "file:/C:/workspace_sdre/i2s/iwo-abox-04.09.2008.owl");
	}
	
	public static void main(String[] args) throws OWLOntologyCreationException, FileNotFoundException, UnsupportedEncodingException {
		TimeManagement tm = TimeManagement.get();
		tm.startTime();
		
		populate();
		
		OWLOntologyManager m = OWLManager.createOWLOntologyManager();
		
		OWLReasonerFactory reasonerFactory = null;
		
		Set<OWLReasonerFactory> reasonerSet = new HashSet<OWLReasonerFactory>();
		reasonerSet.add(new Reasoner.ReasonerFactory());
		reasonerSet.add(new FaCTPlusPlusReasonerFactory());
				
		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		
		int data = 4;
		
		for(Map.Entry<String, String> entry : map.entrySet()) {
			IRI ontoIri = IRI.create(entry.getKey());
			IRI docIri = IRI.create(entry.getValue());
			
			m.addIRIMapper(new SimpleIRIMapper(ontoIri, docIri));
			
			OWLOntology o = m.loadOntology(ontoIri);
			
			for(OWLReasonerFactory rf : reasonerSet) {
				reasonerFactory = rf;
				
				OWLReasoner reasoner = reasonerFactory.createReasoner(o, config);
								
				String rn = reasoner.getReasonerName();
				System.out.println("**Reasoner: " + rn);
				System.out.println("--Precomputaions--");
				
				String filename = rn + "--data" + data + ".txt";
				
				PrintWriter writer = new PrintWriter(filename, "UTF-8");
				
				writer.println(docIri);
				
				System.out.println(docIri);
								
				System.out.println("1. CLASS ASSERTIONS");
				reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS);
				ca = tm.elapsedTime(); tm.restart();
				System.out.println("** time taken : " + ca + " ns");
				writer.println("CLASS ASSERTION - time taken : " + ca + " ns");
				
				System.out.println("2. CLASS HIERARCHY");
				reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);
				ch = tm.elapsedTime(); tm.restart();
				System.out.println("** time taken : " + ch + " ns");
				writer.println("CLASS HIERARCHY - time taken : " + ch + " ns");
				
				System.out.println("3. DATA PROPERTY ASSERTIONS");
				reasoner.precomputeInferences(InferenceType.DATA_PROPERTY_ASSERTIONS);
				dpa = tm.elapsedTime(); tm.restart();
				System.out.println("** time taken : " + dpa + " ns");
				writer.println("DATA PROPERTY ASSERTIONS - time taken : " + dpa + " ns");

				System.out.println("4. OBJECT PROPERTY ASSERTIONS");
				reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_ASSERTIONS);
				opa = tm.elapsedTime(); tm.restart();
				System.out.println("** time taken : " + opa + " ns");
				writer.println("OBJECT PROPERTY ASSERTIONS - time taken : " + opa + " ns");
				
				System.out.println("5. OBJECT PROPERTY HIERARCHY");
				reasoner.precomputeInferences(InferenceType.OBJECT_PROPERTY_HIERARCHY);
				oph = tm.elapsedTime(); tm.restart();
				System.out.println("** time taken : " + oph + " ns");
				writer.println("OBJECT PROPERTY HIERARCHY - time taken : " + oph + " ns");
			
				System.out.println("6. CHECK CONSISTENCY");
				boolean consistent = reasoner.isConsistent();
				System.out.println("Consistent: " + consistent);
				cons = tm.elapsedTime(); tm.restart();
				System.out.println("** time taken : " + cons + " ns");
				writer.println("CHECK CONSISTENCY : " + consistent);
				writer.println("CHECK CONSISTENCY - time taken : " + cons + " ns");
				
		
				System.out.println("reasoner - " + rn + ": ca = " + ca + "; ch = " + ch + "; dpa = " + dpa + "; opa = " + opa + "; oph = " + oph + "; consistency= " + cons);
				writer.println("reasoner - " + rn + ": ca = " + ca + "; ch = " + ch + "; dpa = " + dpa + "; opa = " + opa + "; oph = " + oph + "; consistency= " + cons);
				
				writer.close();
			}
			
			data++;
		}
	}
	
	static class TimeManagement {
		
		private long startTime = 0;
		private long elapsedTime = 0;
		
		static TimeManagement tm = new TimeManagement();
		
		public TimeManagement() {
		}
		
		public static TimeManagement get() {
			return tm;
		}
		
		private void startTime() {
			startTime = System.nanoTime();
			System.out.println("**------------------TimeManagement.startTime() : Timer has start. ");
		}
		
		public long elapsedTime() {
			elapsedTime = System.nanoTime() - startTime;
			double time = elapsedTime / 1000000;
			System.out.println("**------------------TimeManagement.elapsedTime() : " + time + " millis");
			return elapsedTime;
		}
		
		public void restart() {
			startTime = 0; elapsedTime = 0;
			System.out.println("**------------------TimeManagement.restartTime() : Timer has been reset. ");
			startTime();
		}
		
	}

}
