package com.icmwind.gui.web.client.helpers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author anme05
 * 
 * Class to hold mapping from RDFEncoding.returnMapping(). Stores KEY in (String)query and VALUE in (List<String>)matches
 * Usage of @gwt.typeArgs to help GWT Complier to prepare for storing Object types in Collections.
 *
 */
public class FoundMatch implements IsSerializable {
	
	private String query;
		
	/**
	 * @gwt.typeArgs <java.lang.String>
	 */
	private List<String> matches = new ArrayList<String>();

	public FoundMatch() {}


	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}


	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}


	/**
	 * @return the matches
	 * @gwt.typeArgs <java.lang.String>
	 */
	public List<String> getMatches() {
		return matches;
	}


	/**
	 * @param matches the matches to set
	 */
	public void setMatches(List<String> matches) {
		this.matches = matches;
	}
	

}
