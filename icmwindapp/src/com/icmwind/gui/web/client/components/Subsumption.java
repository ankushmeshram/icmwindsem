package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.icmwind.gui.web.client.helpers.ClientUtils;

public class Subsumption extends Composite {
	static Label lblCurrentQuery;
	FlexTable flexTable;
	int flag = 1;
	
	public Subsumption() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setBorderWidth(1);
		panel.setSpacing(10);
		initWidget(panel);
		panel.setWidth("800px");
		
		Label lblExampleFindSensors = new Label("Example: Find Sensors measuring temperature and pressure of Oil. ");
		panel.add(lblExampleFindSensors);
		lblExampleFindSensors.setWidth("780px");
		
		VerticalPanel selectionVPanel = new VerticalPanel();
		panel.add(selectionVPanel);
		selectionVPanel.setSize("600px", "");
		
		flexTable = new FlexTable();
		selectionVPanel.add(flexTable);
		flexTable.setWidth("100%");
		
		Label lblNewLabel = new Label("");
		lblNewLabel.setStyleName("gwt-Label-Subsumption-columns");
		flexTable.setWidget(0, 0, lblNewLabel);
		
		Label lblNewLabel_1 = new Label("");
		lblNewLabel_1.setStyleName("gwt-Label-Subsumption-columns");
		flexTable.setWidget(0, 1, lblNewLabel_1);
		
		Label lblNewLabel_2 = new Label("");
		lblNewLabel_2.setStyleName("gwt-Label-Subsumption-columns");
		flexTable.setWidget(0, 2, lblNewLabel_2);
		
		Label lblNewLabel_3 = new Label("");
		lblNewLabel_3.setStyleName("gwt-Label-Subsumption-columns");
		flexTable.setWidget(0, 3, lblNewLabel_3);

		addConceptRow();
		
		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setSpacing(5);
		panel.add(horizontalPanel_3);
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
		panel.add(horizontalPanel_4);
		horizontalPanel_4.setWidth("180px");
		
		Button btnNewButton = new Button("Execute");
		horizontalPanel_4.add(btnNewButton);
		horizontalPanel_4.setCellWidth(btnNewButton, "80px");
		
		Button btnNewButton_1 = new Button("Reset");
		horizontalPanel_4.add(btnNewButton_1);
		horizontalPanel_4.setCellWidth(btnNewButton_1, "80px");
		
		HorizontalPanel horizontalPanel_5 = new HorizontalPanel();
		horizontalPanel_5.setSpacing(5);
		panel.add(horizontalPanel_5);
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
	
	private Label getFlexLabel(String labelText) {
		Label label = new Label(labelText);
		label.addStyleName("gwt-Label-Subsumption-columns");
		
		return label;
	}
	
	private ListBox getFlexListBox(String key) {
		final ListBox listBox = new ListBox();
		listBox.setWidth("150px");
		listBox.setVisibleItemCount(1);
		
		String[] listData = null;
		
		
		if(key.equals("sub-system")){
			listData = ClientUtils.getSubsystems();
		} else if (key.equals("property")) {
			listData = ClientUtils.getProperties();
		} else if (key.equals("measured-property")) {
			listData = ClientUtils.getMeasuredproperties();
		} else if (key.equals("feature")) {
			listData = ClientUtils.getFeatureofinterests();
		} else if (key.equals("location")) {
			listData = ClientUtils.getLocations();
		} else if (key.equals("concept")) {
			listData = ClientUtils.getConcepts();
		} 
		
		for(int i = 0; i < listData.length; i++) {
			listBox.addItem(listData[i]);
		}
		
		if(key.equals("property")) {
			listBox.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					String selectedItem = listBox.getItemText(listBox.getSelectedIndex());
															
					if(selectedItem.equals("measures")) {
						addMeasuredPropertyRow();
					} else if (selectedItem.equals("hasLocation")) {
						addLocationRow();
					} else if (selectedItem.equals("isSensorOf")) {
						addSubSystemRow();
					}
					
					listBox.setEnabled(false);
				}
			});
		}
			
		return listBox;
	}
	
	private void addConceptRow() {
		int row = flexTable.getRowCount();
	
		flexTable.setWidget(row, 0, getFlexLabel("Concept :"));
		flexTable.setWidget(row, 1, getFlexListBox("concept"));
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		flexTable.setWidget(3, 0, horizontalPanel);
		horizontalPanel.setWidth("100%");
		
		Button btnAddProperty = new Button("Add Property");
		btnAddProperty.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addPropertyRow();
			}
		});
		
		horizontalPanel.add(btnAddProperty);
		flexTable.getFlexCellFormatter().setColSpan(3, 0, 4);
	}
	
	private void addPropertyRow() {
		int row = flexTable.getRowCount();
		
		flexTable.setWidget(row, 0, getFlexLabel("Property :"));
		flexTable.setWidget(row, 1, getFlexListBox("property"));
	}
	
		
	private void addMeasuredPropertyRow() {
		int row = 0;
		
		if(flag == 1) {
			row = flexTable.getRowCount();
			flag = 0;
		} else {
			row = flexTable.getRowCount() - 1;
			flexTable.removeCells(row - 1, 2, 2);
			flexTable.removeRow(row);
			row = flexTable.getRowCount();
//			row = flexTable.getRowCount() - 1;
		}
		
		
		Label measPropLabel = new Label("Measured Property :");
		measPropLabel.addStyleName("gwt-Label-Subsumption-columns");
		
		Label featOfIntLabel = new Label("Feature Of Interest :");
		featOfIntLabel.addStyleName("gwt-Label-Subsumption-columns");
		
		final Button removeProperty = new Button("X", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println();
				
			}
		});
		
		flexTable.setWidget(row - 1, 2, getFlexLabel("Measured Property :"));
		flexTable.setWidget(row - 1, 3, getFlexListBox("measured-property"));
		flexTable.setWidget(row, 2, getFlexLabel("Feature Of Interest :"));
		flexTable.setWidget(row, 3, getFlexListBox("feature"));
		flexTable.setWidget(row - 1, 4, removeProperty);
	}
	
	
	private void addLocationRow() {
		int row = 0;
		
		if(flag == 1) {
			row = flexTable.getRowCount();
			flag = 0;
		} else {
			row = flexTable.getRowCount() - 1;
			flexTable.removeRow(row);
			row = flexTable.getRowCount();
//			row = flexTable.getRowCount() - 1;
		}
		
//		if(flag == 1) {
//			row = flexTable.getRowCount();
//			flag = 0;
//		} else {
//			row = flexTable.getRowCount() - 1;
//		}
				
		flexTable.setWidget(row - 1, 2, getFlexLabel("Location :"));
		flexTable.setWidget(row - 1, 3, getFlexListBox("location"));
	}
	
		
	private void addSubSystemRow() {
		int row = 0;
		
		if(flag == 1) {
			row = flexTable.getRowCount();
			flag = 0;
		} else {
			row = flexTable.getRowCount() - 1;
			flexTable.removeRow(row);
			row = flexTable.getRowCount();
//			row = flexTable.getRowCount() - 1;
		}
		
//		if(flag == 1) {
//			row = flexTable.getRowCount();
//			flag = 0;
//		} else {
//			row = flexTable.getRowCount() - 1;
//		}
		
		flexTable.setWidget(row - 1, 2, getFlexLabel("Sub Sytem :"));
		flexTable.setWidget(row - 1, 3, getFlexListBox("sub-system"));
	}

}
