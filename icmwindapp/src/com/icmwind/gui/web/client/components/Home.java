package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Home extends Composite {

	public Home() {
		Label label = new Label("Home");
		initWidget(label);
		RootPanel.get("titleContainer").clear();
		RootPanel.get("titleContainer").add(new HTML("ICM-Wind Sem: Semantic Sensor Data Analysis"));
		
	}

}
