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
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.icmwind.gui.web.client.helpers.DataToUpload;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class UploadPlain extends Composite {

	public UploadPlain() {
//		RootPanel.get("titleContainer").clear();
//		RootPanel.get("titleContainer").add(new HTML("Upload Data"));
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		panel.setSize("900px", "");
		
		InlineHTML nlnhtmlNewInlinehtml = new InlineHTML("Upload sensor data collected from Wind Turbine system.");
		panel.add(nlnhtmlNewInlinehtml);
		panel.setCellHeight(nlnhtmlNewInlinehtml, "30px");
				
		String pathText = "<b>Datasheet Path</b><br/>" +
						  "Currently supported format is Comma Separated Verbose (*.csv).";
		
		VerticalPanel pathVPanel = new VerticalPanel();
		pathVPanel.setSpacing(5);
		panel.add(pathVPanel);
		pathVPanel.setSize("800px", "70px");
		HTML pathHtml = new HTML("<b>Datasheet Path</b><br/><i>Currently supported format is Comma Separated Verbose (*.csv).</i>", true);
		pathVPanel.add(pathHtml);
		pathVPanel.setCellHeight(pathHtml, "40px");
		panel.setCellHeight(pathHtml, "40px");
		
		HorizontalPanel fileHPanel = new HorizontalPanel();
		fileHPanel.setSpacing(5);
		pathVPanel.add(fileHPanel);
		fileHPanel.setWidth("800px");
		
		HTML fileHtml = new HTML("<b>Enter the path of datasheet</b>", true);
		fileHPanel.add(fileHtml);
		fileHPanel.setCellVerticalAlignment(fileHtml, HasVerticalAlignment.ALIGN_MIDDLE);
		fileHPanel.setCellHorizontalAlignment(fileHtml, HasHorizontalAlignment.ALIGN_CENTER);
		fileHPanel.setCellWidth(fileHtml, "200px");
		fileHtml.setWidth("200px");
		fileHtml.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		final TextBox fileTBox = new TextBox();
		fileHPanel.add(fileTBox);
		fileHPanel.setCellWidth(fileTBox, "350px");
		fileTBox.setWidth("350px");
		
		Button btnSubmit_1 = new Button("Submit");
		fileHPanel.add(btnSubmit_1);
		fileHPanel.setCellVerticalAlignment(btnSubmit_1, HasVerticalAlignment.ALIGN_MIDDLE);
		
		String additionalInfoText = "<b>Additional Data Information</b><br/>" +
									"Fill in additional information about data based on the content in the file." +
									"If ID can not be found please check the System Settings";
		
		VerticalPanel infoVPanel = new VerticalPanel();
		infoVPanel.setSpacing(5);
		panel.add(infoVPanel);
		infoVPanel.setSize("800px", "");
		HTML infoHtml = new HTML("<b>Additional Data Information</b><br/><i>Fill in additional information about data based on the content in the file.If ID can not be found please check the System Settings</i>", true);
		infoVPanel.add(infoHtml);
		infoVPanel.setCellHeight(infoHtml, "40px");
		panel.setCellHeight(infoHtml, "55px");
		
		HorizontalPanel infoHPanel_1 = new HorizontalPanel();
		infoVPanel.add(infoHPanel_1);
		infoHPanel_1.setWidth("800px");
		
		Label infoItemLabel_1 = new Label("Wind Turbine ID :");
		infoHPanel_1.add(infoItemLabel_1);
		infoHPanel_1.setCellVerticalAlignment(infoItemLabel_1, HasVerticalAlignment.ALIGN_MIDDLE);
		infoHPanel_1.setCellHorizontalAlignment(infoItemLabel_1, HasHorizontalAlignment.ALIGN_RIGHT);
		infoHPanel_1.setCellWidth(infoItemLabel_1, "300px");
		
		ListBox infoListBox_1 = new ListBox();
		infoListBox_1.addItem("");
		infoListBox_1.addItem("MarpingenI");
		infoListBox_1.addItem("MarpingenII");
		infoHPanel_1.add(infoListBox_1);
		infoListBox_1.setWidth("150px");
		infoListBox_1.setVisibleItemCount(1);
		
		HorizontalPanel infoHPanel_2 = new HorizontalPanel();
		infoVPanel.add(infoHPanel_2);
		infoHPanel_2.setWidth("800px");
		
		Label lblGearBoxId = new Label("Gear Box ID :");
		infoHPanel_2.add(lblGearBoxId);
		infoHPanel_2.setCellHorizontalAlignment(lblGearBoxId, HasHorizontalAlignment.ALIGN_RIGHT);
		infoHPanel_2.setCellWidth(lblGearBoxId, "300px");
		lblGearBoxId.setWidth("300px");
		
		ListBox listBox = new ListBox();
		listBox.addItem("");
		listBox.addItem("GB_MarpI");
		listBox.addItem("GB_MarpII");
		listBox.setVisibleItemCount(1);
		infoHPanel_2.add(listBox);
		listBox.setWidth("150px");
		
		String analysisPeriodText = "<b>Analysis Period</b><br/>" +
									"Choose the time period for analysis.";
		
		VerticalPanel periodVPanel = new VerticalPanel();
		periodVPanel.setSpacing(5);
		panel.add(periodVPanel);
		periodVPanel.setSize("800px", "100px");
		HTML periodHtml = new HTML("<b>Analysis Period</b><br/><i>Choose the time period for analysis.</i>", true);
		periodVPanel.add(periodHtml);
		panel.setCellHeight(periodHtml, "40px");
		
		HorizontalPanel dateHPanel = new HorizontalPanel();
		periodVPanel.add(dateHPanel);
		panel.setCellHeight(dateHPanel, "20px");
		dateHPanel.setSize("600px", "");
		
		HTML startHtml = new HTML("<b>Starting Date :</b>", true);
		dateHPanel.add(startHtml);
		dateHPanel.setCellHorizontalAlignment(startHtml, HasHorizontalAlignment.ALIGN_RIGHT);
		dateHPanel.setCellWidth(startHtml, "350px");
		
		final ListBox dateListBox = new ListBox();
		dateListBox.addItem("","");
		dateListBox.addItem("25.02.2012","25.02.2012");
		dateListBox.addItem("30.02.2012","30.02.2012");
		dateListBox.addItem("05.03.2012","05.03.2012");
		dateHPanel.add(dateListBox);
		dateListBox.setWidth("120px");
		dateListBox.setVisibleItemCount(1);
		
		HorizontalPanel daysHPanel = new HorizontalPanel();
		periodVPanel.add(daysHPanel);
		panel.setCellHeight(daysHPanel, "20px");
		daysHPanel.setSize("600px", "");
		
		HTML htmlNumberOfDays = new HTML("<b>Number of days of records to analyse.(Max 25) :</b>", true);
		daysHPanel.add(htmlNumberOfDays);
		daysHPanel.setCellHorizontalAlignment(htmlNumberOfDays, HasHorizontalAlignment.ALIGN_RIGHT);
		daysHPanel.setCellWidth(htmlNumberOfDays, "350px");
		
		final ListBox dayListBox = new ListBox();
		dayListBox.addItem("","");
		dayListBox.addItem("5","5");
		dayListBox.addItem("10","10");
		daysHPanel.add(dayListBox);
		dayListBox.setWidth("120px");
		dayListBox.setVisibleItemCount(1);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.setBorderWidth(0);
		panel.add(horizontalPanel);
		horizontalPanel.setSize("180px", "");
		
		Button btnSubmit = new Button("Upload");
		btnSubmit.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
			}
		});
		
			
		horizontalPanel.add(btnSubmit);
		horizontalPanel.setCellWidth(btnSubmit, "100px");
		btnSubmit.setWidth("80px");
		
		Button btnReset = new Button("Reset");
		horizontalPanel.add(btnReset);
		horizontalPanel.setCellHorizontalAlignment(btnReset, HasHorizontalAlignment.ALIGN_RIGHT);
		btnReset.setWidth("80px");
		
		initWidget(panel);
	}

}
