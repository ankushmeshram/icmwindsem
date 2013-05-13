package org.icmwind.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.icmwind.core.impl.OntologyProcessJenaImpl;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestJena {

	public static void main(String[] args) throws ParseException {
		
		OntologyProcessJenaImpl opij = OntologyProcessJenaImpl.getInstance();
		opij.init();
//		opij.createAbox();
		System.out.println(opij.getClassNamesList().toString());
		
		System.out.println(opij.getClassForURI(opij.getNS() + "CS_Drive").getURI());
		
		
//		String uri = "http://www.test.com/test.ow";
//		String ns = uri + "#";
//		
//		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
//		m.getDocumentManager().addAltEntry("http://www.test.com/test.ow", "file:C:/Users/anme05/Desktop/testing.owl");
//		
//		m.read(uri);
//		
//		OntClass a = m.getOntClass(ns + "A");
//		
//		Individual l = m.createIndividual(ns + "L", a); 
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//		
//		Date date = sdf.parse("26.10.2008 14:32:00");
//		
//		Calendar c = GregorianCalendar.getInstance();
//		c.setTime(date);
//		
//		Literal t = m.createTypedLiteral(c);
//		
////		Literal t = m.createTypedLiteral("26.10.2008 14:32:00", XSDDatatype.XSDdateTime);
//		
//		DatatypeProperty hasTime = m.getDatatypeProperty(ns + "hasTime");
//
//		l.addProperty(hasTime, t);
//		
//		m.writeAll(System.out, "Turtle", null);
//		
		
	}
}
