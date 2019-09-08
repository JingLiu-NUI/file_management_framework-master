package com.studentportal.security.auth;

import com.studentportal.user.UserRole;

public class SignUpDetails {

    private String email;
    private String givenName;
    private String familyName;
    private UserRole userRole;

    public SignUpDetails() {}

    public SignUpDetails(String email, String givenName, String familyName, UserRole userRole) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
