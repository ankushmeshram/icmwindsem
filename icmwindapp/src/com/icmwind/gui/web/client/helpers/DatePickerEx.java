package com.icmwind.gui.web.client.helpers;
import java.util.Date;

import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.CalendarView;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultMonthSelector;
import com.google.gwt.user.datepicker.client.MonthSelector;


public class DatePickerEx extends DatePicker {

	public DatePickerEx(Date minimum, Date maximum) {
		super(new DefaultMonthSelector(),
				new com.google.gwt.user.datepicker.client.DefaultCalendarView(minimum, maximum), new CalendarModel());
	}

	public DatePickerEx(MonthSelector monthSelector, CalendarView view,
			CalendarModel model) {
		super(monthSelector, view, model);
	}
}