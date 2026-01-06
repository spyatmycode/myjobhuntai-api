package com.spyatmycode.myjobhuntai.controller;



import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;
import com.spyatmycode.myjobhuntai.dto.candidateProfile.NewCandidateProfileDTO;
import com.spyatmycode.myjobhuntai.model.CandidateProfile;
import com.spyatmycode.myjobhuntai.model.CandidateUserDetails;
import com.spyatmycode.myjobhuntai.service.CandidateProfileService;
import com.spyatmycode.myjobhuntai.service.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/candidate-profile")
@Slf4j
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;
    private final JwtService jwtService;

    public CandidateProfileController(
        CandidateProfileService candidateProfileService,
        JwtService jwtService
    ){
        this.candidateProfileService = candidateProfileService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create-candidate-profile")
    public ResponseEntity<ApiResponse<Void>> createCandidate(
        @RequestBody @Valid NewCandidateProfileDTO requestBody
    ){
        CandidateUserDetails userDetails = (CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        
        if(userDetails.getUsername().equals(requestBody.getEmail())==false){
            log.info("email={} request body email={}",userDetails.getUsername(),requestBody.getEmail());
            throw new BadCredentialsException("You cannot perform this request for this user.");
        }
        CandidateProfile newCandidateProfile = candidateProfileService.createCandidate(requestBody);

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("id", newCandidateProfile.getId());

        String newToken = jwtService.generateToken(userDetails,extraClaims);

        return ApiResponse.success(HttpStatus.CREATED, null, "Candidate profile created", newToken );
    };
// @PreAuthorize("hasRole('RECRUITER')")
    @GetMapping("/get-all-candidates")
    public ResponseEntity<ApiResponse<Iterable<CandidateProfile>>> getAllCandidates(){

        return ApiResponse.success(HttpStatus.OK, candidateProfileService.getAllCandidateProfiles(), "Profiles retrieved successfully", null);
    };

    @GetMapping("/get-candidate/{id}")
     public ResponseEntity<ApiResponse<CandidateProfile>> getCandidateProfile(
        @PathVariable Long id
    ){
         Long candidateId =((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCandidateId();

         if(id.equals(candidateId) == false){
            throw new BadCredentialsException("You cannot perform this request for this user.");
         }

        return ApiResponse.success(HttpStatus.OK, candidateProfileService.getCandidateProfileById(candidateId), "Profile retrieved successfully", null);
    };


    @PutMapping("/update-active/{id}")
    public ResponseEntity<ApiResponse<CandidateProfile>> updateCandidateActive(
        @PathVariable Long id
    ){

         Long candidateId =((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCandidateId();

         if(id.equals(candidateId) == false){
            throw new BadCredentialsException("You cannot perform this request for this user.");
         }
        
        return ApiResponse.success(HttpStatus.OK, candidateProfileService.updateCandidateActive(id), "Profiles activated successfully", null);
    };

    @DeleteMapping("/delete-candidate/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCandidateProfile(
        @PathVariable Long id
    ){

         Long candidateId =((CandidateUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getCandidateId();

         if(id.equals(candidateId) == false){
            throw new BadCredentialsException("You cannot perform this request for this user.");
         };

        candidateProfileService.deleteCandidateProfile(id);
        return ApiResponse.success(HttpStatus.OK, null , "Profile deleted successfully", null);
    };



    
}
