package com.studentportal.api;

import com.studentportal.hibernate.UserService;
import com.studentportal.security.auth.AuthHelper;
import com.studentportal.security.auth.SignInCredentials;
import com.studentportal.user.Admin;
import com.studentportal.user.User;
import com.studentportal.user.UserRole;
import junit.framework.TestCase;
import org.junit.Test;

public class AuthApiTest extends TestCase {

    private AuthApi api = new AuthApi();
    private UserService service = new UserService();

    // only testing this method because its one because its a lot of work testing the other ones
    // since we would have to access the email programmatically so we can get the temp password
    @Test
    public void testSignIn() throws Exception {
        String email = "testaccntemail@gmail.com";
        String password = "password";

        Admin admin = new Admin(0, email, "", "", UserRole.ADMIN);
        service.save(admin);

        SignInCredentials credentials = new SignInCredentials(email, password);
        String json = AuthHelper.convertSignInCredentialsToJson(credentials);

        User user = null;
        user = api.signIn(json);

        assertFalse(user == null);
    }
}