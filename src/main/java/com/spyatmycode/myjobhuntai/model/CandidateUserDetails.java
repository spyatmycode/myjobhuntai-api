package com.spyatmycode.myjobhuntai.model;

import java.util.Collection;


import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

public class CandidateUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final boolean enabled;
    private final CandidateProfile candidateProfile;
    private final Collection<? extends GrantedAuthority> authorities;

    public CandidateUserDetails(
            String username,
            String password,
            boolean enabled,
            Collection<? extends GrantedAuthority> authorities,
            CandidateProfile candidateProfile
            
        ) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.candidateProfile = candidateProfile;
        this.authorities = authorities;
    };

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.authorities;
    }

    public boolean hasProfile(){
        return this.getProfile() != null;
    }

    public CandidateProfile getProfile() {
        return this.candidateProfile;
    }

    public Long getCandidateId() {
        return this.hasProfile() ?  this.candidateProfile.getId(): null;
    }

    @Override
    public String getPassword() {

        return this.password;
    }

    @Override
    public String getUsername() {

        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}