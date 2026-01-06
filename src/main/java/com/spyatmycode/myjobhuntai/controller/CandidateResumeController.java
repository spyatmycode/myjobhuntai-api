package com.spyatmycode.myjobhuntai.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.spyatmycode.myjobhuntai.dto.aiGeneration.AIResumeSummaryResponse;
import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;
import com.spyatmycode.myjobhuntai.dto.candidateResume.NewCandidateResumeDTO;
import com.spyatmycode.myjobhuntai.model.CandidateResume;
import com.spyatmycode.myjobhuntai.model.CandidateUserDetails;
import com.spyatmycode.myjobhuntai.service.AIGenerationService;
import com.spyatmycode.myjobhuntai.service.CandidateProfileService;
import com.spyatmycode.myjobhuntai.service.CandidateResumeService;
import com.spyatmycode.myjobhuntai.service.SupabaseStorageService;

@RestController
@RequestMapping("/api/candidate-resume")
public class CandidateResumeController {

        private final CandidateResumeService candidateResumeService;
        private final AIGenerationService aiGenerationService;
        private final SupabaseStorageService supabaseStorageService;

        public CandidateResumeController(
                        CandidateResumeService candidateResumeService,
                        AIGenerationService aiGenerationService,
                        CandidateProfileService candidateProfileService,
                        SupabaseStorageService supabaseStorageService

        ) {
                this.candidateResumeService = candidateResumeService;
                this.aiGenerationService = aiGenerationService;
                this.supabaseStorageService = supabaseStorageService;

        };

        @PostMapping(path = "/add-candidate-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        public ResponseEntity<ApiResponse<AIResumeSummaryResponse>> addCandidateResume(
                        @RequestParam(required = true) String title,
                        @RequestParam(required = false) String optionalUserPrompt,
                        @RequestParam(name = "resumeFile", required = true) MultipartFile resumePdf) {
                Long candidateId = ((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal()).getCandidateId();

                String resumeUrl = supabaseStorageService.uploadResume(resumePdf, candidateId);

                AIResumeSummaryResponse resumeSummaryResponse = aiGenerationService.generateResumeSummary(
                                resumePdf,
                                optionalUserPrompt,
                                candidateId);

                var addedCandidateResume = candidateResumeService.addCandidateResume(
                                new NewCandidateResumeDTO(
                                                title,
                                                resumeSummaryResponse.professionalSummary(),
                                                resumeSummaryResponse.skills(),
                                                candidateId,
                                                resumeUrl));

                AIResumeSummaryResponse finalSummaryResponse = new AIResumeSummaryResponse(
                                resumeSummaryResponse.professionalSummary(),
                                resumeSummaryResponse.skills(),
                                addedCandidateResume.getId(),
                                addedCandidateResume.getFilePath()

                );
                return ApiResponse.success(HttpStatus.CREATED, finalSummaryResponse, "Resume added successfully", null);
        };

        @PutMapping(path = "/update-candidate-resume/{resumeId}", consumes = {
                        MediaType.MULTIPART_FORM_DATA_VALUE })
        public ResponseEntity<ApiResponse<AIResumeSummaryResponse>> updateCandidateResume(
                        @RequestParam String title,
                        @RequestPart(required = false) String optionalUserPrompt,
                        @RequestParam(required = true) String resumeUrl,
                        @RequestPart(required = false, name = "resumeFile") MultipartFile resumeFile,
                        @PathVariable Long resumeId) {

                Long candidateId = ((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal()).getCandidateId();

                resumeUrl = resumeFile != null ? supabaseStorageService.uploadResume(resumeFile, candidateId)
                                : resumeUrl;

                AIResumeSummaryResponse resumeSummaryResponse = resumeFile != null
                                ? aiGenerationService.generateResumeSummary(
                                                resumeFile,
                                                optionalUserPrompt,
                                                candidateId)
                                : null;

                CandidateResume existingResume = candidateResumeService.findCandidateResumebyId(resumeId, candidateId);

                String resumeSummary = resumeFile != null ? resumeSummaryResponse.professionalSummary()
                                : existingResume.getResumeSummary();

                String skills = resumeFile != null ? resumeSummaryResponse.skills() : existingResume.getSkills();

                var updatedRequestBody = new NewCandidateResumeDTO(
                                title,
                                resumeSummary,
                                skills,
                                resumeId,
                                resumeUrl);

                var candidateResume = candidateResumeService.updateCandidateResume(updatedRequestBody, resumeId,
                                candidateId);

                return ApiResponse.success(
                                HttpStatus.OK,
                                new AIResumeSummaryResponse(
                                                resumeSummary,
                                                skills,
                                                candidateResume.getId(),
                                                candidateResume.getFilePath()),
                                "Resume updated successfully", null);
        }

        @GetMapping("/get-all-resumes")
        public ResponseEntity<ApiResponse<Iterable<CandidateResume>>> getResumesForAllCandidates() {
                return ApiResponse.success(HttpStatus.OK, candidateResumeService.getAllResumesFromAllCandidates(),
                                "Retrieved successfully", null);
        }

        @GetMapping("/get-resume/{id}")
        public ResponseEntity<ApiResponse<CandidateResume>> getCandidateResume(
                        @PathVariable Long id) {
                return ApiResponse.success(HttpStatus.OK, candidateResumeService.getCandidateResume(id),
                                "Retrieved successfully", null);
        };

        @GetMapping("/get-candidate-resumes")
        public ResponseEntity<ApiResponse<List<CandidateResume>>> getCandidateResumes() {
                Long candidateId = ((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal()).getCandidateId();
                return ApiResponse.success(HttpStatus.OK, candidateResumeService.getResumesForCandidate(candidateId),
                                "Retrieved successfully", null);
        };

        @DeleteMapping("/delete-resume/{id}")
        public ResponseEntity<ApiResponse<Void>> deleteCandidateResume(
                        @PathVariable Long id) {
                candidateResumeService.removeCandidateResume(id);
                return ApiResponse.success(HttpStatus.OK, null, "Deleted successfully", null);
        };

}
