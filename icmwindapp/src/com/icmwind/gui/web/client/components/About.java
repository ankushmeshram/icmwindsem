package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class About extends Composite {

	public About() {
		Label label = new Label("About.");
		initWidget(label);
		RootPanel.get("titleContainer").clear();
		RootPanel.get("titleContainer").add(new HTML("ICM-Wind Project"));
	}

}
