package com.studentportal.security.auth;

public class SignUpConfirmDetails {
    private String email;
    private String tempPassword;
    private String finalPassword;

    public SignUpConfirmDetails() {}

    public SignUpConfirmDetails(String email, String tempPassword, String finalPassword) {
        this.email = email;
        this.tempPassword = tempPassword;
        this.finalPassword = finalPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public String getFinalPassword() {
        return finalPassword;
    }

    public void setFinalPassword(String finalPassword) {
        this.finalPassword = finalPassword;
    }
}
