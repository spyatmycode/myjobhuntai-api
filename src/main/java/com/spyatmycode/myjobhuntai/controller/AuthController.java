package com.spyatmycode.myjobhuntai.controller;

import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;
import com.spyatmycode.myjobhuntai.dto.auth.SignupRequestDTO;
import com.spyatmycode.myjobhuntai.model.CandidateUserDetails;
import com.spyatmycode.myjobhuntai.service.AuthService;
import com.spyatmycode.myjobhuntai.service.CandidateProfileService;
import com.spyatmycode.myjobhuntai.service.EmailService;
import com.spyatmycode.myjobhuntai.service.JwtAuthenticationFilter;
import com.spyatmycode.myjobhuntai.service.JwtService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Autowired
    private EmailService emailService; // Inject


    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            AuthenticationProvider authenticationProvider,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            PasswordEncoder passwordEncoder,
            CandidateProfileService candidateProfileService,
            JwtService jwtService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid SignupRequestDTO requestBody) {

        log.info("Signing up email={}", requestBody.email(), requestBody.password());
        UserDetails registeredUser = authService.registerUser(requestBody.email(), requestBody.password());

        Map<String, Object> extraClaims = new HashMap<>();

        String token = jwtService.generateToken(registeredUser, extraClaims);

        log.info("Signing up email={} done token={}", requestBody.email(), requestBody.password(), token);

        emailService.sendLoginNotification(requestBody.email());

        return ApiResponse.success(HttpStatus.CREATED, null, "Account created successful", token);

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Void>> login(
            @RequestBody @Valid SignupRequestDTO requestBody) {

        log.info("Logging email={}", requestBody.email(), requestBody.password());

        var authenticationToken = new UsernamePasswordAuthenticationToken(requestBody.email(), requestBody.password());

        Authentication auth = authenticationManager.authenticate(authenticationToken);

        CandidateUserDetails userDetails = (CandidateUserDetails) auth.getPrincipal();

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("id", userDetails.getCandidateId());

        String token = jwtService.generateToken(userDetails, extraClaims);

        log.info("Logging email={} done token={}", requestBody.email(), requestBody.password(), token);

        emailService.sendLoginNotification(requestBody.email());

        return ApiResponse.success(HttpStatus.OK, null, "Account login successful", token);
    }
}