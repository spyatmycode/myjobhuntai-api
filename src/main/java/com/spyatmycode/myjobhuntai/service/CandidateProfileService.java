package com.spyatmycode.myjobhuntai.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spyatmycode.myjobhuntai.dto.candidateProfile.NewCandidateProfileDTO;
import com.spyatmycode.myjobhuntai.exception.ResourceNotFoundException;
import com.spyatmycode.myjobhuntai.model.CandidateProfile;
import com.spyatmycode.myjobhuntai.repository.CandidateProfileRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CandidateProfileService {
    
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository){
        this.candidateProfileRepository = candidateProfileRepository;
    };

    /* 
        Create candidate
        Update Candidate Active
        Get Candidate
        Get Candidates

    */


    public CandidateProfile createCandidate(
        NewCandidateProfileDTO candidateProfileDTO
    ){

        log.info("Creating new candidate profile with email {}", candidateProfileDTO.getEmail());

        CandidateProfile newCandidate = new CandidateProfile();
        newCandidate.setEmail(candidateProfileDTO.getEmail().toLowerCase().strip());
        newCandidate.setFirstName(candidateProfileDTO.getFirstName().strip());
        newCandidate.setLastName(candidateProfileDTO.getLastName().strip());
        newCandidate.setPreferredRole(candidateProfileDTO.getPreferredRole().strip());

        newCandidate.setCountryPhoneCode(candidateProfileDTO.getCountryPhoneCode().strip());
        newCandidate.setPhoneNumber(candidateProfileDTO.getPhoneNumber().strip());
        newCandidate.setDateOfBirth(candidateProfileDTO.getDateOfBirth());

        var result = candidateProfileRepository.save(newCandidate);

        log.info("Created new candidate profile email {}", candidateProfileDTO.getEmail());

        return result;

    };


    public CandidateProfile updateCandidateActive(Long id){
         candidateProfileRepository.updateCandidateProfileActive(id);
         return candidateProfileRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Could not update candidate profile with ID " + id)
         );
    };

    public CandidateProfile findById (Long id){
        return candidateProfileRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Could not find candidate with ID " + id));
    }

    public CandidateProfile getCandidateProfileById(Long id){
        return candidateProfileRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Candidate profile with ID "+ id + " not found"));
    };

    public Iterable<CandidateProfile> getAllCandidateProfiles(){
        return candidateProfileRepository.findAll();
    };

    public boolean deleteCandidateProfile(Long id){
        candidateProfileRepository.deleteById(id);
        return true;
    }


}
