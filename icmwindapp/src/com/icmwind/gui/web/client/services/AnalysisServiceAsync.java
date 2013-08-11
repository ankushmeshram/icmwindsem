/**
 * 
 */
package com.icmwind.gui.web.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author anme05
 *
 */
public interface AnalysisServiceAsync {
	
	public void initAnalysis(String ontURI, AsyncCallback<String> callback);
	
	public void initSDRE(String configFilePath, AsyncCallback<Boolean> callback);
	
	public void sensorRedundancyCheck(AsyncCallback<List<String>> callback);
	
	

}

