package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DisclosurePanel;

public class Factual extends Composite {

	public Factual() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		initWidget(panel);
		panel.setSize("900px", "");
		
		Label lblNewLabel = new Label("Queries with respect to data content of the knowledgebase can be asked here. Queries are grouped in basic types. User can modify them and see the result.");
		panel.add(lblNewLabel);
		
		DisclosurePanel disclosurePanel = new DisclosurePanel("Find relations between given objects", false);
		disclosurePanel.setStyleName("gwt-DisclosurePanel-my gwt-DisclosurePanel-my-closed");
		disclosurePanel.setAnimationEnabled(true);
		panel.add(disclosurePanel);
		disclosurePanel.setWidth("880px");
		
		DisclosurePanel disclosurePanel_1 = new DisclosurePanel("Retrieve data with certain characteristics", false);
		disclosurePanel_1.setStyleName("gwt-DisclosurePanel-my gwt-DisclosurePanel-my-closed");
		panel.add(disclosurePanel_1);
		disclosurePanel_1.setWidth("880px");
	}

}
