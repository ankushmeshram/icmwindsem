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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.icmwind.core.RDFEncoder;
import org.icmwind.core.impl.RDFEncoderImpl;


import com.icmwind.gui.web.client.helpers.FoundMatch;
import com.icmwind.gui.web.client.helpers.GlobalInitializer;
import com.icmwind.gui.web.client.services.RDFEncoderService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author anme05
 *
 * RemoteServiceServlet for RemoteService RDFEncoderService.
 */
public class RDFEncoderServiceImpl extends RemoteServiceServlet implements RDFEncoderService {

	private static final long serialVersionUID = 1L;
	
	private RDFEncoder rdfencoder = RDFEncoderImpl.getInstance();

	// Initialize encoding environment 
	@Override
	public String initRDFEncoder() {
		rdfencoder.initOntologyProcess();
		rdfencoder.setEncodeStorage(GlobalInitializer.get().ONTOLOGIES_LOCATION);
		return "Success OntoProc";
	}

	
	// Read file from the path set in GlibalInitializer.UPLOADED_FILE_PATH
	@Override
	public String readUploadedFile() {
		String path = GlobalInitializer.get().getUploadedFilePath();
		
		if(path.equals("")) {
			return "Failure FileProc. File not found!";
		} else {
			rdfencoder.initFileProcess(path);
			return "Success FileProc";
		}
		
	}

	// Return MatchOrSuggest mapping as List<FoundMatch>, where FoundMatch is encapsulation for each mapping.
	public List<FoundMatch> getMatchOrSuggestMap() {
		List<FoundMatch> tempList = new ArrayList<FoundMatch>();
		for(Entry<String, List<String>> entry : rdfencoder.getMoSMap().entrySet()) {
			FoundMatch fm = new FoundMatch();
			fm.setQuery(entry.getKey());
			fm.setMatches(entry.getValue());
			tempList.add(fm);
		}
		return tempList;
	}

	@Override
	public boolean encodeFile(Map<String, String> mapping, Date beginAnalysisPeriod, Date endAnalysisPeriod) {
		rdfencoder.setHeaderToClassNamesMap(mapping);
		
		//Set Storage Folder for encoded files
		rdfencoder.setEncodeStorage("C:\\Users\\anme05\\git\\icmwindsem\\icmwindapp\\war\\data\\encoded");
		
		// Set analysis period
		rdfencoder.setAnalysisPeriod(beginAnalysisPeriod, endAnalysisPeriod);
		
		return rdfencoder.encode();
	}

	
	//CHECK HERE
	public static <K extends Comparable,V extends Comparable> Map<K,V> sortMapByValues(Map<K,V> unsortedMap) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K,V>>(unsortedMap.entrySet());
		
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public int compare(Entry<K, V> o1, Entry<K, V> o2) {
				// for descending order otherwise o1.getValue().compareTo(o2.getValue())				
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		Map<K, V> sortedMap = new LinkedHashMap<K, V>();
		
		for(Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedMap;
	}


	@Override
	public Map<String, String> getDataFileSummary() {
		return rdfencoder.getFileSummary();
		
/*		Map<String, String> fsMap = rdfencoder.getFileSummary();
		
		DataFileSummary dfs = new DataFileSummary();
		dfs.setDataFilePrimaryKey(fsMap.get("pk").toString());
		dfs.setNumberOfObservations(Integer.parseInt(fsMap.get("observations")));
		dfs.setBeginDate(DateTimeFormat.getFormat("EEE MMM dd hh:mm:ss zzz yyyy").parse(fsMap.get("begin")));
		dfs.setEndDate(DateTimeFormat.getFormat("EEE MMM dd hh:mm:ss zzz yyyy").parse(fsMap.get("end")));
		
		return dfs;
*/
	}

	@Override
	public void setDataSourceInfo(Map<String, String> dsInfoMap) {
		rdfencoder.setDataFileSourceInfo(dsInfoMap);
	}

		
}
