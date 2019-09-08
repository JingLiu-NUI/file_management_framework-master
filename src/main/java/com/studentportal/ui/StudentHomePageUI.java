package com.studentportal.ui;

import com.studentportal.user.Student;
import com.studentportal.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentHomePageUI extends HomePageUi {

    private JPanel pane;
    private JButton announcementsBtn;
    private JButton remindersButton;
    private JButton assignmentsBtn;
    private Student student;

    public StudentHomePageUI(Student student) {
        super(student);
        this.student = student;
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private void initComponents() {
        announcementsBtn = new JButton("Announcements");
        announcementsBtn.addActionListener(e -> {
            AnnouncementsUI_student ui = new AnnouncementsUI_student(student);
            ui.show();
        });

        remindersButton = new JButton("Reminders");
        remindersButton.addActionListener(e -> {
            RemindersUI remindersUI = new RemindersUI(student.getUserNum());
            remindersUI.show();
        });

        assignmentsBtn = new JButton("Assignments");
        assignmentsBtn.addActionListener(e -> {
            StudentAssignmentsUi ui = new StudentAssignmentsUi(student);
            ui.show();
        });
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        // profile
        c.gridx = 1;
        c.gridy = 0;
        pane.add(super.getProfileBtn(), c);

        // announcements
        c.gridx = 1;
        c.gridy = 1;
        pane.add(announcementsBtn, c);

        // reminders button
        c.gridx = 1;
        c.gridy = 2;
        pane.add(remindersButton, c);

        // assignments
        c.gridx = 1;
        c.gridy = 3;
        pane.add(assignmentsBtn, c);

        // reset password
        c.gridx = 1;
        c.gridy = 4;
        pane.add(super.getRestPasswordBtn(), c);

        // logout
        c.gridx = 1;
        c.gridy = 5;
        pane.add(super.getLogoutBtn(), c);
    }

    private void prepareGui() {
        super.getFrame().setTitle("Student Home");
        super.getFrame().add(pane);
    }
}
