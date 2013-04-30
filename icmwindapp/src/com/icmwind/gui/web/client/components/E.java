package com.icmwind.gui.web.client.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.icmwind.gui.web.client.services.GlobalInitializerService;
import com.icmwind.gui.web.client.services.RDFEncoderService;
import com.icmwind.gui.web.client.services.RDFEncoderService.RDFEncoderServiceUtil;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.cellview.client.CellTable;

public class E extends Composite {
	
	FlexTable flexTable = new FlexTable();

	private HashMap<String, String> headToClassNameMap;
	// TODO change later
//	private Map<String, ListBox> mapListMap;
	
	
	public E() {
		
		// TODO change later
//		mapListMap = new HashMap<String, ListBox>();
		
		headToClassNameMap = new HashMap<String, String>();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSpacing(10);
		initWidget(verticalPanel);
		verticalPanel.setWidth("600px");
		
		CaptionPanel cptnpnlDataFile = new CaptionPanel("Data File");
		cptnpnlDataFile.setStyleName("blabla");
		verticalPanel.add(cptnpnlDataFile);
		cptnpnlDataFile.setWidth("500px");
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setAction("/eserv");
		cptnpnlDataFile.setContentWidget(formPanel);
		formPanel.setSize("450px", "");
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		formPanel.setWidget(verticalPanel_1);
		verticalPanel_1.setSize("100%", "100%");
		verticalPanel_1.setSpacing(5);
		
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("fileUpload");
		verticalPanel_1.add(fileUpload);
		
		Button btnLoad = new Button("LOAD");
		verticalPanel_1.add(btnLoad);
		
		btnLoad.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				formPanel.submit();
			}
		});
				
		formPanel.addSubmitHandler(new SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event) {
				if(!fileUpload.getFilename().endsWith(".csv")) {
					Window.alert("Choose a *.csv file.");
					event.cancel();
				}
			}
		});
		
				
		CaptionPanel cptnpnlMapping = new CaptionPanel("Mapping");
		cptnpnlMapping.setStyleName("blabla");
		verticalPanel.add(cptnpnlMapping);
		cptnpnlMapping.setWidth("500px");
		
		VerticalPanel verticalPanel_2 = new VerticalPanel();
		cptnpnlMapping.setContentWidget(verticalPanel_2);
		verticalPanel_2.setSpacing(5);
		verticalPanel_2.setWidth("500px");
		flexTable.setBorderWidth(1);
		
		verticalPanel_2.add(flexTable);
		flexTable.setWidth("500px");
		
		Label label_1 = new Label("Data File Column Name");
		flexTable.setWidget(0, 0, label_1);
		label_1.setStyleName("gwt-Label-Bold");
		label_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		label_1.setWidth("180px");
		
		Image image_4 = new Image("");
		flexTable.setWidget(0, 1, image_4);
		image_4.setSize("30px", "30px");
		
		Label label_2 = new Label("Mapped Ontology Class Name");
		flexTable.setWidget(0, 2, label_2);
		label_2.setStyleName("gwt-Label-Bold");
		label_2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		label_2.setWidth("180px");
		
		
		Button btnProceed = new Button("PROCEED");
		verticalPanel_2.add(btnProceed);
		
		CaptionPanel cptnpnlAnalysisPeriod = new CaptionPanel("Analysis Period");
		cptnpnlAnalysisPeriod.setStyleName("blabla");
		verticalPanel.add(cptnpnlAnalysisPeriod);
		cptnpnlAnalysisPeriod.setWidth("500px");
		
		VerticalPanel verticalPanel_3 = new VerticalPanel();
		cptnpnlAnalysisPeriod.setContentWidget(verticalPanel_3);
		verticalPanel_3.setSpacing(5);
		verticalPanel_3.setWidth("500px");
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(5);
		verticalPanel_3.add(horizontalPanel_1);
		
		Label lblFrom = new Label("From : ");
		horizontalPanel_1.add(lblFrom);
		horizontalPanel_1.setCellVerticalAlignment(lblFrom, HasVerticalAlignment.ALIGN_MIDDLE);
		
		DateBox dateBox = new DateBox();
		dateBox.setFormat(new DefaultFormat(DateTimeFormat.getShortDateFormat()));
		horizontalPanel_1.add(dateBox);
		
		Label label = new Label("----");
		horizontalPanel_1.add(label);
		horizontalPanel_1.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label lblTo = new Label("To : ");
		horizontalPanel_1.add(lblTo);
		horizontalPanel_1.setCellVerticalAlignment(lblTo, HasVerticalAlignment.ALIGN_MIDDLE);
		
		DateBox dateBox_1 = new DateBox();
		dateBox_1.setFormat(new DefaultFormat(DateTimeFormat.getShortDateFormat()));
		horizontalPanel_1.add(dateBox_1);
		
		Button button = new Button("PROCEED");
		verticalPanel_3.add(button);
		
		CaptionPanel cptnpnlEncode = new CaptionPanel("Encode");
		cptnpnlEncode.setStyleName("blabla");
		verticalPanel.add(cptnpnlEncode);
		cptnpnlEncode.setWidth("500px");
		
		VerticalPanel verticalPanel_4 = new VerticalPanel();
		verticalPanel_4.setSpacing(5);
		cptnpnlEncode.setContentWidget(verticalPanel_4);
		verticalPanel_4.setWidth("500px");
		
		Button btnEncode = new Button("Encode");
		verticalPanel_4.add(btnEncode);
		
		Label lblNewLabel = new Label("Your file has been processed by the system for analysis. Click \"Analyse\" to begin the analysis or \"Home\" to go to homepage.");
		verticalPanel_4.add(lblNewLabel);
		lblNewLabel.setWidth("500px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setSpacing(5);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("500px");
		verticalPanel.setCellVerticalAlignment(horizontalPanel, HasVerticalAlignment.ALIGN_MIDDLE);
		
		Button btnAnalyse = new Button("Analyse");
		horizontalPanel.add(btnAnalyse);
		
		Button btnHome = new Button("Home");
		horizontalPanel.add(btnHome);
		setWidth("600px");
		
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert(event.getResults());
				
				// RemoteService Call to pass read uploaded file and initialize RDFEncoder.initFileProcess(String filepath).
				RDFEncoderServiceUtil.getInstance().readUploadedFile(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
										
					}

					@Override
					public void onSuccess(String result) {
						Window.alert(result);
						
						// RemoteService Call to get mapping in form of List<FoundMatch>, FoundMatch encapsulates every mapping with (String query, List<String> matches).
						RDFEncoderServiceUtil.getInstance().getMatchOrSuggestMap(new AsyncCallback<List<FoundMatch>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(List<FoundMatch> result) {
								//debug
								System.out.println("E.addSubmitCompleteHandler.readUploadedFile.mosMap.onSuccess");
								
								for(FoundMatch fm : result) {
									//debug
									System.out.println(fm.getQuery() + " ---> " + fm.getMatches().toString());
								
									addMatchOrSuggestionToFlexRow(fm.getQuery(), fm.getMatches());
									
								}
								
								//debug
								System.out.println(headToClassNameMap.toString());
							}
						});
					}
				});
			}
		});

		
		btnEncode.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				
				
				
				// TODO change later
