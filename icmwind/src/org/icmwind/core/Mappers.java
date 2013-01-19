package org.icmwind.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

import com.google.common.collect.HashBiMap;

public interface Mappers {
	
	public HashBiMap<String, String> getHeaderToClassMap(String mappingPropertiesFilePath, String [] headerNames ) throws FileNotFoundException, IOException;
	
	public Map<String, String> getClassToHeaderMap();
	
	public Map<String, OWLClass> getClassToURIMap(Set<String> classNames, Set<OWLClass> classes);
	
}
