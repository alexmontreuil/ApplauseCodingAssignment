package com.applause.codingassignment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration("testerMatchProperties")
@ConfigurationProperties("app")
public class TesterMatchConfiguration {
	
	private String testerRecordsPath;
	private String bugRecordsPath;
	private String deviceRecordsPath;
	private String testerDeviceRecordsPath;
	public String getTesterRecordsPath() {
		return testerRecordsPath;
	}
	public void setTesterRecordsPath(String testerRecordsPath) {
		this.testerRecordsPath = testerRecordsPath;
	}
	public String getBugRecordsPath() {
		return bugRecordsPath;
	}
	public void setBugRecordsPath(String bugRecordsPath) {
		this.bugRecordsPath = bugRecordsPath;
	}
	public String getDeviceRecordsPath() {
		return deviceRecordsPath;
	}
	public void setDeviceRecordsPath(String deviceRecordsPath) {
		this.deviceRecordsPath = deviceRecordsPath;
	}
	public String getTesterDeviceRecordsPath() {
		return testerDeviceRecordsPath;
	}
	public void setTesterDeviceRecordsPath(String testerDeviceRecordsPath) {
		this.testerDeviceRecordsPath = testerDeviceRecordsPath;
	}
	
	
}
