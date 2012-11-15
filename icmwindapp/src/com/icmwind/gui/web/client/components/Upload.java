package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Button;

public class Upload extends Composite {

	public Upload() {
		RootPanel.get("titleContainer").clear();
		RootPanel.get("titleContainer").add(new HTML("Upload Data"));
		
		FormPanel form = new FormPanel();
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		form.setWidget(panel);
		panel.setSize("800px", "");
		
		InlineHTML nlnhtmlNewInlinehtml = new InlineHTML("Upload sensor data collected from Wind Turbine system.");
		panel.add(nlnhtmlNewInlinehtml);
		panel.setCellHeight(nlnhtmlNewInlinehtml, "30px");
				
		String pathText = "<b>Datasheet Path</b><br/>" +
						  "Currently supported format is Comma Separated Verbose (*.csv).";
		
		VerticalPanel pathVPanel = new VerticalPanel();
		panel.add(pathVPanel);
		pathVPanel.setSize("800px", "70px");
		HTML pathHtml = new HTML(pathText, true);
		pathVPanel.add(pathHtml);
		pathVPanel.setCellHeight(pathHtml, "40px");
		panel.setCellHeight(pathHtml, "40px");
		
		FileUpload fileUpload = new FileUpload();
		pathVPanel.add(fileUpload);
		fileUpload.setName("upload");
		panel.setCellHeight(fileUpload, "26px");
		
		String additionalInfoText = "<b>Additional Data Information</b><br/>" +
									"Fill in additional information about data based on the content in the file." +
									"If ID can not be found please check the System Settings";
		
		VerticalPanel infoVPanel = new VerticalPanel();
		panel.add(infoVPanel);
		infoVPanel.setSize("800px", "100%");
		HTML infoHtml = new HTML(additionalInfoText, true);
		infoVPanel.add(infoHtml);
		infoVPanel.setCellHeight(infoHtml, "40px");
		panel.setCellHeight(infoHtml, "55px");
		
		String analysisPeriodText = "<b>Analysis Period</b><br/>" +
									"Choose the time period for analysis.";
		
		VerticalPanel periodVPanel = new VerticalPanel();
		panel.add(periodVPanel);
		periodVPanel.setSize("800px", "100px");
		HTML periodHtml = new HTML(analysisPeriodText, true);
		periodVPanel.add(periodHtml);
		panel.setCellHeight(periodHtml, "40px");
		
		HorizontalPanel dateHPanel = new HorizontalPanel();
		periodVPanel.add(dateHPanel);
		panel.setCellHeight(dateHPanel, "20px");
		dateHPanel.setSize("600px", "");
		
		HTML startHtml = new HTML("Starting Date :", true);
		dateHPanel.add(startHtml);
		dateHPanel.setCellHorizontalAlignment(startHtml, HasHorizontalAlignment.ALIGN_RIGHT);
		dateHPanel.setCellWidth(startHtml, "350px");
		
		ListBox dateListBox = new ListBox();
		dateHPanel.add(dateListBox);
		dateListBox.setWidth("120px");
		dateListBox.setVisibleItemCount(1);
		
		HorizontalPanel daysHPanel = new HorizontalPanel();
		periodVPanel.add(daysHPanel);
		panel.setCellHeight(daysHPanel, "20px");
		daysHPanel.setSize("600px", "");
		
		HTML htmlNumberOfDays = new HTML("Number of days of records to analyse.(Max 25) :", true);
		daysHPanel.add(htmlNumberOfDays);
		daysHPanel.setCellHorizontalAlignment(htmlNumberOfDays, HasHorizontalAlignment.ALIGN_RIGHT);
		daysHPanel.setCellWidth(htmlNumberOfDays, "350px");
		
		ListBox dayListBox = new ListBox();
		daysHPanel.add(dayListBox);
		dayListBox.setWidth("120px");
		dayListBox.setVisibleItemCount(1);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		panel.add(horizontalPanel);
		horizontalPanel.setWidth("180px");
		
		Button btnSubmit = new Button("Submit");
		horizontalPanel.add(btnSubmit);
		horizontalPanel.setCellWidth(btnSubmit, "100px");
		btnSubmit.setWidth("80px");
		
		Button btnReset = new Button("Reset");
		horizontalPanel.add(btnReset);
		horizontalPanel.setCellHorizontalAlignment(btnReset, HasHorizontalAlignment.ALIGN_RIGHT);
		btnReset.setWidth("80px");
		
		HTML separatorHtml = new HTML("", true);
		horizontalPanel.add(separatorHtml);
		panel.setCellHeight(separatorHtml, "10px");
		
		HTML separatorHtml2 = new HTML("", true);
		horizontalPanel.add(separatorHtml2);
		separatorHtml2.setHeight("10px");
		
		initWidget(form);
		form.setSize("800px", "439px");
	}

}
