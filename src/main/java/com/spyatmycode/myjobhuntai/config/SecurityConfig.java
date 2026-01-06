package com.spyatmycode.myjobhuntai.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // IMPORT THIS
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // IMPORT THIS

import com.spyatmycode.myjobhuntai.service.CandidateUserDetailsService;
import com.spyatmycode.myjobhuntai.service.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CandidateUserDetailsService candidateUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // 1. INJECT THE FILTER

    public SecurityConfig(
            @Lazy CandidateUserDetailsService candidateUserDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) { // 2. ADD TO CONSTRUCTOR
        this.candidateUserDetailsService = candidateUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // SWITCH 1: DISABLE CSRF
            // We don't use cookies, so we don't need Cross-Site Request Forgery protection.
            .csrf(csrf -> csrf.disable())

            // SWITCH 2: THE AMNESIA SWITCH (STATELESS)
            // "Don't store session state. Don't create a JSESSIONID cookie."
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // URL PERMISSIONS (You already had this)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
            )

            // SETUP USER DETAILS SERVICE (You already had this)
            .authenticationProvider(authenticationProvider())

            // SWITCH 3: THE BOUNCER SWITCH (FILTER ORDER)
            // "Run my JWT Filter BEFORE the standard Username/Password Filter."
            // If we don't do this, Spring expects a password and fails before checking our token.
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(candidateUserDetailsService); 
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}