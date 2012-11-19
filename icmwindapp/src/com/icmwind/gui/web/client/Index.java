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
package com.icmwind.gui.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.icmwind.gui.web.client.components.About;
import com.icmwind.gui.web.client.components.AdvancedAnalysis;
import com.icmwind.gui.web.client.components.Analyse;
import com.icmwind.gui.web.client.components.Conceptual;
import com.icmwind.gui.web.client.components.Footer;
import com.icmwind.gui.web.client.components.Home;
import com.icmwind.gui.web.client.components.Navigation;
import com.icmwind.gui.web.client.components.Report;
import com.icmwind.gui.web.client.components.Settings;
import com.icmwind.gui.web.client.components.UploadPlain;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {
	
	public void onModuleLoad() {
		
		RootPanel.get("contentContainer").add(new Home());
		RootPanel.get("navBarContainer").add(new Navigation());
		RootPanel.get("footerContainer").add(new Footer());
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				
				if(historyToken.equals("home")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new Home());
				} else if (historyToken.equals("about")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new About());
				} else if (historyToken.equals("upload")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new UploadPlain());
				} else if (historyToken.equals("analyse")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new Analyse());
				} else if (historyToken.equals("advanced")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new AdvancedAnalysis());
				} else if (historyToken.equals("report")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new Report());
				} else if (historyToken.equals("conceptual")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new Conceptual());
				} else if (historyToken.equals("settings")) {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new Settings());
				} else {
					RootPanel.get("contentContainer").clear();
					RootPanel.get("contentContainer").add(new Home());
				}
			}
		});
		
		History.fireCurrentHistoryState();
	}
}
