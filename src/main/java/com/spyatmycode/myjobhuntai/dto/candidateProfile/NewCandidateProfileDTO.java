package com.spyatmycode.myjobhuntai.dto.candidateProfile;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public class NewCandidateProfileDTO {
    
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    
    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;
    
    @NotBlank(message = "Role cannot be blank")
    private String preferredRole;
    
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;
    
    @NotBlank(message = "Country phone code cannot be blank")
    private String countryPhoneCode;
    
    private LocalDate dateOfBirth;
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPreferredRole() {
        return preferredRole;
    }
    
    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getCountryPhoneCode() {
        return countryPhoneCode;
    }
    
    public void setCountryPhoneCode(String countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
