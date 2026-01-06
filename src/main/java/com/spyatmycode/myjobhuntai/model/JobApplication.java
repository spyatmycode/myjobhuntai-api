package com.spyatmycode.myjobhuntai.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("job_applications")
public class JobApplication {

    @Id
    private Long id;

    @Column("company_name")
    private String companyName;

    @Column("job_title")
    private String jobTitle;

    @Column("job_description")
    private String jobDescription;

    @Column("ai_cover_letter")
    private String aiCoverLetter;

    private String status;

    @Column("candidate_id")
    private Long candidateId;

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public JobApplicationStatus getStatus() {
        return JobApplicationStatus.valueOf(status);
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status.name();
    }

    public String getAiCoverLetter() {
        return aiCoverLetter;
    }

    public void setAiCoverLetter(String aiCoverLetter) {
        this.aiCoverLetter = aiCoverLetter;
    }
   
    
}

