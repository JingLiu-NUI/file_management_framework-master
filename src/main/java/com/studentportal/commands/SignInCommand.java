package com.studentportal.commands;

import com.studentportal.hibernate.UserService;
import com.studentportal.security.auth.SignInCredentials;
import com.studentportal.security.aws.AwsCognitoResult;
import com.studentportal.security.aws.ResultReasons;
import com.studentportal.security.aws.SignIn;
import com.studentportal.user.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SignInCommand implements Command<User> {
    private SignInCredentials credentials;
    private UserService uService;

    public SignInCommand(SignInCredentials credentials, UserService uService) {
        this.credentials = credentials;
        this.uService = uService;
    }

    @Override
    public User execute() {
        AwsCognitoResult result = SignIn.doWork(credentials.getEmail().trim(),
                credentials.getPassword());
        User user = null;
        if (result.isSuccessful()) {
            System.out.println("Successful login attempt");
            user = uService.findByEmail(credentials.getEmail());
            return user;
        } else {
            if (result.getReason().equals(ResultReasons.TEMP_PASSWORD_USED)) {
                throw new WebApplicationException(Response.status(Response.Status.FOUND)
                        .entity("A temporary password was used. Enter a new password").build());
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
        return user;
    }
}
