package com.icmwind.gui.web.client.helpers;

public class DataToUpload {
	private String urlOfFile;
	private String startingDate;
	private int daysToAnalyse;
	
	public DataToUpload(String urlOfFile, String startingDate, int daysToAnalyse) {
		this.urlOfFile = urlOfFile;
		this.startingDate = startingDate;
		this.daysToAnalyse = daysToAnalyse;
	}

	public String getUrlOfFile() {
		return urlOfFile;
	}

	public void setUrlOfFile(String urlOfFile) {
		this.urlOfFile = urlOfFile;
	}

	public String getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(String startingDate) {
		this.startingDate = startingDate;
	}

	public int getDaysToAnalyse() {
		return daysToAnalyse;
	}

	public void setDaysToAnalyse(int daysToAnalyse) {
		this.daysToAnalyse = daysToAnalyse;
	}
	
	
}
