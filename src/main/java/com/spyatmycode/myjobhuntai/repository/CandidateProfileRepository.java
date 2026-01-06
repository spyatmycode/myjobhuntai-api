package com.spyatmycode.myjobhuntai.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spyatmycode.myjobhuntai.model.CandidateProfile;

@Repository
public interface CandidateProfileRepository extends CrudRepository<CandidateProfile, Long> {

    @Modifying
    @Query("UPDATE candidate_profiles SET is_active = true WHERE id = :id")
    public int updateCandidateProfileActive(Long id);

    public Optional<CandidateProfile> findByEmail(String email);
    
}
