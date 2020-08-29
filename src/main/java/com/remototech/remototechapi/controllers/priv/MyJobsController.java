package com.remototech.remototechapi.controllers.priv;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.controllers.LoggedInController;
import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.services.JobsService;
import com.remototech.remototechapi.vos.JobsFilter;

@RestController
@RequestMapping("private/my-jobs")
public class MyJobsController extends LoggedInController {

	@Autowired
	private JobsService jobsService;

	@GetMapping
	public Page<Job> findAll(
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize,
			JobsFilter filter) throws IOException {
		Login loggedUser = getLoggedUser();
		return jobsService.findAllByFilterAndTenant( filter, pageIndex - 1, resultSize, loggedUser.getTenant() );
	}

	@PostMapping
	public Job create(@RequestBody Job job) {
		Login loggedUser = getLoggedUser();
		return jobsService.create( job, loggedUser.getTenant() );
	}

}
