package com.remototech.remototechapi.controllers.priv.admin;

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

import com.remototech.remototechapi.controllers.priv.LoggedInController;
import com.remototech.remototechapi.entities.Candidate;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.AdminJobsService;
import com.remototech.remototechapi.vos.JobsFilter;

@RestController
@RequestMapping("private/admin-jobs")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminJobsController extends LoggedInController {

	@Autowired
	private AdminJobsService adminJobsService;

	@GetMapping
	public Page<Job> findAll(
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize,
			JobsFilter filter) throws IOException {
		return adminJobsService.findAllByFilter( filter, pageIndex - 1, resultSize );
	}

	@GetMapping("{uuid}")
	public Job findById(@PathVariable("uuid") UUID uuid) {
		return adminJobsService.findById( uuid );
	}

	@PostMapping
	public Job create(@Valid @RequestBody Job job) {
		return adminJobsService.create( job );
	}

	@DeleteMapping("{uuid}")
	public void remove(@PathVariable("uuid") UUID uuid) {
		adminJobsService.removeByUuid( uuid );
	}

	@PostMapping("/close/{uuid}")
	public void close(@PathVariable("uuid") UUID uuid) throws GlobalException {
		adminJobsService.close( uuid );
	}

	@PostMapping("/reopen/{uuid}")
	public void reopen(@PathVariable("uuid") UUID uuid) throws GlobalException {
		adminJobsService.reopen( uuid );
	}

	@GetMapping("{uuid}/candidates")
	public Set<Candidate> getCandidates(@PathVariable("uuid") UUID jobUuid) {
		return adminJobsService.getCandidatesFrom( jobUuid );
	}

}
