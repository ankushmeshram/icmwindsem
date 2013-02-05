package org.icmwind.core.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.icmwind.core.FileProcess;
import org.icmwind.core.ICMWindSetup;
import org.icmwind.core.OntologyProcess;
import org.icmwind.core.RDFEncoding;
import org.icmwind.util.Utility;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;

public class RDFEncodingImpl implements RDFEncoding {

	private static RDFEncodingImpl INSTANCE = new RDFEncodingImpl();
	
	private static OntologyProcess ontoproc = OntologyProcessImpl.getInstance();
	private static FileProcess fileproc = FileProcessImpl.getInstance();
	
	private static List<String> headerslist;
	private static List<String> classeslist;
	
	private static AbstractStringMetric similaritymetric = new CosineSimilarity();
	
	private static Map<String, List<String>> mapping = new HashMap<String, List<String>>();
	
	private static boolean isInitialised = false;
	
	
	private RDFEncodingImpl()
	{ 
		ICMWindSetup.init();
		
		try {
			Utility.initDictionary(ICMWindSetup.getDictionaryPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static RDFEncodingImpl getInstance() {
		return INSTANCE;
	}
	
	@Override
	public void initOntologyProcess()
	{
		ontoproc.openFile(ICMWindSetup.getCoreOntologyPath());
		classeslist = ontoproc.getNormClassNamesList();
	}

	@Override
	public void initFileProcess(String filepath)
	{
		fileproc.openFile(filepath);
		headerslist = fileproc.getNormHeadNamesList();
		
		Utility.setSimilarityMetric(similaritymetric);
		for(String header : headerslist) {
			List<String> templist = new ArrayList<String>(); 
			for(String clas : Utility.getSimilarTextList(header, classeslist)) {
				templist.add(ontoproc.getClassNameFor(clas));
			}
			mapping.put(fileproc.getHeadNameFor(header), templist);
		}
		
		isInitialised = true;
	}

	@Override
	public Map<String, List<String>> returnMapping() {
		if(isInitialised)
			return mapping;
		else
			return Collections.emptyMap();
	}

}
