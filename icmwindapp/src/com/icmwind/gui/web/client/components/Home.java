package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.icmwind.gui.web.client.helpers.ClientUtils;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class Home extends Composite {

	public Home() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		panel.setStyleName("timepass");
		initWidget(panel);
		panel.setSize("800px", "");
		
		Label lblWorkInProgress = new Label("This is Home Page. Page under development.");
		lblWorkInProgress.setStyleName("query-label");
		lblWorkInProgress.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(lblWorkInProgress);
		ClientUtils.setTitle("ICM-Wind Sem: Semantic Sensor Data Analysis");;
	}
	
}
