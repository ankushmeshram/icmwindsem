package com.icmwind.gui.web.server.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.icmwind.gui.web.client.services.AnalysisService;

import de.dfki.sdre.test.SDREGUI;


public class AnalysisServiceImpl extends RemoteServiceServlet implements
		AnalysisService {

	private static final long serialVersionUID = 1L;
	
	private SDREGUI sg = null;

	@Override
	public String initAnalysis(String ontURI) {

		String configFilePath = "C:/Users/anme05/alternative_git/sdre/de.dfki.sdre/res/gse_config/sab_2011_test.isrealomsconfig";
		
		// copy file
		
		Path src =  new File("C:/Users/anme05/alternative_git/sdre/de.dfki.sdre/res/gse_config/sab_2011.isrealomsconfig").toPath();
		Path dest = new File("C:/Users/anme05/alternative_git/sdre/de.dfki.sdre/res/gse_config/sab_2011_test.isrealomsconfig").toPath();
		
		try {
			Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		// open copied file and add annotations
				
		try {
			
			File configFile = new File(configFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(configFile);
			
			doc.getDocumentElement().normalize();
			
//			System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());

			Node ontNode = doc.getElementsByTagName("ontologies").item(0);
			
			NamedNodeMap attr = ontNode.getAttributes();
			Node nodeAttr = attr.getNamedItem("abstractURIString");
			nodeAttr.setTextContent(ontURI);
			
			Node nodePhysURI = doc.getElementsByTagName("physicalURIString").item(0);
			nodePhysURI.setTextContent(ontURI.substring(33));
			
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(configFile);
			t.transform(source, result);
			
			System.out.println("Done.");
			
			System.gc();System.gc();System.gc();
			
			return configFilePath;
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean initSDRE(String configFilePath) {
		sg = new SDREGUI();
		return sg.initAnalysis(configFilePath);
				
//		return true;
	}

	@Override
	public List<String> sensorRedundancyCheck() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Run the show!!



}
