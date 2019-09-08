package com.studentportal.core;

import com.studentportal.announcement.Announcement;
import com.studentportal.api.AnnouncementApi;
import com.studentportal.api.AuthApi;
import com.studentportal.reminders.ReminderTypes.AssignmentReminder;
import com.studentportal.security.auth.AuthHelper;
import com.studentportal.security.auth.SignInCredentials;
import com.studentportal.security.auth.SignUpDetails;
import com.studentportal.security.aws.CognitoUser;
import com.studentportal.ui.FileManagementUi;
import com.studentportal.ui.LoginUI;
import com.studentportal.user.UserRole;

public class Client {

    public static void main(String[] args) {
        login();
//        signUpAdmin();
//        loginTest();
    }

    private static void login() {
        LoginUI ui = new LoginUI();
        ui.show();
    }

    private static void loginTest() {
        String email = "12170321@studentmail.ul.ie";
        String password = "leomessi";
        SignInCredentials credentials = new SignInCredentials(email, password);
        String json = AuthHelper.convertSignInCredentialsToJson(credentials);
        AuthApi api = new AuthApi();
        api.signIn(json);
    }

    private static void signUpAdmin() {
        String email = "12170321@studentmail.ul.ie";
        String given_name = "David";
        String family_name = "Nkanga";
        UserRole userRole = UserRole.ADMIN;

        SignUpDetails dets = new SignUpDetails(email, given_name, family_name, userRole);
        String json = AuthHelper.convertSignUpDetailsToJson(dets);
        AuthApi api = new AuthApi();
        api.signUp(json);
    }
}