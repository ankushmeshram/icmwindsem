package org.icmwind.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @author anme05
 * 
 *         Static Class to provide methods for Dictionary and Similarity Metrics (MatchOrSuggest).
 *         IMPORTANT: Check whether ICMWindSetup has been
 *         initialized or not.
 */
public class TranslationUtility {
	private static Properties prop = new Properties();
	private static boolean isDictInitialized = false;
	
	private TranslationUtility() {
	}

	/**
	 * Load dictionary from the passed location
	 * 
	 * @param dictpath
	 *            PAth of the German-English Dictionary (*.properties)
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void initDictionary(String dictpath)
			throws FileNotFoundException, IOException 
	{
		if (!ICMWindConfig.isInitialised())
			ICMWindConfig.init();

		prop.load(TranslationUtility.class.getClassLoader().getResourceAsStream(dictpath));
		isDictInitialized = true;
	}

		
	/**
	 * Find German word in the dictionary and return English word
	 * 
	 * @param query
	 *            German word for which English translation is requested. Also
	 *            dictionary has to be initialized already, if not print error.
	 * @return English translation if else return NA
	 */
	public static String searchInDict(String query) 
	{
		if (prop == null) {
			System.out.println("Error: Dicitionary not initialised. Check static method Utility.initDictionary(String dictpath)");
			return null;
		}

		if (prop.getProperty(query) != null) {
			String translatedWord = prop.getProperty(query);
						
			//debug
			System.out.println("TranslationUtility.searchInDict - query : " + query + " -- translated word : " + translatedWord);
			
			return translatedWord;
		} else {
			
			//debug
			System.out.println("TranslationUtility.searchInDict - query : " + query + " -- translated word : ");
			
			return query;
		}

	}

	/**
	 * Search a composite German word (text) separated by space for translation
	 * 
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
			
			System.out.print("TranslationUtility.searchTextInDict - ");
			return result.toString().trim();
		} else {
			
			System.out.print("TranslationUtility.searchTextInDict - ");
			return searchInDict(querytext);
		}

	}

	
	/**
	 * Notify whether dictionary has been initialized
	 * 
	 * @return true if Dictionary has been initialized via initDictionary(String dictpath) 
	 */
	public static boolean isDictInitialized() {
		return isDictInitialized;
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
	
	
}


