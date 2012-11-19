package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.icmwind.gui.web.client.helpers.Utils;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class Report extends Composite {

	public Report() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("timepass-report");
		verticalPanel.setSpacing(10);
		initWidget(verticalPanel);
		verticalPanel.setSize("900px", "600px");
		
		Label lblThisIsSession = new Label("This is Session Report Page. Page under development.");
		lblThisIsSession.setStyleName("query-label");
		lblThisIsSession.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblThisIsSession);
		Utils.setTitle("Session Report");
		
	}

}
