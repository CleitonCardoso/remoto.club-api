package com.remototech.remototechapi.controllers.priv;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.remototech.remototechapi.exceptions.GlobalException;
import com.remototech.remototechapi.services.JobsService;

@RestController
@RequestMapping("private/jobs")
public class PrivateJobsController extends LoggedInController {

	@Autowired
	private JobsService jobsService;

	@PostMapping("{uuid}/apply")
	public void apply(@PathVariable("uuid") UUID jobUuid) throws GlobalException {
		jobsService.apply( jobUuid, getLoggedUser() );
	}

}
