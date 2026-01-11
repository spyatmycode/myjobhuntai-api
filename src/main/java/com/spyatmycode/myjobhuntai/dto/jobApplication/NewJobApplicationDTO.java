package com.spyatmycode.myjobhuntai.dto.jobApplication;

import com.spyatmycode.myjobhuntai.model.JobApplicationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record NewJobApplicationDTO(
    @NotBlank(
        message = "Company name cannot be blank"
    ) String companyName,
    @NotBlank(
        message = "Job title cannot be blank"
    ) String jobTitle,
    @NotBlank(
        message = "Job description cannot be blank"
    ) String jobDescription,
    String aiCoverLetter,
    @NotNull JobApplicationStatus status,
    @NotNull(message = "Candidate ID cannot be null") Long candidateId,
     String extraNotes
){

}
