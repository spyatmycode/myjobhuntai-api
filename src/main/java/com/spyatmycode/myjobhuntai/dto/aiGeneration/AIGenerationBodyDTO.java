package com.spyatmycode.myjobhuntai.dto.aiGeneration;

import jakarta.validation.constraints.NotBlank;

public record AIGenerationBodyDTO(
    String optionalUserPrompt,
    @NotBlank(message = "Please enter a title for this resume.") String title
) {}
