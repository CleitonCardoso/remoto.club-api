package com.remototech.remototechapi.controllers.priv;

import com.remototech.remototechapi.controllers.LoggedInController;
import com.remototech.remototechapi.entities.Candidature;
import com.remototech.remototechapi.entities.Login;
import com.remototech.remototechapi.services.CandidatureService;
import com.remototech.remototechapi.vos.JobsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("private/my-applications")
public class MyApplicationsController extends LoggedInController {

    @Autowired
    private CandidatureService candidatureService;

    @GetMapping("{job_uuid}")
    public Candidature getCandidature(@PathVariable("job_uuid") UUID jobUuid) {
        Login loggedUser = getLoggedUser();
        return candidatureService.findByJobUuidAndCandidate(jobUuid, loggedUser);
    }

    @GetMapping
    public Page<Candidature> findAllByFilterAndCandidature(
            final @RequestParam(value = "page-index", defaultValue = "0", required = false) Integer pageIndex,
            final @RequestParam(value = "result-size", defaultValue = "10", required = false) Integer resultSize,
            JobsFilter filter) throws IOException {
        Login loggedUser = getLoggedUser();
        return candidatureService.findAllByFilterAndCandidature(filter, pageIndex - 1, resultSize, loggedUser);
    }

}
