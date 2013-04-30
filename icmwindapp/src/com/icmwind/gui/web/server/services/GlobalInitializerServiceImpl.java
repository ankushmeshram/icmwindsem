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
package com.icmwind.gui.web.server.services;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.icmwind.gui.web.client.helpers.GlobalInitializer;
import com.icmwind.gui.web.client.helpers.WindTurbine;
import com.icmwind.gui.web.client.services.GlobalInitializerService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GlobalInitializerServiceImpl extends RemoteServiceServlet implements GlobalInitializerService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public List<String> listWindTurbines() {
		return GlobalInitializer.get().listWindTurbines();
	}

	@Override
	public Map<String, String> getInfoFor(String wind_turbine) {
		System.out.println("GlobalInitializerServiceImpl.getInfoFor()");
		
		WindTurbine WT = new WindTurbine();
		WT.readWindTurbineInfo(wind_turbine);
				
		if(WT.getWindTurbineInfo().isEmpty()) {
			return Collections.emptyMap();
		} else {
			return WT.getWindTurbineInfo();
		}
	}

}
