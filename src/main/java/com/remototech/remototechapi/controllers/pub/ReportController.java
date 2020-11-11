package com.remototech.remototechapi.controllers.pub;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Report;
import com.remototech.remototechapi.repositories.ReportRepository;

@RestController
@RequestMapping("public/report")
public class ReportController {

	@Autowired
	private ReportRepository reportRepository;

	@PostMapping
	public void create(@Valid @RequestBody Report report) {
		reportRepository.save( report );
	}

}
