/**
 * 
 */
package com.icmwind.gui.web.client.services;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * @author anme05
 *
 */
@RemoteServiceRelativePath("AnalysisService")
public interface AnalysisService extends RemoteService {
	
	public static class AnalysisServiceUtil {
		private static AnalysisServiceAsync instance;
		public static AnalysisServiceAsync getInstance() {
			if(instance == null) {
				instance = GWT.create(AnalysisService.class);
			}
			return instance;
		}
	}
	
	public String initAnalysis(String ontURI);
	
	public boolean initSDRE(String configFilePath);
	
	public List<String> sensorRedundancyCheck();
	
	

}

