package foo;

import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ParserException;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import uk.ac.manchester.cs.factplusplus.owlapiv3.FaCTPlusPlusReasonerFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	IRI ontiri = IRI.create("http://www.icmwind.com/instances/iwo-abox-04.09.2008.owl");
    	IRI dociri = IRI.create("file:C:/Users/anme05/git/icmwindsem/i2s/kaminey.owl");
    	String classExp = "Property and isPropertyOf some Oil";
    	
    	ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
    	    	
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLDataFactory fact = man.getOWLDataFactory();
        
        man.addIRIMapper(new SimpleIRIMapper(ontiri, dociri));
        OWLOntology ont = null;
        
        try {
			ont = man.loadOntology(ontiri);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
        
        Set<OWLOntology> importsClosure = ont.getImportsClosure();
        
        BidirectionalShortFormProvider bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(man, importsClosure, shortFormProvider);
        
        OWLReasonerFactory reasonerFactory = new FaCTPlusPlusReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(progressMonitor);
		
		OWLReasoner reasoner = reasonerFactory.createReasoner(ont, config);
		reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY);
		
		ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(fact, classExp);
		parser.setDefaultOntology(ont);
		
		OWLEntityChecker entityChecker = new ShortFormEntityChecker(bidiShortFormProvider);
		parser.setOWLEntityChecker(entityChecker);
		
		OWLClassExpression classExpression = null;
		
		try {
			classExpression = parser.parseClassExpression();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
				
		System.out.println(reasoner.getEquivalentClasses(classExpression).toString());
		System.out.println(reasoner.getSubClasses(classExpression, true).toString());
		
		
		
		
		
       
    }
}
