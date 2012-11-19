package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.icmwind.gui.web.client.helpers.Utils;

public class Analyse extends Composite {

	public Analyse() {
		
		Utils.setTitle("Analyse Sensor Data");
		
		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		initWidget(panel);
		panel.setSize("900px", "");
		
		String analyseText = "The uploaded data is analyzed for fault detection and diagnosis. " +
							 "The severity of system component’s condition is represented by Red, Orange, Green colors denoting condition from worst to okay respectively. " +
							 "Conditions are checked based on the rules defined in the system settings.";
		HTML analyseHtml = new HTML(analyseText, true);
		panel.add(analyseHtml);
		analyseHtml.setWidth("900px");
		panel.setCellHeight(analyseHtml, "40px");
		
		HorizontalPanel statusHPanel = new HorizontalPanel();
		statusHPanel.setSpacing(5);
		panel.add(statusHPanel);
		statusHPanel.setSize("900px", "");
		
		Grid grid = new Grid(2, 1);
		grid.setBorderWidth(0);
		grid.setCellSpacing(1);
		statusHPanel.add(grid);
		statusHPanel.setCellWidth(grid, "200px");
		grid.setSize("200px", "");
		
		HorizontalPanel thermoHPanel = new HorizontalPanel();
		thermoHPanel.setSpacing(5);
		grid.setWidget(0, 0, thermoHPanel);
		grid.getCellFormatter().setHeight(0, 0, "25px");
		grid.getCellFormatter().setWidth(0, 0, "200px");
		thermoHPanel.setSize("200px", "25px");
		
		Image thermoImg = new Image("images/ok_24.png");
		thermoHPanel.add(thermoImg);
		thermoHPanel.setCellWidth(thermoImg, "30px");
		
		Label lblThermobypassValve = new Label("Thermo-bypass Valve");
		thermoHPanel.add(lblThermobypassValve);
		thermoHPanel.setCellVerticalAlignment(lblThermobypassValve, HasVerticalAlignment.ALIGN_MIDDLE);
		thermoHPanel.setCellWidth(lblThermobypassValve, "170px");
		lblThermobypassValve.setHeight("24px");
		grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		
		HorizontalPanel filterHPanel = new HorizontalPanel();
		filterHPanel.setSpacing(5);
		grid.setWidget(1, 0, filterHPanel);
		grid.getCellFormatter().setWidth(1, 0, "200px");
		grid.getCellFormatter().setHeight(1, 0, "25px");
		filterHPanel.setSize("200px", "25px");
		
		Image filterImg = new Image("images/fault_24.png");
		filterHPanel.add(filterImg);
		filterImg.setSize("24px", "24px");
		filterHPanel.setCellWidth(filterImg, "30px");
		
		Label lblFilter = new Label("Filter");
		filterHPanel.add(lblFilter);
		filterHPanel.setCellVerticalAlignment(lblFilter, HasVerticalAlignment.ALIGN_MIDDLE);
		filterHPanel.setCellWidth(lblFilter, "170px");
		lblFilter.setHeight("24px");
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("timepass-analysis");
		statusHPanel.add(scrollPanel);
		scrollPanel.setSize("700px", "400px");
		
		final Label lblExplanationOfThe = new Label("Explanation of the status of current system component selected.");
		lblExplanationOfThe.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		lblExplanationOfThe.setStyleName("query-label");
		scrollPanel.setWidget(lblExplanationOfThe);
		lblExplanationOfThe.setSize("100%", "100%");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.setBorderWidth(0);
		panel.add(horizontalPanel);
		horizontalPanel.setSize("480px", "");
		
		Button btnAnalyseNewData = new Button("Upload New Data");
		btnAnalyseNewData.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				final DialogBox db = new DialogBox();
				db.setAnimationEnabled(true);
				db.setText("This action will clear the current data. Do you want to continue?");
				Button yesBtn = new Button("Yes", new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						db.hide();
						History.newItem("upload");
					}
				});
				Button noBtn = new Button("No", new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						db.hide();						
					}
				});
				HorizontalPanel btnHolder = new HorizontalPanel();
				btnHolder.add(yesBtn);
				btnHolder.add(noBtn);
				db.setWidget(btnHolder);
				int left = Window.getClientWidth()/ 2;
	            int top = Window.getClientHeight()/ 2;
				db.setPopupPosition(left, top);
				db.show();
				
			}
		});
		horizontalPanel.add(btnAnalyseNewData);
		btnAnalyseNewData.setSize("130px", "");
		
		Button btnNewButton = new Button("Session Report");
		btnNewButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				History.newItem("report");
			}
		});
		horizontalPanel.add(btnNewButton);
		btnNewButton.setSize("120px", "");
		
		Button btnAdavncedSemanticAnalysis = new Button("Adavnced Semantic Analysis");
		btnAdavncedSemanticAnalysis.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				History.newItem("advanced");
			}
		});
		horizontalPanel.add(btnAdavncedSemanticAnalysis);
		btnAdavncedSemanticAnalysis.setSize("200px", "");
		
		thermoImg.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lblExplanationOfThe.setText("This is thermo image.");
			}
		});
		
		filterImg.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lblExplanationOfThe.setText("This is filter image.");
			}
		});
//		RootPanel.get("titleContainer").clear();
//		RootPanel.get("titleContainer").add(new HTML("Analyse Sensor Data"));
	}

}
