package com.studentportal.ui;

import com.studentportal.courses.Course;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.courses.GetAllCoursesByTeacherRequest;
import com.studentportal.user.Teacher;
import com.studentportal.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TeacherHomePageUI extends HomePageUi {

    private JPanel pane;

    private JButton fileManagementBtn;
    private JButton announcementsBtn;
    private JButton assignmentsBtn;
    private JButton newCourseBtn;
    private JButton studentManagementBtn;

    public TeacherHomePageUI(User user) {
        super(user);
        initComponents();
        initHandlers();
        setComponentsInPane();
        prepareGui();
    }

    private void initComponents() {
        fileManagementBtn = new JButton("File Management");
        fileManagementBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileManagementUi fileUi = new FileManagementUi();
                fileUi.show();
            }
        });

        announcementsBtn = new JButton("Announcements");
        announcementsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RequestHandler callback = new RequestHandler() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Got courses by teacher");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                    }
                };
                GetAllCoursesByTeacherRequest request = new GetAllCoursesByTeacherRequest();
                List<Course> cList = request.makeRequest(callback, getUser().getUserNum());
                AnnouncementsUI_teacher ui = new AnnouncementsUI_teacher(cList);
                ui.show();
            }
        });

        assignmentsBtn = new JButton("Assignments");
        assignmentsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AssignmentUi assignmentUi = new AssignmentUi((Teacher) getUser());
                assignmentUi.show();
            }
        });

        newCourseBtn = new JButton("New Course");
        newCourseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CourseDetailsUi courseUi = new CourseDetailsUi(
                        (Teacher) TeacherHomePageUI.super.getUser());
                courseUi.show();
            }
        });

        studentManagementBtn = new JButton("Student Management");
        studentManagementBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentManagementUi studentUi = new StudentManagementUi(
                        (Teacher) TeacherHomePageUI.super.getUser());
                studentUi.show();
            }
        });
    }

    private void initHandlers() {

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

        // file management
        c.gridx = 1;
        c.gridy = 2;
        pane.add(fileManagementBtn, c);

        // announcements
        c.gridx = 1;
        c.gridy = 3;
        pane.add(announcementsBtn, c);

        // assignments
        c.gridx = 1;
        c.gridy = 4;
        pane.add(assignmentsBtn, c);

        // new course
        c.gridx = 1;
        c.gridy = 5;
        pane.add(newCourseBtn, c);

        // new student
        c.gridx = 1;
        c.gridy = 6;
        pane.add(studentManagementBtn, c);

        // rest password
        c.gridx = 1;
        c.gridy = 7;
        pane.add(super.getRestPasswordBtn(), c);

        // logout
        c.gridx = 1;
        c.gridy = 8;
        pane.add(super.getLogoutBtn(), c);
    }

    private void prepareGui() {
        super.getFrame().setTitle("Teacher Home");
        super.getFrame().add(pane);
    }
}
