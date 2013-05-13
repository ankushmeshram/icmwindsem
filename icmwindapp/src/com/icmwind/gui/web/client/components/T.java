package com.icmwind.gui.web.client.components;

import java.util.Date;

import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

public class T extends Composite {
	
	Date minDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").parse("04.09.2008 11:54:00");
	Date maxDate = DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").parse("26.10.2008 15:54:00");
	
	public T() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		
		final DateBox dateBox = new DateBox();
		
		dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> dateValueChangeEvent) {
				if(dateValueChangeEvent.getValue().before(minDay())) {
					dateBox.setValue(minDay(), false);
				}
			}
		});
		
		dateBox.getDatePicker().addShowRangeHandler(new ShowRangeHandler<Date>() {
			@Override
			public void onShowRange(ShowRangeEvent<Date> dateShowRangeEvent) {
				final Date today = minDay();
				Date start = zeroTime(dateShowRangeEvent.getStart());
				Date end = zeroTime(dateShowRangeEvent.getEnd());
				
				while(start.before(today)) {
					dateBox.getDatePicker().setTransientEnabledOnDates(false, start);
					start = nextDay(start);					
				}
			}
		});
		
		verticalPanel.add(dateBox);
		

	}
	
	private static Date minDay() {
		return zeroTime(minDay());
	}
	
	private static Date today() {
		return zeroTime(new Date()); 
	}
	
	private static Date zeroTime(final Date date) {
		return DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").parse(DateTimeFormat.getFormat("dd.MM.yyyy HH:mm:ss").format(date));
	}
	
	private static Date nextDay(final Date date) {
		CalendarUtil.addDaysToDate(date, 1);
		return date;
	}

}
