package com.spyatmycode.myjobhuntai.service;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spyatmycode.myjobhuntai.dto.apiResponse.ApiResponse;

import java.io.IOException;

@Component // 1. Manage this class as a Spring Bean
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CandidateUserDetailsService candidateUserDetailsService;

    // Dependency Injection
    public JwtAuthenticationFilter(JwtService jwtService, @Lazy CandidateUserDetailsService candidateUserDetailsService) {
        this.jwtService = jwtService;
        this.candidateUserDetailsService = candidateUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // A. GET THE HEADER
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // B. CHECK IF HEADER IS MISSING OR WRONG FORMAT
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // If no token, pass the request to the next filter.
            // (The request is still "Anonymous" at this point)
            filterChain.doFilter(request, response);
            return;
        }

        // C. EXTRACT TOKEN AND EMAIL
        jwt = authHeader.substring(7); // Remove "Bearer " prefix
        userEmail = jwtService.extractUsername(jwt);

        // D. CHECK IF USER IS NOT AUTHENTICATED YET
        // SecurityContextHolder.getContext().getAuthentication() == null 
        // means: "Is this user currently unknown to Spring Security?"
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
           try {
             // E. LOAD USER FROM DB
            // We fetch the full user details to check things like isEnabled, isLocked, etc.
            UserDetails candidateUserDetails = this.candidateUserDetailsService.loadUserByUsername(userEmail);

            // F. VALIDATE TOKEN
            if (jwtService.isTokenValid(jwt,candidateUserDetails)) {
                
                // G. CREATE AUTH TOKEN
                // This object is what Spring Security needs to know "Who" is logged in
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                       candidateUserDetails,
                        null, // credentials (we don't keep the password here for safety)
                       candidateUserDetails.getAuthorities()
                );
                
                // Add details like IP address, Session ID (not used here but standard practice)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // H. UPDATE SECURITY CONTEXT
                // This is the FINAL STAMP. After this line, the user is "Logged In".
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
           }
           catch(UsernameNotFoundException e){
            log.info("An error occured error={}", e.getMessage());
            ApiResponse.error(HttpStatus.NOT_FOUND, null, "Username not found", null);
           }
           catch (Exception e) {
            log.info("An error occured error={}", e.getMessage());
            ApiResponse.error(HttpStatus.NOT_FOUND, null, "An error occured trying to log you in", null);
           }
        }
        
        // I. PASS TO NEXT FILTER
        filterChain.doFilter(request, response);
    }
} 
