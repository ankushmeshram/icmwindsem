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

import java.util.List;

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
	 * @return
	 */
	public String initRDFEncoder();
	
	/**
	 * @param path
	 * @return
	 */
	public String fileToEncode(String path);
	
	/**
	 * @return
	 * @gwt.typeArgs <client.helpers.FoundMatch>
	 */
	public List<FoundMatch> mosMap();
	
	
}
