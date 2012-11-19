package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;

public class Subsumption extends Composite {

	static ListBox listBox;
	static ListBox listBox_1;
	static ListBox listBox_2;
	static ListBox listBox_3;
	static Label lblCurrentQuery;
	
	public Subsumption() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setBorderWidth(1);
		verticalPanel.setSpacing(10);
		initWidget(verticalPanel);
		verticalPanel.setWidth("700px");
		
		Label lblExampleFindSensors = new Label("Example: Find Sensors measuring temperature and pressure of Oil. ");
		verticalPanel.add(lblExampleFindSensors);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("600px");
		
		Label lblConcept = new Label("Concept :");
		horizontalPanel.add(lblConcept);
		horizontalPanel.setCellHorizontalAlignment(lblConcept, HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel.setCellWidth(lblConcept, "200px");
		lblConcept.setWidth("200px");
		
		listBox = new ListBox();
		listBox.addChangeHandler(new Changer());
		listBox.addItem("Sensor");
		horizontalPanel.add(listBox);
		listBox.setWidth("100px");
		listBox.setVisibleItemCount(1);
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(5);
		verticalPanel.add(horizontalPanel_1);
		horizontalPanel_1.setWidth("600px");
		
		Label lblProperty = new Label("Property :");
		horizontalPanel_1.add(lblProperty);
		horizontalPanel_1.setCellHorizontalAlignment(lblProperty, HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel_1.setCellWidth(lblProperty, "200px");
		lblProperty.setWidth("200px");
		
		listBox_1 = new ListBox();
		listBox_1.addChangeHandler(new Changer());
		listBox_1.addItem("");
		listBox_1.addItem("measures");
		listBox_1.addItem("hasLocation");
		listBox_1.addItem("isSensorOf");
		horizontalPanel_1.add(listBox_1);
		listBox_1.setWidth("100px");
		listBox_1.setVisibleItemCount(1);
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setSpacing(5);
		verticalPanel.add(horizontalPanel_2);
		horizontalPanel_2.setWidth("600px");
		
		Label lblMeasuredProperty = new Label("Measured Property :");
		horizontalPanel_2.add(lblMeasuredProperty);
		horizontalPanel_2.setCellHorizontalAlignment(lblMeasuredProperty, HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel_2.setCellWidth(lblMeasuredProperty, "200px");
		lblMeasuredProperty.setWidth("200px");
		
		listBox_2 = new ListBox();
		listBox_2.addChangeHandler(new Changer());
		listBox_2.addItem("");
		listBox_2.addItem("Temperature");
		listBox_2.addItem("Pressure");
		listBox_2.addItem("Flow");
		horizontalPanel_2.add(listBox_2);
		listBox_2.setWidth("100px");
		listBox_2.setVisibleItemCount(1);
		
		Label lblFeatureOfInterest = new Label("Feature Of Interest :");
		horizontalPanel_2.add(lblFeatureOfInterest);
		horizontalPanel_2.setCellHorizontalAlignment(lblFeatureOfInterest, HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel_2.setCellWidth(lblFeatureOfInterest, "150px");
		lblFeatureOfInterest.setWidth("150px");
		
		listBox_3 = new ListBox();
		listBox_3.addChangeHandler(new Changer());
		listBox_3.addItem("");
		listBox_3.addItem("Oil");
		listBox_3.addItem("Air");
		horizontalPanel_2.add(listBox_3);
		listBox_3.setWidth("100px");
		listBox_3.setVisibleItemCount(1);
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setSpacing(5);
		verticalPanel.add(horizontalPanel_3);
		horizontalPanel_3.setWidth("600px");
		
		Label lblQuery = new Label("Query :");
		horizontalPanel_3.add(lblQuery);
		horizontalPanel_3.setCellWidth(lblQuery, "80px");
		lblQuery.setWidth("80px");
		
		
		
		lblCurrentQuery = new Label("");
		lblCurrentQuery.setStyleName("query-label");
		horizontalPanel_3.add(lblCurrentQuery);
		lblCurrentQuery.setWidth("400px");
		
		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		horizontalPanel_4.setSpacing(5);
		verticalPanel.add(horizontalPanel_4);
		horizontalPanel_4.setWidth("180px");
		
		Button btnNewButton = new Button("Execute");
		horizontalPanel_4.add(btnNewButton);
		horizontalPanel_4.setCellWidth(btnNewButton, "80px");
		
		Button btnNewButton_1 = new Button("Reset");
		horizontalPanel_4.add(btnNewButton_1);
		horizontalPanel_4.setCellWidth(btnNewButton_1, "80px");
		
		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		horizontalPanel_5.setSpacing(5);
		verticalPanel.add(horizontalPanel_5);
		horizontalPanel_5.setWidth("600px");
		
		Label lblResult = new Label("Result :");
		horizontalPanel_5.add(lblResult);
		horizontalPanel_5.setCellWidth(lblResult, "80px");
		lblResult.setWidth("80px");
		
		TextArea textArea = new TextArea();
		textArea.setVisibleLines(5);
		horizontalPanel_5.add(textArea);
		textArea.setWidth("400px");
	}
	
	static class Changer implements ChangeHandler{

		@Override
		public void onChange(ChangeEvent event) {
			resetQueryLabel();
		}
	} 
	
	private static void resetQueryLabel() {
		String query  = listBox.getItemText(listBox.getSelectedIndex()) + " AND " + listBox_1.getItemText(listBox_1.getSelectedIndex()) + " AND " + listBox_2.getItemText(listBox_2.getSelectedIndex()) + 
				" AND " + listBox_3.getItemText(listBox_3.getSelectedIndex());
		lblCurrentQuery.setText(query);
	}

}
