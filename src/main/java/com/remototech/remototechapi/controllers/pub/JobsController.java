package com.remototech.remototechapi.controllers.pub;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.entities.Job;
import com.remototech.remototechapi.services.JobsService;
import com.remototech.remototechapi.vos.JobsFilter;

@RestController
@RequestMapping("public/jobs")
public class JobsController {

	@Autowired
	private JobsService jobsService;

	@GetMapping
	public Page<Job> findAll(
			final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
			final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize,
			JobsFilter filter) throws IOException {
		return jobsService.findAllByFilter( filter, pageIndex < 1 ? 0 : pageIndex - 1, resultSize );
	}

	@PostMapping("{uuid}/apply")
	public void apply(@PathVariable("uuid") UUID jobUuid, @RequestParam("linkedin-url") String linkedInUrl) {
		jobsService.apply( jobUuid, linkedInUrl );
	}

}
