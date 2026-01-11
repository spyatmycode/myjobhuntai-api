package com.spyatmycode.myjobhuntai.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;
import com.spyatmycode.myjobhuntai.dto.jobApplication.NewJobApplicationDTO;
import com.spyatmycode.myjobhuntai.model.CandidateUserDetails;
import com.spyatmycode.myjobhuntai.model.JobApplication;
import com.spyatmycode.myjobhuntai.model.JobApplicationStatus;
import com.spyatmycode.myjobhuntai.service.JobApplicationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/job-application")
@Slf4j
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
        JobApplicationService jobApplicationService
    ){
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/add-job-application")
    public ResponseEntity<ApiResponse<JobApplication>> getCandidateResume(
        @RequestBody @Valid NewJobApplicationDTO newJobApplicationDTO
    ){
      
          Long candidateId =((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCandidateId();

         if(newJobApplicationDTO.candidateId().equals(candidateId) == false){
            log.info("candidate id={} and body candidate id={}", candidateId, newJobApplicationDTO.candidateId());
            throw new BadCredentialsException("You cannot perform this request for this user. Profile may not exist. or Wrong token.");
         };
        return ApiResponse.success(HttpStatus.OK, jobApplicationService.addJobApplication(newJobApplicationDTO), "Created successfully successfully", null);
    };

    @DeleteMapping("/delete-job-application/{id}")
    public ResponseEntity<ApiResponse<Void>> getCandidateResume(
        @PathVariable Long id
    ){
        jobApplicationService.removeJobApplication(id);
        return ApiResponse.success(HttpStatus.OK, null, "Deleted successfully", null);
    };
    
    @PutMapping("/update-job-application-status/{id}")
   public ResponseEntity<ApiResponse<JobApplication>> updateJobApplicationStatus(
        @PathVariable Long id,
        @RequestParam JobApplicationStatus jobApplicationStatus
    ){
       
        return ApiResponse.success(HttpStatus.OK,  jobApplicationService.updateJobApplicationStatus(id, jobApplicationStatus), "Updated successfully", null);
    };

    @PutMapping("/update-job-application/{id}")
public ResponseEntity<ApiResponse<JobApplication>> updateJobApplication(
    @PathVariable Long id,
    @RequestBody @Valid NewJobApplicationDTO updateDTO
) {
    Long candidateId = ((CandidateUserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal()).getCandidateId();

    if (!updateDTO.candidateId().equals(candidateId)) {
        log.info("candidate id={} and body candidate id={}", candidateId, updateDTO.candidateId());
        throw new BadCredentialsException("You cannot perform this request for this user. Profile may not exist or wrong token.");
    }

    JobApplication updatedApplication = jobApplicationService.updateJobApplication(
        id, 
        candidateId, 
        updateDTO
    );

    return ApiResponse.success(
        HttpStatus.OK, 
        updatedApplication, 
        "Updated successfully", 
        null
    );
}

    @GetMapping("/get-job-application/{id}")
    public ResponseEntity<ApiResponse<JobApplication>> getJobApplication(
        @PathVariable Long id
    ){
        return ApiResponse.success(HttpStatus.OK, jobApplicationService.getSingleCandidateApplicationById(id), "Retrieved successfully", null);
    };

    @GetMapping("/get-candidate-job-applications/{id}")
    public ResponseEntity<ApiResponse<List<JobApplication>>> getCandidateJobApplications(
        @PathVariable Long id
    ){
         Long candidateId =((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCandidateId();

         if(id.equals(candidateId) == false){
            throw new BadCredentialsException("You cannot perform this request for this user.");
         };
        return ApiResponse.success(HttpStatus.OK, jobApplicationService.getAllCandidateApplications(id), "Retrieved successfully", null);
    };

// @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/get-all-job-applications")
    public ResponseEntity<ApiResponse<Iterable<JobApplication>>> getAllJobApplications(
    ){
        return ApiResponse.success(HttpStatus.OK, jobApplicationService.getAllJobApplications(), "Retrieved successfully", null);
    }; 
}
