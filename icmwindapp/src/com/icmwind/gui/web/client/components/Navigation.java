package com.icmwind.gui.web.client.components;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;


public class Navigation extends Composite {

	public Navigation() {
		MenuBar menuBar = new MenuBar(true);
		menuBar.setStyleName("navigationbar");
		menuBar.setFocusOnHoverEnabled(false);
		menuBar.setSize("120px", "100%");
		
		MenuItem mntmHome = new MenuItem("Home", false, new HomeCommand());
		mntmHome.setStyleName("navigationbar-menuitem");
		menuBar.addItem(mntmHome);
		mntmHome.setWidth("120px");
		
		MenuItem mntmAbout = new MenuItem("About", false, new AboutCommand());
		mntmAbout.setStyleName("navigationbar-menuitem");
		menuBar.addItem(mntmAbout);
		mntmAbout.setWidth("120px");
		
		MenuItem mntmUploadData = new MenuItem("Upload Data", false, new UploadCommand());
		mntmUploadData.setStyleName("navigationbar-menuitem");
		menuBar.addItem(mntmUploadData);
		mntmUploadData.setWidth("120px");
		
		MenuItem mntmNewItem = new MenuItem("Analyse Data", false, new AnalyseCommand());
		mntmNewItem.setStyleName("navigationbar-menuitem");
		menuBar.addItem(mntmNewItem);
		mntmNewItem.setWidth("120px");
		
		MenuItem mntmActivityReport = new MenuItem("Session Report", false, new ReportCommand());
		mntmActivityReport.setStyleName("navigationbar-menuitem");
		menuBar.addItem(mntmActivityReport);
		mntmActivityReport.setWidth("120px");
		
		MenuItem mntmSettings = new MenuItem("Settings", false, new SettingsCommand());
		mntmSettings.setStyleName("navigationbar-menuitem");
		menuBar.addItem(mntmSettings);
		mntmSettings.setWidth("120px");
		
		initWidget(menuBar);
	}
	
	private class HomeCommand implements Command{
		@Override
		public void execute() {
			History.newItem("home");			
		}
	}
	
	private class AboutCommand implements Command{
		@Override
		public void execute() {
			History.newItem("about");
		}
	}
	
	private class UploadCommand implements Command{
		@Override
		public void execute() {
			History.newItem("upload");
		}
	}
	
	private class AnalyseCommand implements Command{
		@Override
		public void execute() {
			History.newItem("analyse");
		}
	}
	
	private class ReportCommand implements Command{
		@Override
		public void execute() {
			History.newItem("report");
		}
	}
	
	private class SettingsCommand implements Command{
		@Override
		public void execute() {
			History.newItem("settings");
		}
	}

}
