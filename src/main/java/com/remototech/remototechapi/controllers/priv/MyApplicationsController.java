package com.remototech.remototechapi.controllers.priv;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.services.CandidateService;
import com.remototech.remototechapi.services.CandidatureService;
import com.remototech.remototechapi.vos.JobsFilter;

@RestController
@RequestMapping("private/my-applications")
@PreAuthorize("hasAuthority('CANDIDATE')")
public class MyApplicationsController extends LoggedInController {

	@Autowired
	private CandidatureService candidatureService;
	
	@Autowired
	private CandidateService candidateService;

	@GetMapping("{job_uuid}")
	public Candidature getCandidature(@PathVariable("job_uuid") UUID jobUuid) {
		Login loggedUser = getLoggedUser();
		Candidate candidate = candidateService.getOrCreateIfNotExists( loggedUser );
		return candidatureService.findByJobUuidAndCandidate( jobUuid, candidate.getUuid() );
	}

	@GetMapping
	public Page<Candidature> findAllByFilterAndCandidature(
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize,
			JobsFilter filter) throws IOException {
		Login loggedUser = getLoggedUser();
		return candidatureService.findAllByFilterAndCandidature( filter, pageIndex - 1, resultSize, loggedUser );
	}

}
