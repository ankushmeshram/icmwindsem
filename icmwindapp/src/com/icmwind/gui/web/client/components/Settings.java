package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Button;

public class Settings extends Composite {

	public Settings() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.setBorderWidth(1);
		panel.setSpacing(10);
		initWidget(panel);
		panel.setSize("900px", "");
		
		String settingsText = "Information about wind turbines and their parts, whose Sensor data are analyzed, are presented here. " +
							  "You can modify or add new information.";
		HTML settingsHtml = new HTML(settingsText, true);
		panel.add(settingsHtml);
		settingsHtml.setWidth("900px");
		
		VerticalPanel wtVPanel = new VerticalPanel();
		wtVPanel.setSpacing(5);
		panel.add(wtVPanel);
		wtVPanel.setWidth("900px");
		
		FlexTable flexTable_1 = new FlexTable();
		wtVPanel.add(flexTable_1);
		flexTable_1.setWidth("900px");
		
		HTML wtHtml = new HTML("<b>Wind Turbine(s)</b>", true);
		flexTable_1.setWidget(0, 0, wtHtml);
		flexTable_1.getCellFormatter().setWidth(0, 0, "100px");
		wtHtml.setWidth("100px");
		
		Tree wtTree = new Tree();
		flexTable_1.setWidget(1, 0, wtTree);
		wtTree.setAnimationEnabled(true);
		wtTree.setWidth("300px");
		
		TreeItem marpISubItem = new TreeItem("Marpingen I");
		wtTree.addItem(marpISubItem);
		marpISubItem.setWidth("400px");
		
		TreeItem modifiedItem = new TreeItem("<b>Modified On 27.11.2011<b>");
		marpISubItem.addItem(modifiedItem);
		
		TreeItem installedItem = new TreeItem("<b>Installed On 20.11.2011</b>");
		marpISubItem.addItem(installedItem);
		
		TreeItem locItem = new TreeItem("<b>Located At Marpingen</b>");
		marpISubItem.addItem(locItem);
		
		TreeItem partsItems = new TreeItem("<b>Parts</b>");
		marpISubItem.addItem(partsItems);
		
		TreeItem item1 = partsItems.addItem(new Hyperlink("HLB_567", false, "newHistoryToken"));
		
		TreeItem item2 = partsItems.addItem(new Hyperlink("CS100_567", false, "newHistoryToken"));
		flexTable_1.getFlexCellFormatter().setColSpan(1, 0, 1);
		
		Hyperlink rhHLink = new Hyperlink("Rotor Hub", false, "newHistoryToken");
		
		Hyperlink gbHLink = new Hyperlink("GearBox", false, "newHistoryToken");
		
		String genDescText = "<b>Edited on</b> 10.10.2012 <br/>" +
				 "<b>Installed on</b> 20.09.2009 <br/>" +
				 "<b>Location</b> Marpingen";
		
		
		VerticalPanel senVPanel = new VerticalPanel();
		senVPanel.setSpacing(5);
		panel.add(senVPanel);
		senVPanel.setWidth("900px");
		
		HTML senHtml = new HTML("<b>Sensors</b>", true);
		senVPanel.add(senHtml);
		
		Hyperlink cs157HLink = new Hyperlink("CS100_567", false, "newHistoryToken");
//		senVPanel.add(cs157HLink);
		
		Hyperlink hlb567HLink = new Hyperlink("HLB_567", false, "newHistoryToken");
//		senVPanel.add(hlb567HLink);
		
		Tree senTree = new Tree();
		senTree.setAnimationEnabled(true);
		
		TreeItem treeItem = new TreeItem(hlb567HLink);
		senTree.addItem(treeItem);
		treeItem.setState(true);
		TreeItem treeItem_1 = new TreeItem(cs157HLink);
		senTree.addItem(treeItem_1);
		treeItem_1.setState(true);
		senVPanel.add(senTree);
		senTree.setWidth("300px");
		
		TreeItem newItem = new TreeItem("New item");
		newItem.setText("Haramkhor");
		senTree.addItem(newItem);
		newItem.setState(true);
		
		VerticalPanel gbVPanel = new VerticalPanel();
		gbVPanel.setSpacing(5);
		panel.add(gbVPanel);
		gbVPanel.setWidth("900px");
		
		HTML gbHtml = new HTML("<b>Gear Box(es)</b>", true);
		gbVPanel.add(gbHtml);
		
		Tree gbTree = new Tree();
		gbTree.setAnimationEnabled(true);
		gbVPanel.add(gbTree);
		gbTree.setWidth("300px");
		
		TreeItem gbItem1 = gbTree.addItem(new Hyperlink("GB 2345", true, ""));
		
		TreeItem gbItem2 = gbTree.addItem(new Hyperlink("GB 7383", true, ""));
		
		
					
		
		
//		RootPanel.get("titleContainer").clear();
//		RootPanel.get("titleContainer").add(new HTML("Settings"));
	}

}
