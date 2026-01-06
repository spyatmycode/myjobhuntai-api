package com.spyatmycode.myjobhuntai.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spyatmycode.myjobhuntai.dto.candidateResume.NewCandidateResumeDTO;
import com.spyatmycode.myjobhuntai.exception.ResourceNotFoundException;
import com.spyatmycode.myjobhuntai.model.CandidateResume;
import com.spyatmycode.myjobhuntai.repository.CandidateResumeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CandidateResumeService {

    private final CandidateResumeRepository candidateResumeRepository;
    private final CandidateProfileService candidateProfileService;

    public CandidateResumeService(
            CandidateResumeRepository candidateResumeRepository,
            CandidateProfileService candidateProfileService) {
        this.candidateResumeRepository = candidateResumeRepository;
        this.candidateProfileService = candidateProfileService;
    };

    // Add resume
    // Delete resume
    // Get resume for candidate
    // Get resumes for candidate
    // Get all resumes

    public CandidateResume addCandidateResume(
            NewCandidateResumeDTO candidateResumeDTO) {
        log.info("Checking records for Candidate ID {}", candidateResumeDTO.candidateId());

        candidateProfileService.getCandidateProfileById(candidateResumeDTO.candidateId());

        log.info("Adding new resume for candidate with ID {}", candidateResumeDTO.candidateId());

        CandidateResume newCandidateResume = new CandidateResume();

        newCandidateResume.setCandidateId(candidateResumeDTO.candidateId());
        newCandidateResume.setResumeSummary(candidateResumeDTO.resumeSummary().strip());
        newCandidateResume.setSkills(candidateResumeDTO.skills().strip());
        newCandidateResume.setTitle(candidateResumeDTO.title().strip());
        newCandidateResume.setFilePath(candidateResumeDTO.resumeUrl());

        candidateResumeRepository.save(newCandidateResume);

        log.info("Done adding new resume for candidate with ID {}", candidateResumeDTO.candidateId());

        return newCandidateResume;
    };

    public CandidateResume findById(Long id) {
        return candidateResumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find resume with ID " + id));
    };

    public CandidateResume findCandidateResumebyId(
            Long resumeId,
            Long candidateId) {
        return candidateResumeRepository.findByCandidateIdAndResumeId(candidateId, resumeId).orElseThrow(() -> new ResourceNotFoundException("Could not find resume with ID " + resumeId+ " and candidateId " + candidateId));
    }

    public CandidateResume updateCandidateResume(
            NewCandidateResumeDTO updatedResumeDTO,
            Long resumeId,
            Long candidateId
        ) {
        var candidateResume = candidateResumeRepository.findByCandidateIdAndResumeId(candidateId, resumeId).orElseThrow(() -> new ResourceNotFoundException("Could not find resume with ID " + resumeId+ " and candidateId " + candidateId));
    
               

        candidateResume.setResumeSummary(updatedResumeDTO.resumeSummary());
        candidateResume.setSkills(updatedResumeDTO.skills());
        candidateResume.setTitle(updatedResumeDTO.title());
        candidateResume.setFilePath(updatedResumeDTO.resumeUrl());

        candidateResumeRepository.save(candidateResume);
        
        return this.getCandidateResume(resumeId);
    }

    public boolean removeCandidateResume(Long id) {
        log.info("Deleting resume for candidate with ID {}", id);
        candidateResumeRepository.deleteById(id);
        return true;
    };

    public Iterable<CandidateResume> getAllResumesFromAllCandidates() {
        return candidateResumeRepository.findAll();
    };

    public List<CandidateResume> getResumesForCandidate(Long candidateId) {
        return candidateResumeRepository.findByCandidateId(candidateId);
    };

    public CandidateResume getCandidateResume(Long resumeId) {
        return candidateResumeRepository.findById(resumeId).orElseThrow(
                () -> new ResourceNotFoundException("Candidate Resume with ID " + resumeId + " could not be found"));
    }

}
