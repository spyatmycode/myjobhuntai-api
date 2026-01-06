package com.spyatmycode.myjobhuntai.dto.candidateResume;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewCandidateResumeDTO(
    @NotBlank(message = "Resume title cannot be blank") String title,
    @NotBlank(message = "Resume summary cannot be blank") String resumeSummary,
    @NotBlank(message = "Resume skills cannot be blank") String  skills,
    @NotNull(message = "Candidate ID cannot be null") Long candidateId,
    String resumeUrl
) {};
