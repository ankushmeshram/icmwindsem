package com.icmwind.gui.web.client.helpers;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author anme05
 * 
 * Utility class for Client.
 *
 */
public class ClientUtils {
	
	static final String[] concepts = {"Sensor"};
	static final String[] properties = {"", "measures", "hasLocation", "isSensorOf"};
	static final String[] measuredProperties = {"", "Temperature", "Pressure", "Flow"};
	static final String[] featureOfInterests = {"", "Oil", "Air"};
	static final String[] locations = {"", "Marpingen", "Dudweiler", "Dueren"};
	static final String[] subSystems = {"", "Gear Box", "Generator", "Rotor Hub", "Oil Pump"};
	
	static final String[] res = {"600px", "400px"}; 
	
	static RootPanel headerHolder = RootPanel.get("headerContainer");
	
	public static void setTitle(String title) {
		headerHolder.clear();
		headerHolder.add(new HTML(title));
	}

	public static String[] getRes() {
		return res;
	}

	public static String[] getConcepts() {
		return concepts;
	}

	public static String[] getProperties() {
		return properties;
	}

	public static String[] getMeasuredproperties() {
		return measuredProperties;
	}

	public static String[] getFeatureofinterests() {
		return featureOfInterests;
	}

	public static String[] getLocations() {
		return locations;
	}
	
	public static String[] getSubsystems() {
		return subSystems;
	}


}
