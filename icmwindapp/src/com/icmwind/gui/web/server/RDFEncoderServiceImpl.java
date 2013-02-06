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
package com.icmwind.gui.web.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.icmwind.core.RDFEncoder;
import org.icmwind.core.impl.RDFEncodingImpl;

import com.icmwind.gui.web.client.helpers.FoundMatch;
import com.icmwind.gui.web.client.services.RDFEncoderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author anme05
 *
 * RemoteServiceServlet for RemoteService RDFEncoderService.
 */
public class RDFEncoderServiceImpl extends RemoteServiceServlet implements RDFEncoderService {

	private static final long serialVersionUID = 1L;
	
	private RDFEncoder rdfencoder = RDFEncodingImpl.getInstance();

	// Initialize encoding environment 
	@Override
	public String initRDFEncoder() {
		rdfencoder.initOntologyProcess();
		return "Success OntoProc";
	}

	// Pass file to be encoded
	@Override
	public String fileToEncode(String path) {
		rdfencoder.initFileProcess(path);
		return "Success FileProc";
	}

	// Return MatchOrSuggest mapping as List<FoundMatch>, where FoundMatch is encapsulation for each mapping.
	public List<FoundMatch> mosMap() {
		List<FoundMatch> tempList = new ArrayList<FoundMatch>();
		for(Entry<String, List<String>> entry : rdfencoder.returnMapping().entrySet()) {
			FoundMatch fm = new FoundMatch();
			fm.setQuery(entry.getKey());
			fm.setMatches(entry.getValue());
			tempList.add(fm);
		}
		return tempList;
	}

	
	
}
