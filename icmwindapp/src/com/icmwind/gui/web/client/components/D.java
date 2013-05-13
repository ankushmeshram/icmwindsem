package com.icmwind.gui.web.client.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icmwind.gui.web.client.services.GlobalInitializerService;
import com.icmwind.gui.web.client.services.RDFEncoderService.RDFEncoderServiceUtil;

public class D extends Composite {
	
	
	VerticalPanel verPanGeneral = new VerticalPanel();
	VerticalPanel verPanMain = new VerticalPanel();
	CaptionPanel capPanSource = new CaptionPanel("Source");
	VerticalPanel verPanSource = new VerticalPanel();
	Label lblEnterSource = new Label("Enter source...");
	ListBox listBox = new ListBox();
	CaptionPanel capPanelWTInfo = new CaptionPanel("Wind Turbine Information");
	VerticalPanel vePanWTInfo = new VerticalPanel();
	HorizontalPanel horPanName = new HorizontalPanel();
	Label lblName = new Label("Name");Label label_1 = new Label(" : ");Label name = new Label("");
	HorizontalPanel horPanLocation = new HorizontalPanel();
	Label lblLocation = new Label("Location");Label label_2 = new Label(" : ");Label location = new Label("");
	HorizontalPanel horPanDate = new HorizontalPanel();
	Label lblInstallationDate = new Label("Installation Date");Label label_5 = new Label(" : ");Label instdate = new Label("");
	CaptionPanel capPanParts = new CaptionPanel("Parts");
	VerticalPanel verPanParts = new VerticalPanel();
	HorizontalPanel horPanGB = new HorizontalPanel();
	Label lblGearbox = new Label("Gearbox");Label label_7 = new Label(" : ");Label gearbox = new Label("");
	HorizontalPanel horPanRH = new HorizontalPanel();
	Label lblRotorHub = new Label("Rotor Hub");Label label_16 = new Label(" : ");Label rotorhub = new Label("");
	HorizontalPanel horPanOP = new HorizontalPanel();
	Label lblOilPump = new Label("Oil Pump");Label label_18 = new Label(" : ");Label oilpump = new Label("");
	HorizontalPanel horPanTBV = new HorizontalPanel();
	Label lblThermoBypassValve = new Label("Thermo Bypass Valve");Label label_21 = new Label(" : ");Label tbv = new Label("");
	HorizontalPanel horPanCooler = new HorizontalPanel();
	Label lblCooler = new Label("Cooler");Label label_24 = new Label(" : ");Label cooler = new Label("");
	CaptionPanel capPanSensors = new CaptionPanel("Sensors");
	VerticalPanel verPanSensors = new VerticalPanel();
	HorizontalPanel horPanCS = new HorizontalPanel();
	Label lblCs = new Label("CS 1000");Label label_26 = new Label(" : ");Label cs1000 = new Label("");
	HorizontalPanel horPanMCS = new HorizontalPanel();
	Label lblMcs = new Label("MCS 1000");Label label_29 = new Label(" : ");Label mcs1000 = new Label("");
	HorizontalPanel horPanHLB = new HorizontalPanel();
	Label lblHydacLab_1 = new Label("HYDAC Lab");Label label_32 = new Label(" : ");Label hlb = new Label("");
	HorizontalPanel horPanDPS = new HorizontalPanel();
	Label lblDps = new Label("Differential Pressure Sensor");Label label_33 = new Label(" : ");Label dps = new Label("");
	HorizontalPanel horPanPS = new HorizontalPanel();
	Label lblPs = new Label("Pressure Sensor");Label label_3 = new Label(" : ");Label ps = new Label("");
	HorizontalPanel horPanButtons = new HorizontalPanel();
	Button btnProceed = new Button("PROCEED");Button btnReset = new Button("RESET");
	
	HashMap<String, Label> mapLabelObjects = new HashMap<String, Label>();
	HashMap<String, String> mapDataSourceInfo = null;
	
	
	
