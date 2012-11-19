package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.icmwind.gui.web.client.helpers.Utils;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class AdvancedAnalysis extends Composite {

	public AdvancedAnalysis() {
		Utils.setTitle("Advanced Data Analysis");
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(20);
		initWidget(panel);
		panel.setWidth("900px");
		
		String introText = "Users with knowledge about Semantic Web technologies can additionally define their queries via predefined conceptual and data centric query templates";
		HTML introHtml = new HTML(introText, true);
		panel.add(introHtml);
		introHtml.setWidth("900px");
		
		String cqText = "<b>Conceptual Queries</b><br/>" +
						"Queries regarding the conceptual knowledge of Wind Turbine domain particularly Fluid Condition " +
						"Monitoring can be asked here. User can query for Sensors with certain functionalities. Another type of " +
						"query is to find Sensors with similar functionalities";
		HTML cqHtml = new HTML("<b>Conceptual Queries</b><br/>Queries regarding the conceptual knowledge of Wind Turbine domain particularly Fluid Condition Monitoring can be asked here. User can query for Sensors with certain functionalities. Another type of query is to find Sensors with similar functionalities", true);
		panel.add(cqHtml);
		
		String dcqText = "<b>Data Centric Queries</b><br/>" +
						 "Queries with respect to data content of the knowledgebase can be asked here. " +
						 "One can retrieve data with certain properties. Also, one can find logical relations between given two Sensors or data values.";
		HTML dcqHtml = new HTML(dcqText, true);
		panel.add(dcqHtml);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setSpacing(5);
		panel.add(horizontalPanel);
		horizontalPanel.setWidth("800px");
		
		Button btnConceptualQueries = new Button("Conceptual Queries");
		btnConceptualQueries.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				History.newItem("conceptual");
			}
		});
		horizontalPanel.add(btnConceptualQueries);
		btnConceptualQueries.setWidth("150px");
		
		Button btnDataCentricQueries = new Button("Data Centric Queries");
		btnDataCentricQueries.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				History.newItem("factual");
			}
		});
		horizontalPanel.add(btnDataCentricQueries);
		btnDataCentricQueries.setWidth("200px");
	}

}
