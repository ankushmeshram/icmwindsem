package com.icmwind.gui.web.client.components;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import arq.query;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.icmwind.gui.web.client.helpers.FoundMatch;
import com.icmwind.gui.web.client.helpers.GlobalInitializer;
import com.icmwind.gui.web.client.services.AnalysisService.AnalysisServiceUtil;
import com.icmwind.gui.web.client.services.GlobalInitializerService;
import com.icmwind.gui.web.client.services.RDFEncoderService;
import com.icmwind.gui.web.client.services.RDFEncoderService.RDFEncoderServiceUtil;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.HTML;

public class F extends Composite {

	private HashMap<String, String> headToClassNameMap;
	private Date minDate = null;
	private Date maxDate = null;
	
	private String ontPath = null;
	private String ontURI = null;
	
		
	// TODO change later
//	private Map<String, ListBox> mapListMap;
	
	
	public F() {
		
		// TODO change later
//		mapListMap = new HashMap<String, ListBox>();
		
		headToClassNameMap = new HashMap<String, String>();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSpacing(10);
		initWidget(verticalPanel);
		verticalPanel.setWidth("600px");
		
		CaptionPanel cptnpnlDataFile = new CaptionPanel("Query Type Selection");
		cptnpnlDataFile.setStyleName("blabla");
		verticalPanel.add(cptnpnlDataFile);
		cptnpnlDataFile.setWidth("500px");
		
		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		cptnpnlDataFile.setContentWidget(horizontalPanel_2);
		horizontalPanel_2.setWidth("500px");
		
		Label lblSelectQueryType = new Label("Select Query Type : ");
		horizontalPanel_2.add(lblSelectQueryType);
		
		final ListBox listBox = new ListBox();
		listBox.addItem("-------------Informational---------");
		listBox.addItem("Find Sensor Requirements");
		listBox.addItem("Check Sensor Redundancy");
		listBox.addItem("Get FCM Configuration");
		listBox.addItem("-------------Recognition-----------");
		listBox.addItem("Check Faults");
		listBox.addItem("Check Filter Blocakge");
		listBox.addItem("Check Thermo Bypass Valve Fault");
		listBox.addItem("---------------Diagnosis------------");
		listBox.addItem("Diagnose");
		horizontalPanel_2.add(listBox);
		listBox.setVisibleItemCount(1);
		
				
		CaptionPanel cptnpnlMapping = new CaptionPanel("Query Description");
		cptnpnlMapping.setStyleName("blabla");
		verticalPanel.add(cptnpnlMapping);
		cptnpnlMapping.setWidth("500px");
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		cptnpnlMapping.setContentWidget(verticalPanel_2);
		verticalPanel_2.setSpacing(5);
		verticalPanel_2.setWidth("500px");
		
		final HTML qDescHtml = new HTML("Blah blah bla </br> blah blah", true);
		verticalPanel_2.add(qDescHtml);
		
		CaptionPanel cptnpnlQuery = new CaptionPanel("Query");
		cptnpnlQuery.setStyleName("blabla");
		verticalPanel.add(cptnpnlQuery);
		cptnpnlQuery.setWidth("500px");
		
		VerticalPanel verPanelAnaPeriod = new VerticalPanel();
		verPanelAnaPeriod.setSpacing(5);
		cptnpnlQuery.setContentWidget(verPanelAnaPeriod);
		verPanelAnaPeriod.setWidth("500px");
		
		final TextArea queryTxtArea = new TextArea();
		verPanelAnaPeriod.add(queryTxtArea);
		queryTxtArea.setSize("493px", "89px");
		
		final Button btnExecuteQuery = new Button("EXECUTE QUERY");
		verticalPanel.add(btnExecuteQuery);
		
		CaptionPanel cptnpnlEncode = new CaptionPanel("Results");
		cptnpnlEncode.setStyleName("blabla");
		verticalPanel.add(cptnpnlEncode);
		cptnpnlEncode.setWidth("500px");
		
		final HTML resultHtml = new HTML("New HTML", true);
		cptnpnlEncode.setContentWidget(resultHtml);
		resultHtml.setWidth("500px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setSpacing(5);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("500px");
		verticalPanel.setCellVerticalAlignment(horizontalPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Button btnNewQuery = new Button("New Query");
		horizontalPanel.add(btnNewQuery);
		
		Button btnHome = new Button("Home");
		horizontalPanel.add(btnHome);
		setWidth("600px");
		
				
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				
				int index = listBox.getSelectedIndex();
				
				if( index != 0 || index != 4 || index != 8 ) {
					
					if(index == 2) {
						
						String descTxt = 
								"This query finds the redundant sensors installed at the Wind Turbine. </br>" +
								" It's a SPARQL DL Query and...";
						qDescHtml.setText(descTxt);
						
						
						String queryTxt = 
								"# SPARQL DL Query </br>" +
								"Property and isPropertyOf some Oil </br> </br>" +
								"# Equivalence/leastSpecific Checks";
						queryTxtArea.setText(queryTxt);
						queryTxtArea.setEnabled(false);
						
						btnExecuteQuery.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								
								AnalysisServiceUtil.getInstance().sensorRedundancyCheck(new AsyncCallback<List<String>>() {
									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Error at AnalysisServiceUtil.getInstance().sensorRedundancyCheck.onFailure()");
									}

									@Override
									public void onSuccess(List<String> result) {
										
										StringBuilder sb = new StringBuilder();
										
										for(String item : result) {
											sb.append(item);
											sb.append("</br>");
										}
										
										resultHtml.setText(sb.toString());
									}
								});
							}
						});
												
												
						
					}
				}
											
			}
			
			
		});
		
	}
		
}
