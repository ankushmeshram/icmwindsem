package com.icmwind.gui.web.client.helpers;

import com.google.code.p.gwtchismes.client.GWTCIntervalSelector;

public class TestIntSelector extends GWTCIntervalSelector {

	public TestIntSelector() {
		
	}
	
	public TestIntSelector(int layout) {
		init(layout);
//		super.checkinLabel.setText("From");
	}
	
	public void init(int layout) {
		super.initialize(layout);
		super.checkinLabel.setText("From");
		super.checkoutLabel.setText("To");
		super.nightsLabel.setText("Days");
		super.intervalLabel.setText("Duration");
		
	}
	
	
}
