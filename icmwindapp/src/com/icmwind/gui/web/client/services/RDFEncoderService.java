/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.icmwind.gui.web.client.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.icmwind.gui.web.client.helpers.FoundMatch;

/**
 * @author anme05
 * 
 * RemoteService RDFEncoderService and Method Signatures.
 */
@RemoteServiceRelativePath("RDFEncoderService")
public interface RDFEncoderService extends RemoteService {
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	
	public static class RDFEncoderServiceUtil {
		private static RDFEncoderServiceAsync instance;
		public static RDFEncoderServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(RDFEncoderService.class);
			}
			return instance;
		}
	}
	
	/**
	 * Initialize the RDFEncoder
	 * 
	 * @return
	 */
	public String initRDFEncoder();
	
	
	/**
	 * Send information about the data source/ wind turbine
	 * 
	 * @param dsInfoMap
	 */
	public void setDataSourceInfo(Map<String, String> dsInfoMap);
	
	/**
	 * Read the uploaded file, path is taken from GlobalInitliazer 
	 * 
	 * @param path
	 * @return
	 */
	public String readUploadedFile();
	
	/**
	 * Get MatchOrSuggest map as List of FoundMatch objects
	 * 
	 * @return
	 * @gwt.typeArgs <client.helpers.FoundMatch>
	 */
	public List<FoundMatch> getMatchOrSuggestMap();
	
	
	/**
	 * Get summary of period of the uploaded file as DataFileSummary object
	 * - number of observations
	 * - begin and end dates in data file
	 * 
	 * @return
	 */
	public Map<String, String> getDataFileSummary();
	
	/**
	 * Encode the uploaded file with the properly mapped column names, duration of analysis 
	 * 
	 * @param headToClassNameMap
	 * @param beginAnalysisPeriod TODO
	 * @param endAnalysisPeriod TODO
	 * @gwt.typeArgs java.util.Map<java.lang.String, java.lang.String>
	 */
	public boolean encodeFile(Map<String, String> headToClassNameMap, Date beginAnalysisPeriod, Date endAnalysisPeriod);

	
	public String returnEncodedFile(Map<String, String> headToClassNameMap, Date beginAnalysisPeriod, Date endAnalysisPeriod);
	
}