//				for(ListBox lb : mapListMap.values()) {
//					System.out.println(lb.getItemText(lb.getSelectedIndex()));
//				}

			}
		});
		
	}
	
	void addMatchOrSuggestionToFlexRow(String key, List<String> values) {
		int row = flexTable.getRowCount();
		
		Label lbl = new Label(key);
		lbl.setVisible(false);
		lbl.setWidth("180px");
				
		// TODO change later
//		flexTable.setWidget(row, 0, lbl);
//		flexTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		// TODO change later
//		ListBox listBox = new ListBox();
//		listBox.setVisible(false);
//		listBox.setWidth("180px");

		Label lblValue = new Label();
		lblValue.setVisible(false);
		lblValue.setWidth("180px");
		
		if(values.isEmpty()) {
			// TODO Change later
//			lblValue.setText("MATCH NOT FOUND");
//			
//			flexTable.setWidget(row, 2, lblValue);
//			flexTable.getCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_CENTER);
			
			//remove
//			listBox.addItem("MATCH NOT FOUND");
//			listBox.isItemSelected(0);
		} else if(values.size() == 1) {
			lblValue.setText(values.get(0));
			
			headToClassNameMap.put(key, values.get(0));
			
			flexTable.setWidget(row, 0, lbl);
			flexTable.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			
			flexTable.setWidget(row, 2, lblValue);
			flexTable.getCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_CENTER);
			
			//remove
//			listBox.addItem(values.get(0));
//			listBox.isItemSelected(0);
		} else {
			// TODO change later
//			for(String value : values)
//				listBox.addItem(value);
//			
//			flexTable.setWidget(row, 2, listBox);
//			flexTable.getCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_CENTER);
		}

		lbl.setVisible(true);
		lblValue.setVisible(true);
		
		// TODO change later
//		listBox.setVisible(true);
		

		// TODO change later
//		mapListMap.put(key, listBox);
		
	}
	
}
