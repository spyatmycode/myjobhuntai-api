package com.spyatmycode.myjobhuntai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.spyatmycode.myjobhuntai.model.JobApplication;


@Repository
public interface JobApplicationRepository extends CrudRepository<JobApplication, Long>{

    @Query("SELECT * FROM job_applications WHERE candidate_id = :id")
    public List<JobApplication> findByCandidateId(Long id);


    @Modifying
    @Query("UPDATE job_applications SET status = :status WHERE id = :id")
    public int updateJobApplicationStatus(Long id, String status);

    @Modifying
    @Query("UPDATE job_applications SET ai_cover_letter = :aiCoverLetter WHERE id =:id AND candidate_id =:candidateId")
    public int updateAiCoverLetter(Long id,Long candidateId, String aiCoverLetter);

    @Query("SELECT * FROM job_applications WHERE id =:jobApplicationId AND candidate_id = :candidateId")
    public Optional<JobApplication> findByCandidateIdAndJobApplicationId(Long candidateId, Long jobApplicationId);
}
