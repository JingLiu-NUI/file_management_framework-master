package com.studentportal.ui;

import com.studentportal.http.RequestHandler;
import com.studentportal.http.auth.SignUpRequest;
import com.studentportal.security.auth.AuthHelper;
import com.studentportal.security.auth.SignUpDetails;
import com.studentportal.user.UserRole;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminOrTeacherDetailsUi extends Ui {

    private JPanel pane;

    private JLabel emailLbl;
    private JTextField emailTxt;

    private JLabel givenNameLbl;
    private JTextField givenNameTxt;

    private JLabel familyNameLbl;
    private JTextField familyNameTxt;

    private JLabel userRoleLbl;
    private JComboBox userRoleChoiceCmBx;
    private String[] userRoles = {"ADMIN", "TEACHER"};

    private JButton createAccountBtn;
    private JButton cancelBtn;

    private RequestHandler createAccountHandler;

    public AdminOrTeacherDetailsUi() {
        this.createAccountHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),
                        "Account created, and temporary password has been sent");
                close();
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private void initComponents() {
        emailLbl = new JLabel("Email: ");
        emailTxt = new JTextField();
        givenNameLbl = new JLabel("Given Name: ");
        givenNameTxt = new JTextField();
        familyNameLbl = new JLabel("Family Name");
        familyNameTxt = new JTextField();
        userRoleLbl = new JLabel("User Role");
        userRoleChoiceCmBx = new JComboBox(userRoles);

        createAccountBtn = new JButton("Create Account");
        createAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailTxt.getText();
                String givenName = givenNameTxt.getText();
                String familyName = familyNameTxt.getText();
                UserRole userRole = UserRole.valueOf(userRoleChoiceCmBx
                        .getSelectedItem().toString());
                if (StringUtils.isBlank(email) || StringUtils.isBlank(givenName) ||
                        StringUtils.isBlank(familyName)) {
                    JOptionPane.showMessageDialog(getFrame(), "Fields cannot be empty");
                } else {
                    SignUpDetails details = new SignUpDetails(email, givenName,
                            familyName, userRole);
                    String json = AuthHelper.convertSignUpDetailsToJson(details);
                    SignUpRequest request = new SignUpRequest();
                    request.makeRequest(createAccountHandler, json);
                }
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        // email
        c.gridx = 0;
        c.gridy = 0;
        pane.add(emailLbl, c);

        c.gridx = 1;
        c.gridy = 0;
        pane.add(emailTxt, c);

        // given name
        c.gridx = 0;
        c.gridy = 1;
        pane.add(givenNameLbl, c);

        c.gridx = 1;
        c.gridy = 1;
        pane.add(givenNameTxt, c);

        // family name
        c.gridx = 0;
        c.gridy = 2;
        pane.add(familyNameLbl, c);

        c.gridx = 1;
        c.gridy = 2;
        pane.add(familyNameTxt, c);

        // user role
        c.gridx = 0;
        c.gridy = 3;
        pane.add(userRoleLbl, c);

        c.gridx = 1;
        c.gridy = 3;
        pane.add(userRoleChoiceCmBx, c);

        // cancel button
        c.gridx = 0;
        c.gridy = 4;
        pane.add(cancelBtn, c);

        // create account button
        c.gridx = 1;
        c.gridy = 4;
        pane.add(createAccountBtn, c);

    }

    private void prepareGui() {
        getFrame().setTitle("Account Details");
        getFrame().setSize(600, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(pane);
    }
}
