package com.remototech.remototechapi.controllers.pub;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Report;
import com.remototech.remototechapi.repositories.ReportRepository;
import com.remototech.remototechapi.services.CandidateService;

@RestController
@RequestMapping("public/report")
public class ReportController {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private CandidateService candidateService;

	@PostMapping
	public void create(@Valid @RequestBody Report report) {
		reportRepository.save( report );
	}

	@GetMapping
	public List<Candidate> findAllWithIncomplete() {
		return candidateService.findCandidatesWithIncompleteProfile();
	}

}
