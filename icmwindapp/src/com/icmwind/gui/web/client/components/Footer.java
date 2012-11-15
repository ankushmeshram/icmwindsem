package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class Footer extends Composite {

	public Footer() {
		
		HorizontalPanel footerPanel = new HorizontalPanel();
		footerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(footerPanel);
		footerPanel.setSize("250px", "50px");
		
		Image dfkiImage = new Image("dfki_logo.png");
		footerPanel.add(dfkiImage);
		footerPanel.setCellVerticalAlignment(dfkiImage, HasVerticalAlignment.ALIGN_BOTTOM);
		dfkiImage.setHeight("25px");
		
		Image zemaImage = new Image("zema_logo.png");
		footerPanel.add(zemaImage);
		footerPanel.setCellVerticalAlignment(zemaImage, HasVerticalAlignment.ALIGN_BOTTOM);
		zemaImage.setHeight("30px");
	}

}
