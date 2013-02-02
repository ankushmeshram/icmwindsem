package org.icmwind.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anme05
 * 
 * Clas for Normalization
 *
 */
public class Normalization {
	
	private Map<String, String> normalizationMap = new HashMap<String, String>();
	
	/**
	 * @param textToNormalize
	 *            Text to "normalize". In normalization, extra white spaces, %,
	 *            _ are removed and text is converted to lower case.
	 * @return normalized text in lower case
	 */
	public String normalizeText(String textToNormalize) {
		return textToNormalize.replaceAll("\\s{2,}+|-|_", " ")
				.replaceAll("%", "").toLowerCase();
	}

	/**
	 * @param listToNormalize
	 *            List of Text to "normalize". In normalization, extra white
	 *            spaces, %, _ are removed and text is converted to lower case.
	 * @return List of normalized text
	 */
	public List<String> normalizeList(List<String> listToNormalize) {
		List<String> tempList = new ArrayList<>(listToNormalize.size());
		
		if(!this.normalizationMap.isEmpty())
			this.normalizationMap.clear();
		
		for (String text : listToNormalize) {
			String normtext = normalizeText(text);
			tempList.add(normtext);
			this.normalizationMap.put(normtext, text);
		}
		
		return tempList;
	}

	/**
	 * @param textToNormalize
	 *            Text to "normalize and translate". Use
	 *            Utility.normalizeText(String textToNormalize) to normalize and
	 *            then translate it using Dictionary. In normalization, extra
	 *            white spaces, %, _ are removed and text is converted to lower
	 *            case. In translation, query is searched against Ger-Eng
	 *            Dictionary
	 * @return Translated normalized text
	 */
	public String normalizeTextWithTranslation(String textToNormalize) {
		if (!Utility.isDictInitialized()) {
			System.out.println("ERROR: Call Utility.initDictionary(String dictPath) before invoking Utility.normalizeTextWithTranslation(String textToNormalize).");
			System.exit(0);
		}

		return Utility.searchTextInDict(this.normalizeText(textToNormalize));

		// DEBUG
		// String normtext = normalizeText(textToNormalize);
		// System.out.println("SYSTEM: normTextWiTrans - normtext: " +
		// normtext);
		// return searchTextInDict(normtext);
	}

	/**
	 * @param listToNormalize
	 *            List of text to "normalize and translate". In normalization,
	 *            extra white spaces, %, _ are removed and text is converted to
	 *            lower case. In translation, query is searched against Ger-Eng
	 *            Dictionary
	 * @return Translated normalized list of text
	 */
	public List<String> normalizeListWithTranslation(List<String> listToNormalize) 
	{
		List<String> tempList = new ArrayList<>(listToNormalize.size());
		
		if(!this.normalizationMap.isEmpty())
			this.normalizationMap.clear();
		
		for (String text : listToNormalize) {
			String normtranstext = normalizeTextWithTranslation(text);
			tempList.add(normtranstext);
			this.normalizationMap.put(normtranstext, text);

			// DEBUG
			// String normtranstext = normalizeTextWithTranslation(text);
			// System.out.println("SYSTEM: normListWTrans - normtransnext: " +
			// normtranstext);
			// tempList.add(normtranstext);
		}
		return tempList;
	}

	/**
	 * @return the normalizationMap with "normalized_text-text"
	 */
	public  Map<String, String> getNormalizationMap() {
		if(this.normalizationMap.isEmpty())
			return Collections.emptyMap();
		else
			return this.normalizationMap;
	}

}
