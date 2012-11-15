package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;

public class Settings extends Composite {

	public Settings() {
		
		Label lblThisIsSettings = new Label("This is Settings.");
		initWidget(lblThisIsSettings);
		RootPanel.get("titleContainer").clear();
		RootPanel.get("titleContainer").add(new HTML("Settings"));
	}

}
