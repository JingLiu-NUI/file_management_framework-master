package com.studentportal.ui;

import com.studentportal.http.RequestHandler;
import com.studentportal.http.auth.ResetPasswordConfirmRequest;
import com.studentportal.http.auth.RestPasswordRequest;
import com.studentportal.http.auth.SignInRequest;
import com.studentportal.http.auth.SignUpConfirmRequest;
import com.studentportal.security.auth.AuthHelper;
import com.studentportal.security.auth.ForgotPasswordConfirmDetails;
import com.studentportal.security.auth.SignInCredentials;
import com.studentportal.security.auth.SignUpConfirmDetails;
import com.studentportal.user.Student;
import com.studentportal.user.User;
import com.studentportal.user.UserRole;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User interface for logging in a user
 */
public class LoginUI extends Ui {
    private JLabel emailLabel;
    private JLabel passwordLabel;

    private JTextField emailField;
    private JPasswordField passwordField;

    private JButton loginButton;
    private JButton forgotPasswordBtn;

    private RequestHandler signInHandler;
    private RequestHandler signUpConfirmHandler;
    private RequestHandler forgotPasswordHandler;
    private RequestHandler forgotPasswordConfirmHandler;

    private String emailStr;
    private String passwordStr;

    public LoginUI() {
        initHandlers();
        prepareGUI();
    }

    private void initHandlers() {
        this.signUpConfirmHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),"Account confirmed");
                passwordField.setText("");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };

        this.signInHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(), "Success");
            }

            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().contains("Found") && !e.getMessage().contains("Not Found")) {
                    System.out.println(e.getMessage());
                    JOptionPane.showMessageDialog(getFrame(),
                            "Temporary password used, enter a new password");
                    String finalPassword = JOptionPane.showInputDialog(getFrame(),
                            "Enter a new password(min 6 characters)");
                    if (!StringUtils.isBlank(finalPassword)) {
                        SignUpConfirmDetails details = new SignUpConfirmDetails(emailStr,
                                passwordStr, finalPassword);
                        String json = AuthHelper.convertSignUpConfirmDetailsToJson(details);
                        SignUpConfirmRequest request = new SignUpConfirmRequest();
                        request.makeRequest(signUpConfirmHandler, json);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                }
            }
        };


        this.forgotPasswordHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(), "Verificaton code sent to email");
                System.out.println("code sent");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };

        this.forgotPasswordConfirmHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(), "Password changed");
                System.out.println("password changed");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };
    }

    private void prepareGUI() {
        getFrame().setTitle("Login");
        getFrame().setSize(400, 200);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout(3, 2));

        emailLabel = new JLabel("Email: ");
        emailField = new JTextField();

        passwordLabel = new JLabel("Password: ");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!validateInput()) {
                    JOptionPane.showMessageDialog(getFrame(), "Please enter information in all fields", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    String email = new String(emailField.getText());
                    String password = new String(passwordField.getPassword());
                    emailStr = email;
                    passwordStr = password;
                    String json = AuthHelper.convertSignInCredentialsToJson(
                            new SignInCredentials(email, password));
                    SignInRequest request = new SignInRequest();

                    User user = request.makeRequest(signInHandler, json);
                    if (user != null) {
                        System.out.println(user.toString());
                        if (user.getUserRole().equals(UserRole.ADMIN)) {
                            AdminHomePageUi home = new AdminHomePageUi(user);
                            home.show();
                            close();
                        } else if (user.getUserRole().equals(UserRole.TEACHER)) {
                            TeacherHomePageUI home = new TeacherHomePageUI(user);
                            home.show();
                            close();
                        } else if (user.getUserRole().equals(UserRole.STUDENT)) {
                            StudentHomePageUI home = new StudentHomePageUI((Student) user);
                            home.show();
                            close();
                        }
                    } else {
                        System.out.println("User is Null");
                    }
                }
            }
        });

        forgotPasswordBtn = new JButton("Forgot Password");
        forgotPasswordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showOptionDialog(null,
                        "",
                        "Choose an option",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Send Verification Code", "Enter New Password"},
                        null);

                if (choice == 0) {
                    //  send code
                    String email = JOptionPane.showInputDialog(getFrame(), "Enter your email");
                    if (!StringUtils.isBlank(email)) {
                        RestPasswordRequest request = new RestPasswordRequest();
                        request.makeRequest(forgotPasswordHandler, email);
                    } else {
                        JOptionPane.showMessageDialog(getFrame(),
                                "Email cannot be empty");
                    }
                } else {
                    // enter new password
                    String email = JOptionPane.showInputDialog(getFrame(),
                            "Enter your email");
                    String newPassword = JOptionPane.showInputDialog(getFrame(),
                            "Enter new password");
                    String verfCode = JOptionPane.showInputDialog(getFrame(),
                            "Enter verification code");

                    if (!StringUtils.isBlank(email) || !StringUtils.isBlank(newPassword) ||
                            !StringUtils.isBlank(verfCode)) {

                        ForgotPasswordConfirmDetails details =
                                new ForgotPasswordConfirmDetails(email, newPassword, verfCode);

                        String json = AuthHelper.convertPasswordConfirmDetailsToJson(details);
                        ResetPasswordConfirmRequest request = new ResetPasswordConfirmRequest();
                        request.makeRequest(forgotPasswordConfirmHandler, json);
                    } else {
                        JOptionPane.showMessageDialog(getFrame(),
                                "New password or verification code cannot be empty");
                    }
                }
            }
        });

        getFrame().add(emailLabel);
        getFrame().add(emailField);

        getFrame().add(passwordLabel);
        getFrame().add(passwordField);

        getFrame().add(forgotPasswordBtn);
        getFrame().add(loginButton);
    }

    private boolean validateInput() {
        char[] password = passwordField.getPassword();
        if (emailField.getText().isEmpty() || password.length == 0) {
            return false;
        }
        return true;
    }
}