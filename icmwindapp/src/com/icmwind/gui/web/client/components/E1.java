package com.icmwind.gui.web.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class E1 extends Composite {
	public E1() {
		
		final FormPanel formPanel = new FormPanel();
		formPanel.setAction("/e1serv");
		formPanel.setMethod(FormPanel.METHOD_POST);
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		initWidget(formPanel);
		formPanel.setWidth("500px");
		
		VerticalPanel verPanel = new VerticalPanel();
		formPanel.setWidget(verPanel);
		verPanel.setSize("500px", "");
		
		VerticalPanel verPanDatasheet = new VerticalPanel();
		verPanel.add(verPanDatasheet);
		verPanDatasheet.setSpacing(5);
		
		final FileUpload fileUpload = new FileUpload();
		fileUpload.setName("fileUpload");
		verPanDatasheet.add(fileUpload);
		
		Button btnLoad = new Button("Load");
		verPanDatasheet.add(btnLoad);
		
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
		
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				Window.alert(event.getResults());
			}
		});
		
	}

}