	public D() {

		// Fill up the map of Label objects
		fillMapLabelObjects();
		
		verPanMain.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verPanMain.setSpacing(10);
		verPanMain.setWidth("600px");
		
		// Initialise Composite widget
		initWidget(verPanMain);
		setWidth("600px");		
		
		capPanSource.setStyleName("blabla");
		verPanMain.add(capPanSource);
		capPanSource.setWidth("500px");
		
		verPanSource.setSpacing(5);
		capPanSource.setContentWidget(verPanSource);
		verPanSource.setWidth("500px");
			
		verPanSource.add(lblEnterSource);

		listWindTurbines();
	
//		GlobalInitializerService.Util.getInstance().listWindTurbines(new AsyncCallback<List<String>>() {
//			
//			@Override
//			public void onSuccess(List<String> result) {
//				for( String wt : result) {
//					listBox.addItem(wt);
//				}
//				
//				listBox.setSelectedIndex(-1);
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				Window.alert("Error in D.listWindTurbines");
//			}
//		});

		listBox.setVisibleItemCount(1);
		verPanSource.add(listBox);
				
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				capPanelWTInfo.setVisible(false);
				horPanButtons.setVisible(false);
				
				String wtSelected = listBox.getItemText( listBox.getSelectedIndex() );
				
				getDataSourceInfoFor(wtSelected);
				
				
				
//				GlobalInitializerService.Util.getInstance().getInfoFor(wtSelected, new AsyncCallback<Map<String,String>>() {
//					@Override
//					public void onFailure(Throwable caught) {
//						Window.alert("D.callback()");
//					}
//
//					@Override
//					public void onSuccess(Map<String, String> result) {
//						
//						if(!result.isEmpty()) {
//							for(Entry<String, String> entry : result.entrySet()) {
////								System.out.println("Callback result : " + entry.getKey() + " -- " + entry.getValue());
//								
//								String key = entry.getKey().toString().toLowerCase();
//								String value = entry.getValue().toString();
//								
////								System.out.println("Setting Labels in GUI for key" + key);
//				
//								Label temp = mapLabelObjects.get(key);
//								temp.setText(value);
//							}
//
//							capPanelWTInfo.setVisible(true);
//							horPanButtons.setVisible(true);
//						} else {
//							Window.alert("Selection not available.");
//						}
//					}
//				});
							
			}
		});
		
		capPanelWTInfo.setVisible(false);
		capPanelWTInfo.setStyleName("blabla");
		verPanMain.add(capPanelWTInfo);
		capPanelWTInfo.setWidth("500px");
	
		vePanWTInfo.setSpacing(5);
		capPanelWTInfo.setContentWidget(vePanWTInfo);
		vePanWTInfo.setWidth("500px");
		
		vePanWTInfo.add(verPanGeneral);
		verPanGeneral.setSize("472px", "");
		
		horPanName.setSpacing(5);
		verPanGeneral.add(horPanName);
		horPanName.setWidth("320px");
		
		lblName.setStyleName("gwt-Label-Bold");
		horPanName.add(lblName);
		lblName.setWidth("130px");
		
		label_1.setStyleName("gwt-Label-Bold");
		horPanName.add(label_1);
		label_1.setWidth("10px");
		
		horPanName.add(name);
		name.setWidth("100px");
		
		horPanLocation.setSpacing(5);
		verPanGeneral.add(horPanLocation);
		horPanLocation.setWidth("320px");
		
		lblLocation.setStyleName("gwt-Label-Bold");
		horPanLocation.add(lblLocation);
		lblLocation.setWidth("130px");
		
		label_2.setStyleName("gwt-Label-Bold");
		horPanLocation.add(label_2);
		label_2.setWidth("10px");
		
		horPanLocation.add(location);
		location.setWidth("100px");
		
		horPanDate.setSpacing(5);
		verPanGeneral.add(horPanDate);
		horPanDate.setWidth("320px");
		
		lblInstallationDate.setStyleName("gwt-Label-Bold");
		horPanDate.add(lblInstallationDate);
		lblInstallationDate.setWidth("130px");
		
		label_5.setStyleName("gwt-Label-Bold");
		horPanDate.add(label_5);
		label_5.setWidth("10px");
		
		horPanDate.add(instdate);
		instdate.setWidth("100px");
		
		capPanParts.setStyleName("blabla");
		vePanWTInfo.add(capPanParts);
		capPanParts.setWidth("450px");
		capPanParts.setContentWidget(verPanParts);
		verPanParts.setSize("449px", "0");
		
		horPanGB.setSpacing(5);
		verPanParts.add(horPanGB);
		horPanGB.setWidth("300px");
		
		
		lblGearbox.setStyleName("gwt-Label-Bold");
		horPanGB.add(lblGearbox);
		lblGearbox.setWidth("130px");
		
		
		label_7.setStyleName("gwt-Label-Bold");
		horPanGB.add(label_7);
		label_7.setWidth("10px");
		
		
		horPanGB.add(gearbox);
		gearbox.setWidth("100px");
		
		
		horPanRH.setSpacing(5);
		verPanParts.add(horPanRH);
		horPanRH.setWidth("300px");
		
		
		lblRotorHub.setStyleName("gwt-Label-Bold");
		horPanRH.add(lblRotorHub);
		lblRotorHub.setWidth("130px");
		
		
		label_16.setStyleName("gwt-Label-Bold");
		horPanRH.add(label_16);
		label_16.setWidth("10px");
		
		
		horPanRH.add(rotorhub);
		rotorhub.setWidth("100px");
		
		
		horPanOP.setSpacing(5);
		verPanParts.add(horPanOP);
		horPanOP.setWidth("300px");
		
		
		lblOilPump.setStyleName("gwt-Label-Bold");
		horPanOP.add(lblOilPump);
		lblOilPump.setWidth("130px");
		
		
		label_18.setStyleName("gwt-Label-Bold");
		horPanOP.add(label_18);
		label_18.setWidth("10px");
		
		
		horPanOP.add(oilpump);
		oilpump.setWidth("100px");
		
		
		horPanTBV.setSpacing(5);
		verPanParts.add(horPanTBV);
		horPanTBV.setWidth("300px");
		
		
		lblThermoBypassValve.setStyleName("gwt-Label-Bold");
		horPanTBV.add(lblThermoBypassValve);
		lblThermoBypassValve.setWidth("130px");
		
		
		label_21.setStyleName("gwt-Label-Bold");
		horPanTBV.add(label_21);
		label_21.setWidth("10px");
		
		
		horPanTBV.add(tbv);
		tbv.setWidth("100px");
		
		
		horPanCooler.setSpacing(5);
		verPanParts.add(horPanCooler);
		horPanCooler.setWidth("300px");
		
		
		lblCooler.setStyleName("gwt-Label-Bold");
		horPanCooler.add(lblCooler);
		lblCooler.setWidth("130px");
		
		
		label_24.setStyleName("gwt-Label-Bold");
		horPanCooler.add(label_24);
		label_24.setWidth("10px");
		
		
		horPanCooler.add(cooler);
		cooler.setWidth("100px");
		
		
		capPanSensors.setStyleName("blabla");
		vePanWTInfo.add(capPanSensors);
		capPanSensors.setWidth("450px");
		capPanSensors.setContentWidget(verPanSensors);
		verPanSensors.setSize("449px", "0");
		
		
		horPanCS.setSpacing(5);
		verPanSensors.add(horPanCS);
		horPanCS.setWidth("300px");
		
		
		lblCs.setStyleName("gwt-Label-Bold");
		horPanCS.add(lblCs);
		lblCs.setWidth("130px");
		
		
		label_26.setStyleName("gwt-Label-Bold");
		horPanCS.add(label_26);
		label_26.setWidth("10px");
		
		
		horPanCS.add(cs1000);
		cs1000.setWidth("100px");
		
		
		horPanMCS.setSpacing(5);
		verPanSensors.add(horPanMCS);
		horPanMCS.setWidth("300px");
		
		
		lblMcs.setStyleName("gwt-Label-Bold");
		horPanMCS.add(lblMcs);
		lblMcs.setWidth("130px");
		
		
		label_29.setStyleName("gwt-Label-Bold");
		horPanMCS.add(label_29);
		label_29.setWidth("10px");
		
		
		horPanMCS.add(mcs1000);
		mcs1000.setWidth("100px");
		
		
		horPanHLB.setSpacing(5);
		verPanSensors.add(horPanHLB);
		horPanHLB.setWidth("300px");
		
		
		lblHydacLab_1.setStyleName("gwt-Label-Bold");
		horPanHLB.add(lblHydacLab_1);
		lblHydacLab_1.setWidth("130px");
		
		
		label_32.setStyleName("gwt-Label-Bold");
		horPanHLB.add(label_32);
		label_32.setWidth("10px");
		
		
		horPanHLB.add(hlb);
		hlb.setWidth("100px");
		horPanPS.setSpacing(5);
		
		verPanSensors.add(horPanPS);
		horPanPS.setWidth("300px");
		lblPs.setStyleName("gwt-Label-Bold");
		
		horPanPS.add(lblPs);
		lblPs.setWidth("130px");
		label_3.setStyleName("gwt-Label-Bold");
		
		horPanPS.add(label_3);
		label_3.setWidth("10px");
		
		horPanPS.add(ps);
		ps.setWidth("100px");
		
		horPanButtons.setVisible(false);
		horPanButtons.setSpacing(5);
		horPanButtons.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		horPanButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verPanMain.add(horPanButtons);
		
		horPanButtons.setWidth("523px");
		
		horPanButtons.add(btnProceed);
		horPanButtons.add(btnReset);
		
		btnProceed.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				sendDataSourceInfo();
				
				RootPanel.get("contentContainer").clear();
				RootPanel.get("contentContainer").add(new E());
			}
		});
	}
	
	
	/**
	 *  Fill the map with <String key, Label label> 
	 *  String key corresponds to Key in Information file
	 *  Label label corresponds to Place where the Value will be displayed in GUI 
	 * 
	 */
	private void fillMapLabelObjects() {
		mapLabelObjects.put("name", name);
		mapLabelObjects.put("location", location);
		mapLabelObjects.put("installation date", instdate);
		
		mapLabelObjects.put("gearbox", gearbox);
		mapLabelObjects.put("rotor hub", rotorhub);
		mapLabelObjects.put("oil pump", oilpump);
		mapLabelObjects.put("thermo bypass valve", tbv);
		mapLabelObjects.put("cooler", cooler);
		
		mapLabelObjects.put("cs 1000", cs1000);
		mapLabelObjects.put("mcs 1000", mcs1000);
		mapLabelObjects.put("hydac lab", hlb);
		mapLabelObjects.put("differential pressure sensor", dps);
		mapLabelObjects.put("pressure sensor", ps);
	}
	
	private void listWindTurbines() {
		GlobalInitializerService.Util.getInstance().listWindTurbines(new AsyncCallback<List<String>>() {
					@Override
			public void onSuccess(List<String> result) {
				for( String wt : result) {
					listBox.addItem(wt);
				}
				listBox.setSelectedIndex(-1);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error in D.listWindTurbines");
			}
		});
	}
	
	private void getDataSourceInfoFor(String wtName) {
		
		GlobalInitializerService.Util.getInstance().getInfoFor(wtName, new AsyncCallback<Map<String,String>>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("D.callback()");
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				
				if(!result.isEmpty()) {
					mapDataSourceInfo = (HashMap<String, String>) result;
				
					for(Entry<String, String> entry : result.entrySet()) {
//						System.out.println("Callback result : " + entry.getKey() + " -- " + entry.getValue());
						
						String key = entry.getKey().toString().toLowerCase();
						String value = entry.getValue().toString();
						
//						System.out.println("Setting Labels in GUI for key" + key);
		
						Label temp = mapLabelObjects.get(key);
						temp.setText(value);
					}

					capPanelWTInfo.setVisible(true);
					horPanButtons.setVisible(true);
					
					
				} else {
					Window.alert("Selection not available.");
				}
			}
		});
	}

	private void sendDataSourceInfo() {
		RDFEncoderServiceUtil.getInstance().setDataSourceInfo(mapDataSourceInfo, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				Window.alert("Abox File saved to provided location");
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}
}


