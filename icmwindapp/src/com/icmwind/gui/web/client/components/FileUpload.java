package com.icmwind.gui.web.client.components;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.icmwind.gui.web.client.helpers.FoundMatch;
import com.icmwind.gui.web.client.services.RDFEncoderService.RDFEncoderServiceUtil;
import com.google.gwt.user.client.ui.FlexTable;


/**
 * @author anme05
 *
 * Client-Side component providing functionality for upload the data file and RDFying it.
 */
public class FileUpload extends Composite {

	public FileUpload() {
		
		final FormPanel form = new FormPanel();
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction("/uploadme");
		initWidget(form);
		form.setWidth("800px");
		
		System.out.println(GWT.getModuleBaseURL());

		VerticalPanel panel = new VerticalPanel();
		VerticalPanel uploadpanel = new VerticalPanel();
		final VerticalPanel mappingpanel = new VerticalPanel(); mappingpanel.setVisible(false);
		final VerticalPanel listpanel = new VerticalPanel();
		VerticalPanel analysispanel = new VerticalPanel(); analysispanel.setVisible(false);
		VerticalPanel encodepanel = new VerticalPanel(); encodepanel.setVisible(false);
		
		final FlexTable flexTable = new FlexTable();
		
		panel.setSpacing(10);
		form.setWidget(panel);
		panel.setSize("780px", "");
		
		
		uploadpanel.setSpacing(5);
		panel.add(uploadpanel);
		uploadpanel.setWidth("780px");
		
		HTML uploadHeaderText = new HTML("<b>Upload File</b>", true);
		uploadpanel.add(uploadHeaderText);
		
		HTML uploadText = new HTML("Please upload the data file", true);
		uploadpanel.add(uploadText);
		
		final com.google.gwt.user.client.ui.FileUpload fileUpload = new com.google.gwt.user.client.ui.FileUpload();
		fileUpload.setName("fileUpload");
		uploadpanel.add(fileUpload);
		
		Button btnupload = new Button("Upload File");
		uploadpanel.add(btnupload);
		
		btnupload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				form.submit();
			}
		});
		
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			@Override
			public void onSubmit(SubmitEvent event) {
				if(!fileUpload.getFilename().endsWith(".csv")) {
					Window.alert("Choose a *.csv file.");
					event.cancel();
				}
			}
		});
		
		// after passed file has been successfully copied to System known directory
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				// Currently pass this path to find the data file. TODO: Change it ASAP. 
				String fileName = "C:\\ICMWIND_DATA\\" + fileUpload.getFilename(); 
				System.out.println("FU:" + fileName);
				
				// RemoteService Call to pass the file path and initialize RDFEncoder.initFileProcessing().
				RDFEncoderServiceUtil.getInstance().fileToEncode(fileName, new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						
						// RemoteService Call to get mapping in form of List<FoundMatch>, FoundMatch encapsulates every mapping with (String query, List<String> matches).
						RDFEncoderServiceUtil.getInstance().mosMap(new AsyncCallback<List<FoundMatch>>() {

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Failure mosMap.");								
							}

							public void onSuccess(List<FoundMatch> result) {
								for(FoundMatch fm : result) {
									// add query to Text and matches to ListBox
									int row = flexTable.getRowCount();
									flexTable.setText(row, 0, fm.getQuery());
									
									//TODO: Add ChangeHandler to every ListBox
									ListBox lb = new ListBox();
										 
									for(String match : fm.getMatches())
										lb.addItem(match);
									
									// for Selection from Ontology Map
									lb.addItem("Other..");
									flexTable.setWidget(row, 2, lb);
								}
							}
						});
						
						mappingpanel.setVisible(true);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failure for fileToEncode");
					}
				});
				Window.alert(event.getResults());
			}
		});
		
		
		mappingpanel.setSpacing(5);
		panel.add(mappingpanel);
		mappingpanel.setWidth("780px");
		
		HTML mapHeaderText = new HTML("<b>Map Column Names of Data File</b>", true);
		mappingpanel.add(mapHeaderText);
		mapHeaderText.setWidth("770");
		
		HTML mapText = new HTML("Column names of data file are mapped to ICMWind system's available data measurements name.<br>Select the appropriate mapped measurement data name.", true);
		mappingpanel.add(mapText);
		mapText.setWidth("770");
		
		mappingpanel.add(listpanel);
		listpanel.setWidth("770px");
		

		flexTable.setCellPadding(3);
		mappingpanel.add(flexTable);
		flexTable.setWidth("770px");
		
		HTML dataColumnHeaderText = new HTML("<b>Data file Coumn Name</b>", true);
		flexTable.setWidget(0, 0, dataColumnHeaderText);
		dataColumnHeaderText.setWidth("200px");
		
		HTML blankText = new HTML("BLANK", true);
		flexTable.setWidget(0, 1, blankText);
		blankText.setWidth("100px");
		
		HTML mappedDataHeaderText = new HTML("<b>Mapped ICMWind's Measurement Data Name</b>", true);
		flexTable.setWidget(0, 2, mappedDataHeaderText);
		mappedDataHeaderText.setWidth("300px");
		
		Button btnSaveSelection = new Button("Save Selection");
		mappingpanel.add(btnSaveSelection);
		
		
		analysispanel.setSpacing(5);
		panel.add(analysispanel);
		analysispanel.setWidth("780px");
		
		HTML analysisHeaderText = new HTML("<b>Analysis Period</b>", true);
		analysispanel.add(analysisHeaderText);
		
		HTML analysisText = new HTML("Select the period to analyse the data for faults. Current max analysis period is one month.", true);
		analysispanel.add(analysisText);
		
		Grid periodgrid = new Grid(1, 4);
		periodgrid.setCellSpacing(3);
		analysispanel.add(periodgrid);
		
		HTML fromText = new HTML("<b>From</b>", true);
		periodgrid.setWidget(0, 0, fromText);
		fromText.setWidth("100px");
		
		ListBox fromListBox = new ListBox();
		periodgrid.setWidget(0, 1, fromListBox);
		fromListBox.setWidth("150px");
		fromListBox.setVisibleItemCount(1);
		
		HTML toText = new HTML("<b>To</b>", true);
		periodgrid.setWidget(0, 2, toText);
		toText.setWidth("100px");
		
		ListBox toListBox = new ListBox();
		periodgrid.setWidget(0, 3, toListBox);
		toListBox.setWidth("150px");
		toListBox.setVisibleItemCount(1);
		periodgrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		periodgrid.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		encodepanel.setSpacing(5);
		panel.add(encodepanel);
		encodepanel.setWidth("780px");
		
		HTML encodeHeaderText = new HTML("<b>Encode To ICMWind's Internal Format</b>", true);
		encodepanel.add(encodeHeaderText);
		
		HTML encodeText = new HTML("Based on the mapping done the data file will be converted to ICMWind System's internal format.<br>The records for the selected period will be available for the analysis.", true);
		encodepanel.add(encodeText);
		
		Button btnEncode = new Button("Encode");
		encodepanel.add(btnEncode);
		btnEncode.setWidth("100px");
		encodepanel.setCellHorizontalAlignment(btnEncode, HasHorizontalAlignment.ALIGN_CENTER);
		
	}

}
