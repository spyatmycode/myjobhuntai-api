package com.spyatmycode.myjobhuntai.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spyatmycode.myjobhuntai.exception.UserAlreadyExistsException;


@Service
@Transactional
public class AuthService {

    private final JdbcUserDetailsManager usersTableManager;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            JdbcUserDetailsManager usersTableManager,
            PasswordEncoder passwordEncoder) {
        this.usersTableManager = usersTableManager;
        this.passwordEncoder = passwordEncoder;
    };

    public UserDetails registerUser(
            String username,
            String rawPassword) {
        if (usersTableManager.userExists(username)) {
            throw new UserAlreadyExistsException("This user with username/email " + username + " already exists.");
        };


        UserDetails userDetails = User.builder().username(username).password(passwordEncoder.encode(rawPassword)).roles("CANDIDATE").build();

        usersTableManager.createUser(userDetails);

        return userDetails;
    }

    
}
