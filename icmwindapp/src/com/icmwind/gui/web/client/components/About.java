package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.icmwind.gui.web.client.helpers.ClientUtils;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class About extends Composite {

	public About() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		panel.setStyleName("timepass-about");
		initWidget(panel);
		panel.setSize("800px", "");
		
		Label lblNewLabel = new Label("This is About Page. Page under development.");
		lblNewLabel.setStyleName("query-label");
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(lblNewLabel);
		ClientUtils.setTitle("ICM-Wind Project");
	}

}
