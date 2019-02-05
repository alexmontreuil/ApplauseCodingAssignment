package com.applause.codingassignment.service;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.applause.codingassignment.TesterMatchConfiguration;
import com.applause.codingassignment.domain.TesterInfo;
import com.applause.codingassignment.domain.TesterMatchCriteria;

@Service
public class TesterMatchService {
	
	@Autowired
	private TesterMatchConfiguration config;	

	private Iterable<CSVRecord> testerRecords;
	private Iterable<CSVRecord> bugRecords;
	private Iterable<CSVRecord> deviceRecords;
	private Iterable<CSVRecord> testerDeviceRecords;
	private Map<String, TesterInfo> testerInfo = new HashMap<>(); 
	
	@PostConstruct
	public void init() {
		try {
			Reader inTesters = new FileReader(config.getTesterRecordsPath());
			CSVParser csvParser = new CSVParser(inTesters,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			testerRecords = csvParser.getRecords();			
			csvParser.close();
			

			Reader inBugs = new FileReader(config.getBugRecordsPath());
			csvParser = new CSVParser(inBugs,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			bugRecords = csvParser.getRecords();			
			csvParser.close();

			Reader inDevices = new FileReader(config.getDeviceRecordsPath());
			csvParser = new CSVParser(inDevices,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			deviceRecords = csvParser.getRecords();			
			csvParser.close();
			
			Reader inTesterDevices = new FileReader(config.getTesterDeviceRecordsPath());
			csvParser = new CSVParser(inTesterDevices,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			testerDeviceRecords = csvParser.getRecords();			
			csvParser.close();
			
			setTesterInfo();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	
	public void setTesterInfo(){
		testerRecords.forEach(o -> 	bugRecords.forEach(b -> 
			{
				TesterInfo info = null;
				if(!testerInfo.containsKey(o.get("testerId"))) {
					info = new TesterInfo();
					info.setCountry(o.get("country"));
					info.setId(o.get("testerId"));
					info.setFirstName(o.get("firstName"));
					info.setLastName(o.get("lastName"));
					testerInfo.put(o.get("testerId"), info);
					
				}else {
					info = testerInfo.get(o.get("testerId"));
				}			
			}
		));
	}
	
	public List<TesterInfo> getMatches(final TesterMatchCriteria criteria){
		Set<TesterInfo> testersForCountry = getTestersForCountry(criteria.getCountries());
		Set<TesterInfo> testers = getTestersForDevice(testersForCountry, criteria.getDevices());
		List<TesterInfo> sortedTesters = new ArrayList<TesterInfo>(testers);
		Collections.sort(sortedTesters, 
				new Comparator<TesterInfo>() {
		            @Override
		            public int compare(TesterInfo o1, TesterInfo o2) {
		            	int compare = 0;
		            	if(o2.getExperience() > o1.getExperience()) {
		            		compare = 1;
		            	}else if(o2.getExperience() < o1.getExperience()) {
		            		compare = -1;
		            	}
		                return compare;
		            }
        		}				
		);
		return sortedTesters;
	}	

	private Set<TesterInfo> getTestersForCountry(final String country){
		StringTokenizer tokens = new StringTokenizer(country,",");
		List<String> countries = new ArrayList<>();
		while(tokens.hasMoreTokens()) {
			countries.add(tokens.nextToken().trim());
		}
		Collection<TesterInfo> testerInfos = testerInfo.values();
		Set<TesterInfo> testers  = testerInfos.parallelStream().filter(info -> 
			countries.contains(info.getCountry()) || country.equalsIgnoreCase("ALL")
		).collect(Collectors.toSet());
		resetTesters(testers);
		return testers;
	}
	
	private Set<TesterInfo> getTestersForDevice(final Set<TesterInfo> testersForCountry, final String device){
		StringTokenizer tokens = new StringTokenizer(device,",");
		List<String> devices = new ArrayList<>();
		while(tokens.hasMoreTokens()) {
			devices.add(tokens.nextToken().trim());
		}
		Set<TesterInfo> testers = new HashSet<>();
		testersForCountry.forEach(info 
				-> testerDeviceRecords.forEach(testerDevice 
						-> deviceRecords.forEach(deviceRecord 
								-> bugRecords.forEach(bug ->
				{
					if(info.getId().equals(testerDevice.get("testerId")) 
							&& deviceRecord.get("deviceId").equals(testerDevice.get("deviceId"))
							&& (devices.contains(deviceRecord.get("description")) || device.equalsIgnoreCase("ALL"))) {
						testers.add(info);
					}
				}
		))));
		setTesterExperience(testers, devices, device.equalsIgnoreCase("ALL") ? true : false);
		return testers;
	}
	
	private void setTesterExperience(Set<TesterInfo> testers, List<String> devices, boolean all) {

		testers.parallelStream().forEach(info 
				-> bugRecords.forEach(bugRecord 
						-> testerDeviceRecords.forEach(testerDeviceRecord 
								-> deviceRecords.forEach(deviceRecord -> 
			{
				if(info.getId().equals(bugRecord.get("testerId"))
					&& bugRecord.get("testerId").equals(info.getId())
					&& bugRecord.get("deviceId").equals( testerDeviceRecord.get("deviceId"))
					&& testerDeviceRecord.get("testerId").equals(info.getId())
					&& deviceRecord.get("deviceId").equals(testerDeviceRecord.get("deviceId"))
					&& (devices.contains(deviceRecord.get("description")) || all)
						) {
					info.setExperience(info.getExperience() + 1);
				}
			}
		))));
	}
	
	private void resetTesters(Set<TesterInfo> testers) {
		testers.parallelStream().forEach(info -> 
			{
				info.setExperience(0);
			}
		);
	}
}
