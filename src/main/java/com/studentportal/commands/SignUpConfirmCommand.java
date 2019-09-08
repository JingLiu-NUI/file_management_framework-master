package com.studentportal.commands;

import com.studentportal.hibernate.UserService;
import com.studentportal.security.auth.SignUpConfirmDetails;
import com.studentportal.security.aws.AwsCognitoResult;
import com.studentportal.security.aws.CognitoUser;
import com.studentportal.security.aws.ResultReasons;
import com.studentportal.security.aws.SignUpConfirm;
import com.studentportal.user.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SignUpConfirmCommand implements Command<Void> {
    private SignUpConfirmDetails confirmDetails;
    private UserService userService;

    public SignUpConfirmCommand(SignUpConfirmDetails confirmDetails, UserService userService) {
        this.confirmDetails = confirmDetails;
        this.userService = userService;
    }

    @Override
    public Void execute() {
        String email = confirmDetails.getEmail().trim();
        String tempPassword = confirmDetails.getTempPassword();
        String finalPassword = confirmDetails.getFinalPassword();
        AwsCognitoResult result = SignUpConfirm.doWork(email, tempPassword, finalPassword);
        if (result.isSuccessful()) {
            User user = CognitoUser.getUser(email);
            if (user != null) {
                userService.save(user);
                System.out.println("Account successfully confirmed");
            }
        } else {
            if (result.getReason().equals(ResultReasons.INVALID_PASSWORD)) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("An incorrect password was used").build());
            } else if (result.getReason().equals(ResultReasons.NOT_AUTHORIZED)) {
                throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("An incorrect email or password was used").build());
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
