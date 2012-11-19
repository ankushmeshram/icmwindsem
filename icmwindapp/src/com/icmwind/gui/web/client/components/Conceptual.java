package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;

public class Conceptual extends Composite {

	public Conceptual() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		initWidget(panel);
		panel.setWidth("900px");
		
		HTML htmlNewHtml = new HTML("Queries regarding the conceptual knowledge of Wind Turbine domain particularly Fluid Condition Monitoring can be asked here.", true);
		panel.add(htmlNewHtml);
		
		Label lblNewLabel = new Label("Queries are grouped in basic types. User can modify them and see the result.");
		panel.add(lblNewLabel);
		
		DisclosurePanel disclosurePanel = new DisclosurePanel("Find Sensors with certain functionality");
		disclosurePanel.setStyleName("gwt-DisclosurePanel-my gwt-DisclosurePanel-my-closed");
		disclosurePanel.setAnimationEnabled(true);
		panel.add(disclosurePanel);
		disclosurePanel.setWidth("800px");
		
		Subsumption subsumption = new Subsumption();
		disclosurePanel.setContent(subsumption);
		subsumption.setSize("800px", "4cm");
		
		DisclosurePanel disclosurePanel_1 = new DisclosurePanel("Find Sensors similar to given definition");
		disclosurePanel_1.setStyleName("gwt-DisclosurePanel-my gwt-DisclosurePanel-my-closed");
		panel.add(disclosurePanel_1);
		disclosurePanel_1.setWidth("800px");
	}

}
