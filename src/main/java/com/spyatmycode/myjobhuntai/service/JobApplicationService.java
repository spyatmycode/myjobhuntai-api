package com.spyatmycode.myjobhuntai.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spyatmycode.myjobhuntai.dto.jobApplication.NewJobApplicationDTO;
import com.spyatmycode.myjobhuntai.exception.ResourceNotFoundException;
import com.spyatmycode.myjobhuntai.model.JobApplication;
import com.spyatmycode.myjobhuntai.model.JobApplicationStatus;
import com.spyatmycode.myjobhuntai.repository.JobApplicationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CandidateProfileService candidateProfileService;

    public JobApplicationService(
        JobApplicationRepository jobApplicationRepository,
        CandidateProfileService candidateProfileService
    ){
        this.jobApplicationRepository = jobApplicationRepository;
        this.candidateProfileService = candidateProfileService;
    };

    //add job application
    //remove job application
    //update status
    //get application
    //get applications

    public JobApplication addJobApplication(
        NewJobApplicationDTO jobApplicationDTO
    ){

        log.info("Checking records for Candidate ID {}", jobApplicationDTO.candidateId());

        candidateProfileService.getCandidateProfileById(jobApplicationDTO.candidateId());

        log.info("Adding new job application for Candidate ID {}", jobApplicationDTO.candidateId());
        JobApplication newJobApplication = new JobApplication();

        newJobApplication.setCompanyName(jobApplicationDTO.companyName().strip());
        newJobApplication.setCandidateId(jobApplicationDTO.candidateId());
        newJobApplication.setJobDescription(jobApplicationDTO.jobDescription().strip());
        newJobApplication.setJobTitle(jobApplicationDTO.jobTitle().strip());
        newJobApplication.setStatus(jobApplicationDTO.status());

        jobApplicationRepository.save(newJobApplication);

        log.info("Done adding new job application for Candidate ID {}", jobApplicationDTO.candidateId());


        return newJobApplication;

    };


    public JobApplication findById(Long id){
        return jobApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Could not find job application with ID  " + id));
    }

    public JobApplication findByCandidateIdAndJobApplicationId(Long candidateId, Long jobApplicationId){
        return jobApplicationRepository.findByCandidateIdAndJobApplicationId(candidateId, jobApplicationId).orElseThrow(()-> new ResourceNotFoundException("Could not find job application with ID " + jobApplicationId + " and candidate ID " + candidateId));
    }

    public boolean removeJobApplication(Long id){
        log.info("Deleting job application with ID {}", id);
        jobApplicationRepository.deleteById(id);
        return true;
    }

    public JobApplication  updateJobApplicationStatus(Long id, JobApplicationStatus jobApplicationStatus){
        log.info("Updating status for job application ID {} to {}",id, jobApplicationStatus);
        jobApplicationRepository.updateJobApplicationStatus(id, jobApplicationStatus.name());
        log.info("Done updating status for job application ID {} to {}",id, jobApplicationStatus);
        return this.getSingleCandidateApplicationById(id);

    }

    public boolean updateAiCoverLetter(
        Long jobApplicationId,
        Long candidateId,
        String coverLetter
    ){
        jobApplicationRepository.updateAiCoverLetter(jobApplicationId, candidateId, coverLetter);
        return true;
    }

    public List<JobApplication> getAllCandidateApplications(Long id){
        return jobApplicationRepository.findByCandidateId(id);
    }

    public JobApplication getSingleCandidateApplicationById(Long id){
        return jobApplicationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Job application with ID: " + id + " not found"));
    }

    public Iterable<JobApplication> getAllJobApplications(){
        return jobApplicationRepository.findAll();
    }

    
}
