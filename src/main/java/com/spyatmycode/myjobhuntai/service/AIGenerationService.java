package com.spyatmycode.myjobhuntai.service;


import java.util.Map;
import java.util.function.Consumer;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.PromptUserSpec;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spyatmycode.myjobhuntai.dto.aiGeneration.AICoverLetterAnalysisResponse;
import com.spyatmycode.myjobhuntai.dto.aiGeneration.AIResumeSummaryResponse;
import com.spyatmycode.myjobhuntai.prompts.Prompts;


import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AIGenerationService {

    private final CandidateResumeService candidateResumeService;
    private final CandidateProfileService candidateProfileService;
    final private JobApplicationService jobApplicationService;

    private final ChatClient chatClient;
    public final DocumentParsingService documentParsingService;

    public AIGenerationService(
            ChatClient.Builder chatBuilder,
            DocumentParsingService documentParsingService, 
            CandidateResumeService candidateResumeService,
            CandidateProfileService candidateProfileService,
            JobApplicationService jobApplicationService
        ) {
        this.jobApplicationService = jobApplicationService;
        this.candidateResumeService = candidateResumeService;
        this.candidateProfileService = candidateProfileService;
        this.chatClient = chatBuilder.build();
        this.documentParsingService = documentParsingService;
    }

    public AICoverLetterAnalysisResponse generateCoverLetter(
            Long resumeId,
            Long jobApplicationId,
            Long candidateId,
            String optionalUserPrompt){
        log.info("Checking records for candidate with ID {}", candidateId);

        var candidateProfile = candidateProfileService.findById(candidateId);
               
        log.info("Checking records for resume with ID {}", resumeId);
        var candidateResume = candidateResumeService.findCandidateResumebyId(resumeId, candidateId);

        var resumeContent = documentParsingService.parsePDF(candidateResume.getFilePath());

        log.info("Checking records for job application with ID {}", jobApplicationId);
        var jobApplication = jobApplicationService.findById(jobApplicationId);

        var converter = new BeanOutputConverter<>(AICoverLetterAnalysisResponse.class);

        // Handle null optional prompt
        String safeOptionalPrompt = (optionalUserPrompt != null && !optionalUserPrompt.isBlank())
                ? optionalUserPrompt
                : "No additional instructions";

        String promptText = Prompts.coverLetterPrompt;

        // Create a map of variables
        Map<String, Object> variables = Map.of(
                "name", candidateProfile.getFullName(),
                "email", candidateProfile.getEmail(),
                "phoneNumber", candidateProfile.getCountryPhoneCode() + candidateProfile.getPhoneNumber(),
                "resumeSummary", resumeContent,
                "skills", candidateResume.getSkills(),
                "jobDescription", jobApplication.getJobDescription(),
                "companyName", jobApplication.getCompanyName(),
                "optionalUserPrompt", safeOptionalPrompt,
                "format", converter.getFormat());


        log.info("Making the API Call to OpenAI");
        AICoverLetterAnalysisResponse aiResponse = chatClient.prompt()
                .user(u -> u.text(promptText).params(variables))
                .call()
                .entity(AICoverLetterAnalysisResponse.class);

        log.info("Updating job application with generated cover letter");

        jobApplicationService.updateAiCoverLetter(jobApplicationId, candidateId, aiResponse.coverLetter());

        return aiResponse;
    };

    public AIResumeSummaryResponse generateResumeSummary(
            MultipartFile file, String optionalUserPrompt,
            Long canidateId
        )  {

        var candidate = candidateProfileService.findById(canidateId);

        String prompt = Prompts.resumeSummaryPrompt;

        var converter = new BeanOutputConverter<>(AIResumeSummaryResponse.class);

        optionalUserPrompt = optionalUserPrompt != null && !optionalUserPrompt.isBlank() ? optionalUserPrompt : "No additional instructions";

        String parsedResumeText = documentParsingService.parsePDF(file);

        Map<String, Object> promptParameters = Map.of(
            "name", candidate.getFullName(),
            "phoneNumber", candidate.getPhoneNumber(),
            "email", candidate.getEmail(),
            "resumeText", parsedResumeText,
            "optionalUserPrompt", optionalUserPrompt,
            "format", converter.getFormat()
        );

        AIResumeSummaryResponse aiResponse = chatClient.prompt().user(new Consumer<PromptUserSpec>() {
            @Override
            public void accept(PromptUserSpec t) {
                t.text(prompt).params(promptParameters);
            }
        }).call().entity(AIResumeSummaryResponse.class);


        return aiResponse;


    }

}