package com.icmwind.gui.web.client.helpers;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Utils {
	static RootPanel titleHolder = RootPanel.get("titleContainer");
	
	public static void setTitle(String title) {
		titleHolder.clear();
		titleHolder.add(new HTML(title));
	}
	
}
