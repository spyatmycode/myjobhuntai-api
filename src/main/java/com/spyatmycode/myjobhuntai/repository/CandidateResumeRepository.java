package com.spyatmycode.myjobhuntai.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spyatmycode.myjobhuntai.model.CandidateResume;

@Repository
public interface CandidateResumeRepository extends CrudRepository<CandidateResume, Long>{
    
    @Query("SELECT * FROM candidate_resumes WHERE candidate_id =:id")
    public List<CandidateResume> findByCandidateId(Long id);

    @Modifying
    @Query("UPDATE candidate_resumes set resume_summary = :resumeSummary, skills = :skills, title = :title WHERE id =:id")
    public int updateCandidateResume(Long id, String resumeSummary, String skills, String title);

    @Query("SELECT * FROM candidate_resumes WHERE id =:resumeId AND  candidate_Id = :candidateId")
    public Optional<CandidateResume> findByCandidateIdAndResumeId(Long candidateId, Long resumeId);
}
