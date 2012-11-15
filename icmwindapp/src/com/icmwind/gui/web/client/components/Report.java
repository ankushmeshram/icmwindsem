package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;

public class Report extends Composite {

	public Report() {
		
		Label lblThisReportPage = new Label("This Report Page.");
		initWidget(lblThisReportPage);
		RootPanel.get("titleContainer").clear();
		RootPanel.get("titleContainer").add(new HTML("Session Report"));
	}

}
