package com.studentportal.commands;

import com.studentportal.security.aws.AwsCognitoResult;
import com.studentportal.security.aws.ForgotPassword;
import com.studentportal.security.aws.ResultReasons;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ForgotPasswordCommand implements Command<Void> {
    private String email;

    public ForgotPasswordCommand(String email) {
        this.email = email;
    }

    @Override
    public Void execute() {
        AwsCognitoResult result = ForgotPassword.doWork(email);
        if (result.isSuccessful()) {
            System.out.println("Verification code sent to email");
        } else {
            if (result.getReason().equals(ResultReasons.CODE_DELIVERY_FAILED)) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Failed to send verification code to email").build());
            } else if (result.getReason().equals(ResultReasons.USER_NOT_CONFIRMED)) {
                throw new WebApplicationException(Response.status(Response.Status.PRECONDITION_REQUIRED)
                        .entity("That user account has not been confirmed").build());
            } else if (result.getReason().equals(ResultReasons.NOT_AUTHORIZED)) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("An incorrect email or password was used").build());
            } else if (result.getReason().equals(ResultReasons.INVALID_PARAMS)) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid parameters were used. Check your email.").build());
            } else if (result.getReason().equals(ResultReasons.NO_SUCH_USER)) {
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                        .entity("A user with email does not exist").build());
            } else if (result.getReason().equals(ResultReasons.TOO_MANY_REQUESTS)) {
                throw new WebApplicationException(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Too many requests were sent. Please try again later").build());
            }
        }
        return null;
    }
}
