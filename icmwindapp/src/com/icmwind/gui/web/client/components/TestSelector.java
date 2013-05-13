package com.icmwind.gui.web.client.components;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TestSelector extends Composite {
	public TestSelector() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		IntervalSelector is = new IntervalSelector(2);
		
		Date minDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").parse("04.09.2008 11:54:00");
		Date maxDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").parse("26.10.2008 15:54:00");
		
		is.setDateFormat("dd.MM.yyyy HH:mm:ss");
		is.setStartDate(minDate);
		is.setBeginAnalysisDate(minDate);
		is.setEndAnalysisDate(maxDate);
		
		verticalPanel.add(is);
		
	}

}
