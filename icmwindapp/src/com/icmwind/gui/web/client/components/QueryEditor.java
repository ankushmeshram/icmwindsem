package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.icmwind.gui.web.client.helpers.ClientUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class QueryEditor extends Composite {

	final ListBox conceptListBox;
	final ListBox relationListBox;
	final ListBox objectListBox;
	final ListBox connectorListBox;
	
	public QueryEditor() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		initWidget(panel);
		panel.setWidth("900px");
		
		FlexTable flexTable = new FlexTable();
		panel.add(flexTable);
		flexTable.setWidth("700px");
		
		Label lblConcept = new Label("CONCEPT");
		lblConcept.setStyleName("makeItCenter");
		flexTable.setWidget(0, 0, lblConcept);
		lblConcept.setWidth("150px");
		
		Label lblRelation = new Label("RELATION");
		lblRelation.setStyleName("makeItCenter");
		flexTable.setWidget(0, 1, lblRelation);
		lblRelation.setWidth("150px");
		
		Label lblObject = new Label("OBJECT");
		lblObject.setStyleName("makeItCenter");
		flexTable.setWidget(0, 2, lblObject);
		lblObject.setWidth("150px");
		
		Label lblAndor = new Label("AND/OR/DELETE");
		lblAndor.setStyleName("makeItCenter");
		flexTable.setWidget(0, 3, lblAndor);
		lblAndor.setWidth("150px");
		
		conceptListBox = new ListBox();
		fillList(conceptListBox, "concept");
		
		conceptListBox.setDirectionEstimator(false);
		flexTable.setWidget(1, 0, conceptListBox);
		conceptListBox.setWidth("150px");
		conceptListBox.setVisibleItemCount(1);
		
		relationListBox = new ListBox();
		fillList(relationListBox, "property");
		flexTable.setWidget(1, 1, relationListBox);
		relationListBox.setWidth("150px");
		relationListBox.setVisibleItemCount(1);
		
		objectListBox = new ListBox();
		fillList(objectListBox, "measured-property");
		flexTable.setWidget(1, 2, objectListBox);
		objectListBox.setWidth("150px");
		objectListBox.setVisibleItemCount(1);
		
		connectorListBox = new ListBox();
		connectorListBox.addItem("");
		connectorListBox.addItem("AND");
		connectorListBox.addItem("OR");
		connectorListBox.addItem("DELETE");
		flexTable.setWidget(1, 3, connectorListBox);
		connectorListBox.setWidth("150px");
		connectorListBox.setVisibleItemCount(1);
		
		Button btnSave = new Button("Save");
		panel.add(btnSave);
		
		HorizontalPanel queryHPanel = new HorizontalPanel();
		panel.add(queryHPanel);
		queryHPanel.setWidth("700px");
		
		Label lblNewLabel = new Label("QUERY");
		lblNewLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		queryHPanel.add(lblNewLabel);
		queryHPanel.setCellWidth(lblNewLabel, "100px");
		lblNewLabel.setWidth("100px");
		
		final Label queryLabel = new Label("New label");
		queryLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		queryHPanel.add(queryLabel);
		
		btnSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String query = conceptListBox.getItemText(conceptListBox.getSelectedIndex()) + "." + relationListBox.getItemText(relationListBox.getSelectedIndex()) + "(" +
								objectListBox.getItemText(objectListBox.getSelectedIndex()) + ") " + connectorListBox.getItemText(connectorListBox.getSelectedIndex()); 
				queryLabel.setText(query);
			}
		});
	}
	
//	private static class listChangerHandler implements ChangeHandler {
//
//		@Override
//		public void onChange(ChangeEvent event) {
//			ListBox listBox = (ListBox)event.getSource();
//			String text = listBox.getItemText(listBox.getSelectedIndex());
//			
//		}
//		
//	}
	
	private void fillList(ListBox listBox, String key) {
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
	}

}
