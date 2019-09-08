package com.studentportal.api;

import com.studentportal.commands.*;
import com.studentportal.hibernate.UserService;
import com.studentportal.security.auth.*;
import com.studentportal.user.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthApi {
    private UserService uService = new UserService();

    @POST
    @Path("/signIn")
    public User signIn(String json) {
        SignInCredentials credentials = AuthHelper.extractSignInCredentialsFromJson(json);
        SignInCommand cmd = new SignInCommand(credentials, uService);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (User) apiControl.doWork();
    }

    @POST
    @Path("/signUp")
    public void signUp(String json) {
        SignUpDetails details = AuthHelper.extractSignUpDetailsFromJson(json);
        SignUpCommand cmd = new SignUpCommand(details);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @POST
    @Path("/signUp/confirm")
    public void signUpConfirm(String json) {
        SignUpConfirmDetails confirmDetails = AuthHelper.extractSignUpConfirmDetailsFromJson(json);
        SignUpConfirmCommand cmd = new SignUpConfirmCommand(confirmDetails, uService);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @GET
    @Path("/forgotPassword/{email}")
    public void forgotPassword(@PathParam("email") String email) {
        ForgotPasswordCommand cmd = new ForgotPasswordCommand(email);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @POST
    @Path("/forgotPassword/confirm")
    public void forgotPasswordConfirm(String json) {
        ForgotPasswordConfirmDetails confirmDetails = AuthHelper
                .extractPasswordConfirmDetailsFromJson(json);
        ForgotPasswordConfirmCommand cmd = new ForgotPasswordConfirmCommand(confirmDetails);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }
}
