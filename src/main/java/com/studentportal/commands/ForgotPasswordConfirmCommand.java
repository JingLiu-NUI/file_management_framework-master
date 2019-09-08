package com.studentportal.commands;

import com.studentportal.security.auth.ForgotPasswordConfirmDetails;
import com.studentportal.security.aws.AwsCognitoResult;
import com.studentportal.security.aws.ForgotPasswordConfirm;
import com.studentportal.security.aws.ResultReasons;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class ForgotPasswordConfirmCommand implements Command<Void> {

    private ForgotPasswordConfirmDetails confirmDetails;

    public ForgotPasswordConfirmCommand(ForgotPasswordConfirmDetails confirmDetails) {
        this.confirmDetails = confirmDetails;
    }

    @Override
    public Void execute() {
        String email = confirmDetails.getEmail();
        String password = confirmDetails.getPassword();
        String verificationCode = confirmDetails.getVerificationCode();
        AwsCognitoResult result = ForgotPasswordConfirm.doWork(email, password,
                verificationCode);

        if (result.isSuccessful()) {
            System.out.println("Password has been reset");
        } else {
            if (result.getReason().equals(ResultReasons.VERIFICATION_CODE_MISMATCH)) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid verification code").build());
            } else if (result.getReason().equals(ResultReasons.USER_NOT_CONFIRMED)) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("That verification code has expired").build());
            } else if (result.getReason().equals(ResultReasons.NOT_AUTHORIZED)) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("An incorrect email or password was used").build());
            } else if (result.getReason().equals(ResultReasons.INVALID_PASSWORD)) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("An incorrect password was used").build());
            } else if (result.getReason().equals(ResultReasons.INVALID_PARAMS)) {
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid parameters were used. Check your email.").build());
            } else if (result.getReason().equals(ResultReasons.NO_SUCH_USER)) {
                throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND)
                        .entity("A user with email does not exist").build());
            } else if (result.getReason().equals(ResultReasons.TOO_MANY_REQUESTS)) {
                throw new WebApplicationException(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Too many requests were sent. Please try again later").build());
            } else if (result.getReason().equals(ResultReasons.USER_NOT_CONFIRMED)) {
                throw new WebApplicationException(Response.status(Response.Status.PRECONDITION_REQUIRED)
                        .entity("That user account has not been confirmed").build());
            } else if (result.getReason().equals(ResultReasons.TOO_MANY_ATTEMPTS)) {
                throw new WebApplicationException(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("There has been too many attempts to reset the password").build());
            }
        }
        return null;
    }
}
