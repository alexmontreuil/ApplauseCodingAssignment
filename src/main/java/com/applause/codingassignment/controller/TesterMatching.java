package com.applause.codingassignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.applause.codingassignment.domain.TesterInfo;
import com.applause.codingassignment.domain.TesterMatchCriteria;
import com.applause.codingassignment.service.TesterMatchService;

@RestController
@RequestMapping("/api")
public class TesterMatching {
	
	@Autowired
	private TesterMatchService matchService;
	
	@PostMapping("/TesterMatch")
	public List<TesterInfo> getTesterMatches(@RequestBody TesterMatchCriteria criteria){
		return matchService.getMatches(criteria);
	}

}
