package com.remototech.remototechapi.controllers.priv;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.controllers.LoggedInController;
import com.remototech.remototechapi.dtos.CandidateDTO;
import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Candidature;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.entities.Role;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.CandidatureService;
import com.remototech.remototechapi.services.JobsService;
import com.remototech.remototechapi.vos.JobsFilter;

@RestController
@RequestMapping("private/my-jobs")
@PreAuthorize("hasAnyAuthority('ADMIN', 'COMPANY')")
public class MyJobsController extends LoggedInController {

	@Autowired
	private JobsService jobsService;

	@Autowired
	private CandidatureService candidatureService;

	@GetMapping
	public Page<Job> findAll(
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize,
			JobsFilter filter) throws IOException {
		Login loggedUser = getLoggedUser();
		return jobsService.findAllByFilterAndTenant( filter, pageIndex - 1, resultSize, loggedUser.getTenant() );
	}

	@GetMapping("{uuid}")
	public Job findById(@PathVariable("uuid") UUID uuid) {
		Login loggedUser = getLoggedUser();
		return jobsService.findByIdAndTenant( uuid, loggedUser.getTenant() );
	}

	@PostMapping
	public Job create(@Valid @RequestBody Job job) {
		Login loggedUser = getLoggedUser();
		return jobsService.create( job, loggedUser.getTenant() );
	}

	@DeleteMapping("{uuid}")
	public void remove(@PathVariable("uuid") UUID uuid) {
		jobsService.removeByUuidAndTenant( uuid, getLoggedUser().getTenant() );
	}

	@PostMapping("/close/{uuid}")
	public void close(@PathVariable("uuid") UUID uuid) throws GlobalException {
		jobsService.close( uuid, getLoggedUser().getTenant() );
	}

	@PostMapping("/reopen/{uuid}")
	public void reopen(@PathVariable("uuid") UUID uuid) throws GlobalException {
		jobsService.reopen( uuid, getLoggedUser().getTenant() );
	}

	@GetMapping("{uuid}/candidates")
	public Set<CandidateDTO> getCandidates(@PathVariable("uuid") UUID jobUuid) throws GlobalException {
		Login loggedUser = getLoggedUser();
		return candidatureService.findByJobUuidAndTenantUuid( jobUuid, loggedUser.getTenant().getUuid() );
	}

	@GetMapping("{uuid}/candidates/{candidate_uuid}")
	public Candidature getCandidature(@PathVariable("uuid") UUID jobUuid, @PathVariable("candidate_uuid") UUID candidateUuid) {
		Login loggedUser = getLoggedUser();
		if (Role.ADMIN.equals( loggedUser.getRole() )) {
			return candidatureService.findByJobUuidAndCandidate( jobUuid, candidateUuid );
		}
		return candidatureService.findByJobUuidTenantAndCandidate( jobUuid, loggedUser.getTenant().getUuid(), candidateUuid );
	}

}
