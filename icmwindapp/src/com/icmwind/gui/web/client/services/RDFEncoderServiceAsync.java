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
import com.icmwind.gui.web.client.helpers.ObservationPeriod;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author anme05
 * 
 * RemoteService RDFEncoderService and Method Signatures.
 */
public interface RDFEncoderServiceAsync {
	
	public void initRDFEncoder(AsyncCallback<String> callback);
	
	/**
	 * @param path
	 */
	public void readUploadedFile(AsyncCallback<String> callback);
	
	public void getMatchOrSuggestMap(AsyncCallback<List<FoundMatch>> callback);
	
	
	/**
	 * Get Observation period of the uploaded file
	 */
	public void getObservationPeriod(AsyncCallback<ObservationPeriod> callback);
	
	/**
	 * @param headToClassNameMap
	 */
	public void encodeFile(Map<String, String> headToClassNameMap, AsyncCallback<Boolean> callback);
	
	
}
