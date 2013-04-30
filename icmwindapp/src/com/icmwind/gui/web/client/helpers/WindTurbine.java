package com.icmwind.gui.web.client.helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class WindTurbine {
	
	Map<String, String> mapWTInfo = new HashMap<>();
	
	Properties info = new Properties();
	
	public void readWindTurbineInfo(String wind_turbine_name) {
		String path = GlobalInitializer.get().getSourceForWindTurbine(wind_turbine_name);
		
		try {
			FileInputStream filestream = new FileInputStream(path);
			
			try {
				info.load(filestream);
				
				for(Entry<Object, Object> entry : info.entrySet()) {
					mapWTInfo.put(entry.getKey().toString(), entry.getValue().toString());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Error in WindTurbine.readWindTurbineInfo()....File doesn't exist");
		}
	}
	
	// TODO make it serializable
	public Map<String, String> getWindTurbineInfo() {
		return mapWTInfo;
	}

}
