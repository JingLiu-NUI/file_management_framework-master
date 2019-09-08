package com.studentportal.ui;

import com.studentportal.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminHomePageUi extends HomePageUi {

    private JButton newAdminOrTeacherButton;
    private JPanel pane;

    public AdminHomePageUi(User user) {
        super(user);
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private void initComponents() {
        newAdminOrTeacherButton = new JButton("New Admin/Teacher Account");
        newAdminOrTeacherButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminOrTeacherDetailsUi detailsUi = new AdminOrTeacherDetailsUi();
                detailsUi.show();
            }
        });
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        // profile button
        c.gridx = 1;
        c.gridy = 0;
        pane.add(super.getProfileBtn(), c);

        // new admin button
        c.gridx = 1;
        c.gridy = 1;
        pane.add(newAdminOrTeacherButton, c);

        // rest password button
        c.gridx = 1;
        c.gridy = 2;
        pane.add(super.getRestPasswordBtn(), c);

        // logout button
        c.gridx = 1;
        c.gridy = 3;
        pane.add(super.getLogoutBtn(), c);
    }

    private void prepareGui() {
        super.getFrame().setTitle("Admin Home");
        super.getFrame().add(pane);
    }
}
