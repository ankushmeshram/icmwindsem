package com.icmwind.gui.web.client.helpers;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ObservationPeriod implements IsSerializable {

	Date beginPeriod = null;
	Date endPeriod = null;
	int observationFrequency = 0;
	
	public Date getBeginPeriod() {
		return beginPeriod;
	}
	
	public void setBeginPeriod(Date beginPeriod) {
		this.beginPeriod = beginPeriod;
	}
	
	public Date getEndPeriod() {
		return endPeriod;
	}
	
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}
	
	public int getObservationFrequency() {
		return observationFrequency;
	}
	
	public void setObservationFrequency(int observationFrequency) {
		this.observationFrequency = observationFrequency;
	}
	
	
}
