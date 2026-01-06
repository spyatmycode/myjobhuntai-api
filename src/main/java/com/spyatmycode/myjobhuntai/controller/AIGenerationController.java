package com.spyatmycode.myjobhuntai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spyatmycode.myjobhuntai.dto.aiGeneration.AICoverLetterAnalysisResponse;
import com.spyatmycode.myjobhuntai.dto.aiGeneration.AIGenerationBodyDTO;
import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;
import com.spyatmycode.myjobhuntai.model.CandidateUserDetails;
import com.spyatmycode.myjobhuntai.service.AIGenerationService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/ai")
public class AIGenerationController {

    private final AIGenerationService aiGenerationService;


    public AIGenerationController(
        AIGenerationService aiGenerationService
    ){
        this.aiGenerationService = aiGenerationService;
    }  
    
    @PostMapping("/generate-cover-letter")
    public ResponseEntity<ApiResponse<AICoverLetterAnalysisResponse>> generateCoverLetter(
        @RequestParam Long candidateId,
        @RequestParam Long resumeId,
        @RequestParam Long jobApplicationId,
        @RequestBody @Valid AIGenerationBodyDTO optionalUserPrompt

    ){
         Long actualCandidateId =((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCandidateId();

         if(actualCandidateId.equals(candidateId) == false){
            throw new BadCredentialsException("You cannot perform this request for this user.");
         };
        var aiResponse = aiGenerationService.generateCoverLetter(
            resumeId,
            jobApplicationId,
            candidateId,
            optionalUserPrompt.optionalUserPrompt()
        );

        return ApiResponse.success( HttpStatus.OK, aiResponse , "Cover letter generated", null);
    }
    
}
