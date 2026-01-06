package com.spyatmycode.myjobhuntai.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import com.spyatmycode.myjobhuntai.model.CandidateProfile;
import com.spyatmycode.myjobhuntai.model.CandidateUserDetails;
import com.spyatmycode.myjobhuntai.repository.CandidateProfileRepository;

@Service
public class CandidateUserDetailsService implements UserDetailsService{

    private final JdbcUserDetailsManager jdbcUserDetailsManager;
    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateUserDetailsService(
        JdbcUserDetailsManager jdbcUserDetailsManager,
        CandidateProfileRepository candidateProfileRepository
    ){
        this.candidateProfileRepository = candidateProfileRepository;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    };

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        UserDetails standardUser = jdbcUserDetailsManager.loadUserByUsername(email);

        CandidateProfile candidateProfile = candidateProfileRepository.findByEmail(email).orElse(null);



        return new CandidateUserDetails(
                standardUser.getUsername(),
                standardUser.getPassword(),
                standardUser.isEnabled(),
                standardUser.getAuthorities(),
                candidateProfile
        );
    }

    
}
