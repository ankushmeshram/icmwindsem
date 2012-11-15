package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Analyse extends Composite {

	public Analyse() {
		
		Label label = new Label("Analyse.");
		initWidget(label);
//		RootPanel.get("titleContainer").clear();
//		RootPanel.get("titleContainer").add(new HTML("Analyse Sensor Data"));
	}

}
