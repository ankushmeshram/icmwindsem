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

import com.icmwind.gui.web.client.helpers.FoundMatch;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author anme05
 * 
 * RemoteService RDFEncoderService and Method Signatures.
 */
public interface RDFEncoderServiceAsync {
	
	/**
	 * Initialize the RDFEncoder
	 */
	public void initRDFEncoder(AsyncCallback<String> callback);
	
	
	/**
	 * Send information about the data source/ wind turbine
	 * 
	 * @param dsInfoMap
	 */
	public void setDataSourceInfo(Map<String, String> dsInfoMap, AsyncCallback<Void> callback);
	
	/**
	 * Read the uploaded file, path is taken from GlobalInitliazer 
	 * 
	 * @param path
	 */
	public void readUploadedFile(AsyncCallback<String> callback);
	
	/**
	 * Get MatchOrSuggest map as List of FoundMatch objects
	 */
	public void getMatchOrSuggestMap(AsyncCallback<List<FoundMatch>> callback);
	
	
	/**
	 * Get summary of period of the uploaded file as DataFileSummary object
	 * - number of observations
	 * - begin and end dates in data file
	 */
	public void getDataFileSummary(AsyncCallback<Map<String, String>> callback);
	
	/**
	 * Encode the uploaded file with the properly mapped column names, duration of analysis 
	 * 
	 * @param headToClassNameMap
	 * @param beginAnalysisPeriod TODO
	 * @param endAnalysisPeriod TODO
	 */
	public void encodeFile(Map<String, String> headToClassNameMap, Date beginAnalysisPeriod, Date endAnalysisPeriod, AsyncCallback<Boolean> callback);

	
	public void returnEncodedFile(Map<String, String> headToClassNameMap, Date beginAnalysisPeriod, Date endAnalysisPeriod, AsyncCallback<String> callback);
	
}
