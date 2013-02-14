package org.icmwind.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;


import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;

/**
 * @author anme05
 * 
 *         Static Class to provide methods for Dictionary and Similarity Metrics (MatchOrSuggest).
 *         IMPORTANT: Check whether ICMWindSetup has been
 *         initialized or not.
 */
public class Utility {
	private static Properties prop = new Properties();
	private static boolean isDictInitialized = false;
	
	private static AbstractStringMetric metric = new CosineSimilarity();
	private static float thresholdscore = (float) 0.5;
	
	private Utility() {
	}

	/**
	 * @param dictpath
	 *            PAth of the German-English Dictionary (*.properties)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void initDictionary(String dictpath)
			throws FileNotFoundException, IOException 
	{

		if (!ICMWindSetup.isInitialised())
			ICMWindSetup.init();

		prop.load(Utility.class.getClassLoader().getResourceAsStream(dictpath));
		isDictInitialized = true;
	}

	/**
	 * @return true if Dictionary has been initialized via initDictionary(String dictpath) 
	 */
	public static boolean isDictInitialized() {
		return isDictInitialized;
	}

		

	/**
	 * @param query
	 *            German word for which English translation is requested. Also
	 *            dictionary has to be initialized already, if not print error.
	 * @return English translation if else return NA
	 */
	public static String searchInDict(String query) 
	{
		if (prop == null) {
			System.out
					.println("Error: Dicitionary not initialised. Check static method Utility.initDictionary(String dictpath)");
			return null;
		}

		if (prop.getProperty(query) != null) {
			return prop.getProperty(query);
		} else {
			return query;
		}

	}

	/**
	 * @param querytext
	 *            Text to search in Dictionary. Split querytext according to
	 *            whitespaces and translate individually.
	 * @return Translated querytext
	 */
	public static String searchTextInDict(String querytext) {
		String[] textsplit = querytext.trim().split(" ");

		if (textsplit.length > 0) {
			StringBuilder result = new StringBuilder();

			for (String text : textsplit)
				result.append(searchInDict(text) + " ");

			return result.toString().trim();
		} else {
			return searchInDict(querytext);
		}

	}

	
	
	

	/**
	 * @param list List to convert to String array
	 * @return String[] array
	 */
	public static String[] convertStringListToArray(List<String> list) {
		String[] tempArr = new String[list.size()];
		tempArr = list.toArray(tempArr);
		return tempArr;
	}

	/**
	 * @param metric the metric to set
	 */
	public static void setSimilarityMetric(AbstractStringMetric metric) {
		Utility.metric = metric;
	}


	/**
	 * @param scorethreshold the threshold score to set
	 */
	public static void setThresholdScore(float scorethreshold) {
		Utility.thresholdscore = scorethreshold;
	}
	
	/**
	 * @param query
	 * @param dataList
	 * @return
	 */
	public static List<String> matchOrSuggest(String query, List<String> dataList) {
//		DEBUG
//		System.out.println("SYSTEM: String Similarity - Similarity Metric: " + metric.getShortDescriptionString() + " . Threshold >= " + thresholdscore + ".");
		
		List<String> similartextlist = new ArrayList<>(createSimilarityMap(metric, query, dataList, thresholdscore).keySet()); 	
		return similartextlist;
	}
	
	/**
	 * @param query
	 * @param dataList
	 * @return
	 */
	public static List<String> getSimilarTextList(String query, List<String> dataList) {
//		DEBUG
//		System.out.println("SYSTEM: String Similarity - Similarity Metric: " + metric.getShortDescriptionString() + " . Threshold >= " + thresholdscore + ".");
		
		List<String> similartextlist = new ArrayList<>(createSimilarityMap(metric, query, dataList, thresholdscore).keySet()); 	
		return similartextlist;
	}
	
	/**
	 * @param query
	 * @param dataList
	 * @param similaritymetric
	 * @return sorted similarity map
	 */
	public static Map<String,Float> createSimilarityMap(AbstractStringMetric similaritymetric, String query, List<String> dataList, float threshold) {
		
		AbstractStringMetric metric = similaritymetric;
		Map<String, Float> similarityMap = new HashMap<String, Float>();
		
		for(String data : dataList ) {
			
			float score = metric.getSimilarity(query, data);

			// remove strings with 0.0 score
			if(score != 0.0) {
				
				// only strings having score greater than threshold
				if(score >= threshold) {
					
					// if match is found i.e. 1.0 score
					if(score == 1.0) {
						
						// if map is empty, add match and break
						if(similarityMap.isEmpty()) {
							similarityMap.put(data, score);
							break;
						}
						
						// if map is not empty, clear it. Add and break
						else {
							similarityMap.clear();
							similarityMap.put(data, score);
							break;
						}
					}
					
					// if exact match not found add it anyway
					else {
						similarityMap.put(data, score);
					}
				}
			}
		}
		
		return sortMapByValues(similarityMap);
	}
	
	/**
	 * @param unsortedMap
	 * @return sorted map
	 */
	@SuppressWarnings("rawtypes")
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortMapByValues(Map<K,V> unsortedMap) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K,V>>(unsortedMap.entrySet());
		
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				// for descending order otherwise o1.getValue().compareTo(o2.getValue())				
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		
		for(Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}
	
	
//	/**
//	 * @param similarityMap
//	 * @return
//	 */
//	public static List<String> getTopResults(int topNum, Map<String,Float> similarityMap) {
//		List<String> topResults = new ArrayList<String>();
//		int count = 0;
//		
//		for( Entry<String, Float> entry : similarityMap.entrySet() ) {
//			if(count <= topNum) {
//				topResults.add(entry.getKey());
//				count ++;
//			}
//		}
//		
//		return topResults;
//	}	
	
	
}
