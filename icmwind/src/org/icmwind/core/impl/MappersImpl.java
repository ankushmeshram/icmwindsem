package org.icmwind.core.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.icmwind.core.Mappers;
import org.semanticweb.owlapi.model.OWLClass;

import com.google.common.collect.HashBiMap;

public class MappersImpl implements Mappers {

	private HashBiMap<String, String> headerToClassMap = HashBiMap.create();
	private Map<String,OWLClass> classToURIMap = new HashMap<String, OWLClass>();
	
	@Override
	public HashBiMap<String, String> getHeaderToClassMap(String mappingPropertiesFilePath, String[] headerNames) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(mappingPropertiesFilePath));
		
		//* Add only elements which are mapped (i.e. ignore Null) and have mapping in Properties (i.e. ignore mappings with "NaN"). 
		for(String head : headerNames) {
			if( (prop.getProperty(head)!=null) && (! prop.getProperty(head).equals("NaN")) ) {
				headerToClassMap.put(head, prop.getProperty(head));
			}
		}
		
		return headerToClassMap;
	}

	
	@Override
	public Map<String, String> getClassToHeaderMap() {
		return headerToClassMap.inverse();
	}

	
	@Override
	public Map<String, OWLClass> getClassToURIMap(List<String> classNames, Set<OWLClass> classes) {
		for(String className : classNames) {
			for(OWLClass aClass : classes ) {
				if(className.equals(aClass.getIRI().getFragment())){
					classToURIMap.put(className, aClass);
				}
			}
		}
		return classToURIMap;
	}


	@Override
	public Map<String, OWLClass> getClassToURIMap(Set<String> classNames,
			Set<OWLClass> classes) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
