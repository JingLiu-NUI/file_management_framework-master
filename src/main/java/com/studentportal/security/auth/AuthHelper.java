package com.studentportal.security.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AuthHelper {

    public static SignInCredentials extractSignInCredentialsFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SignInCredentials credentials = null;
        try {
            credentials = mapper.readValue(json, SignInCredentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    public static String convertSignInCredentialsToJson(SignInCredentials credentials) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(credentials);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static SignUpDetails extractSignUpDetailsFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SignUpDetails details = null;
        try {
            details = mapper.readValue(json, SignUpDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static String convertSignUpDetailsToJson(SignUpDetails details) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(details);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static SignUpConfirmDetails extractSignUpConfirmDetailsFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        SignUpConfirmDetails confirmDetails = null;
        try {
            confirmDetails = mapper.readValue(json, SignUpConfirmDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return confirmDetails;
    }

    public static String convertSignUpConfirmDetailsToJson(SignUpConfirmDetails confirmDetails) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(confirmDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static ForgotPasswordConfirmDetails extractPasswordConfirmDetailsFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        ForgotPasswordConfirmDetails confirmDetails = null;
        try {
            confirmDetails = mapper.readValue(json, ForgotPasswordConfirmDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return confirmDetails;
    }

    public static String convertPasswordConfirmDetailsToJson(ForgotPasswordConfirmDetails confirmDetails) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(confirmDetails);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
