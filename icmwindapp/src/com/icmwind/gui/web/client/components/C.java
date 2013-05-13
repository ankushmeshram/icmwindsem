package com.icmwind.gui.web.client.components;


import java.util.Date;



import com.google.code.p.gwtchismes.client.GWTCIntervalSelector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icmwind.gui.web.client.helpers.TestIntSelector;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.Button;

public class C extends Composite {
	public C() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		
		final Label startLable = new Label("New label");
		final Label endLabel = new Label("New label");
		
		initWidget(verticalPanel);
		
		final Date minDate = DateTimeFormat.getFormat("dd.MM.yyyy hh:mm:ss").parse("04.09.2008 13:10:21");
//				getFormat("EEE MMM dd hh:mm:ss vvv yyyy").parse("Thu Sep 04 13:10:21 CEST 2008");
		
//		Date beginDate = DateTimeFormat.getFormat("dd.MM.yyyy").parse("04.09.2008");
//		Date endDate =  DateTimeFormat.getFormat("dd.MM.yyyy").parse("04.11.2009");
//		
//		final IntervalSelector is = new IntervalSelector(2);
//		is.setDateFormat("dd.MM.yyyy");
//		
//		is.setBeginAnalysisDate(beginDate);
//		is.setEndAnalysisDate(endDate);
//		
//		is.setStartDate(beginDate);
//		is.setMaxAnalysisPeriod(68);
//		
//		verticalPanel.add(is);
		
//		TestIntSelector t = new TestIntSelector(2);
		
				
//		final GWTCIntervalSelector gis = new GWTCIntervalSelector(2);
//		gis.setDatePickerPosition(2);
//		gis.setDateFormat("dd.MM.yyyy");
//		
//		gis.setMinimalDate(beginDate);
//		gis.setMaximalDate(endDate);
//		
//		gis.setInitDate(beginDate);
//		gis.setMaxdays(730);
//		
//		verticalPanel.add(gis);
//		
		Button btnNewButton = new Button("New button");
		btnNewButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				startLable.setText(gis.getInitDate().toString());
//				endLabel.setText(gis.getEndDate().toString());
				startLable.setText(minDate.toString());
			}
		});
		
		verticalPanel.add(btnNewButton);
		
		
		verticalPanel.add(startLable);
		

		verticalPanel.add(endLabel);
		
		
		
		
		
	}

}
