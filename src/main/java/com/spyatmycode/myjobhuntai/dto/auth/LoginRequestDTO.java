package com.spyatmycode.myjobhuntai.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Please enter a valid email") String email,
    @NotEmpty @Min(value=8, message = "Password must be at least characters long") 
    String password
){};