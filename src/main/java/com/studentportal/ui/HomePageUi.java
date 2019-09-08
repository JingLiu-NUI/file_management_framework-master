package com.studentportal.ui;

import com.studentportal.http.RequestHandler;
import com.studentportal.http.auth.ResetPasswordConfirmRequest;
import com.studentportal.http.auth.RestPasswordRequest;
import com.studentportal.security.auth.AuthHelper;
import com.studentportal.security.auth.ForgotPasswordConfirmDetails;
import com.studentportal.ui.LoginUI;
import com.studentportal.ui.Ui;
import com.studentportal.user.User;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class HomePageUi extends Ui {
    private JButton profileBtn;
    private JButton restPasswordBtn;
    private JButton logoutBtn;

    private RequestHandler restPasswordHandler;
    private RequestHandler restPasswordConfirmHandler;

    private User user;

    public HomePageUi(User user) {
        this.user = user;
        this.restPasswordHandler = new RequestHandler() {
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

        this.restPasswordConfirmHandler = new RequestHandler() {
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
        initComponents();
        prepareGui();
    }

    public User getUser() {
        return user;
    }

    public JButton getProfileBtn() {
        return profileBtn;
    }

    public void setProfileBtn(JButton profileBtn) {
        this.profileBtn = profileBtn;
    }

    public JButton getRestPasswordBtn() {
        return restPasswordBtn;
    }

    public void setRestPasswordBtn(JButton restPasswordBtn) {
        this.restPasswordBtn = restPasswordBtn;
    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public void setLogoutBtn(JButton logoutBtn) {
        this.logoutBtn = logoutBtn;
    }

    private void initComponents() {
        profileBtn = new JButton("Profile");
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getFrame(),
                        "ID: " + user.getUserNum() + "\n" +
                                "Forename: " + user.getGivenName() + "\n" +
                                "Surname: " + user.getFamilyName() + "\n" +
                                "Email: " + user.getUserEmail() + "\n" +
                                "User Role: " + user.getUserRole());
            }
        });

        restPasswordBtn = new JButton("Reset Password");
        restPasswordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showOptionDialog(null, //Component parentComponent
                        "",
                        "Choose an option",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Send Verification Code", "Enter New Password"},
                        null);

                if (choice == 0) {
                    //  send code
                    RestPasswordRequest request = new RestPasswordRequest();
                    request.makeRequest(restPasswordHandler, user.getUserEmail());
                } else {
                    // enter new password
                    String email = user.getUserEmail();
                    String newPassword = JOptionPane.showInputDialog(getFrame(), "Enter new password");
                    String verfCode = JOptionPane.showInputDialog(getFrame(), "Enter verification code");

                    if (!StringUtils.isBlank(newPassword) || !StringUtils.isBlank(verfCode)) {
                        ForgotPasswordConfirmDetails details = new ForgotPasswordConfirmDetails(email, newPassword, verfCode);

                        String json = AuthHelper.convertPasswordConfirmDetailsToJson(details);
                        ResetPasswordConfirmRequest request = new ResetPasswordConfirmRequest();
                        request.makeRequest(restPasswordConfirmHandler, json);
                    } else {
                        JOptionPane.showMessageDialog(getFrame(),
                                "New password or verification code cannot be empty");
                    }
                }
            }
        });

        logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // delete access token, potential interception point
                close();
                LoginUI ui = new LoginUI();
                ui.show();
            }
        });
    }

    private void prepareGui() {
        getFrame().setTitle("Home");
        getFrame().setSize(500, 300);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);
    }
}
