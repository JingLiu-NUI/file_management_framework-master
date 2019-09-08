package com.studentportal.commands;

import com.studentportal.hibernate.UserService;
import com.studentportal.security.auth.SignUpDetails;
import com.studentportal.security.aws.AwsCognitoResult;
import com.studentportal.security.aws.ResultReasons;
import com.studentportal.security.aws.SignUp;
import com.studentportal.user.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SignUpCommand implements Command<Void> {
    private SignUpDetails details;

    public SignUpCommand(SignUpDetails details) {
        this.details = details;
    }

    @Override
    public Void execute() {
        String email = details.getEmail().trim();
        String givenName = details.getGivenName();
        String familyName = details.getFamilyName();
        UserRole userRole = details.getUserRole();
        AwsCognitoResult result = SignUp.doWork(email, givenName, familyName,
                userRole);
        if (result.isSuccessful()) {
            System.out.println("User signed up to aws");
        } else {
            if (result.getReason().equals(ResultReasons.EMAIL_EXISTS)) {
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT)
                        .entity("A user with that email already exists").build());
            } else if (result.getReason().equals(ResultReasons.INVALID_PARAMS)) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid parameters were used. Check your email.").build());
            } else if (result.getReason().equals(ResultReasons.TOO_MANY_REQUESTS)) {
                throw new WebApplicationException(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Too many requests were sent. Please try again later").build());
            }
        }
        return null;
    }
}
