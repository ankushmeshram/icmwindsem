package com.icmwind.gui.web.client.components;

import java.util.Date;

import com.google.code.p.gwtchismes.client.GWTCIntervalSelector;
import com.google.gwt.i18n.client.DateTimeFormat;

public class IntervalSelector extends GWTCIntervalSelector {

	private Date maxDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").parse("26.10.2008 15:54:00");
	
	public IntervalSelector() {
		
	}
	
	public IntervalSelector(int layout) {
		init(layout);
//		super.checkinLabel.setText("From");
	}
	
	public void init(int layout) {
		super.initialize(layout);
		super.checkinLabel.setText("From");
		super.checkoutLabel.setText("To");
		super.nightsLabel.setText("Days");
		super.intervalLabel.setText("Duration");
		super.checkoutCalendar.setMaximalDate(maxDate);
	}
	
	public void setMaxAnalysisPeriod(int maxDaysOfRecords) {
		super.setMaxdays(maxDaysOfRecords);
	}
	
	public void setBeginAnalysisDate(Date beginDate) {
		super.setMinimalDate(beginDate);
	}
	
	public void setEndAnalysisDate(Date endDate) {
		maxDate = endDate;
		super.setMaximalDate(endDate);
	}
	
	public void setStartDate(Date startDate) {
		super.setInitDate(startDate);
	}
	
	public void setDateFormat(String dateFormat) {
		super.setDateFormat(dateFormat);
	}
	
}
