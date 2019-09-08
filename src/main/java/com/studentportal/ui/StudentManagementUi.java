package com.studentportal.ui;

import com.studentportal.courses.Course;
import com.studentportal.http.RequestChoice;
import com.studentportal.http.RequestFactoryProducer;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.courses.CourseRequestFactory;
import com.studentportal.http.courses.GetAllCoursesByTeacherRequest;
import com.studentportal.user.Teacher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementUi extends Ui {

    private JPanel pane;

    private JButton addStudentBtn;
    private JButton viewStudents;
    private JButton addStudentToCourseBtn;
    private JButton removeStudentFromCourseBtn;

    private Teacher teacher;

    private RequestHandler getAllHandler;

    public StudentManagementUi(Teacher teacher) {
        this.teacher = teacher;
        this.getAllHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                System.out.println("Courses retrieved");
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
        addStudentBtn = new JButton("New Student Account");
        addStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentDetailsUi detailsUi = new StudentDetailsUi();
                detailsUi.show();
            }
        });

        addStudentToCourseBtn = new JButton("Add Student To Course");
        addStudentToCourseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CourseRequestFactory courseFactory = (CourseRequestFactory) RequestFactoryProducer.getFactory(
                        RequestChoice.COURSE);
                GetAllCoursesByTeacherRequest request = (GetAllCoursesByTeacherRequest) courseFactory
                        .getAllByTeacher();
                List<Course> cList = request.makeRequest(getAllHandler, teacher.getUserNum());
                if (cList.size() > 0) {
                    AddStudentToCourseUi ui = new AddStudentToCourseUi(cList);
                    ui.show();
                } else {
                    JOptionPane.showMessageDialog(getFrame(), "You have no courses belonging to you");
                }
            }
        });

        removeStudentFromCourseBtn = new JButton("Remove Student From Course");
        removeStudentFromCourseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CourseRequestFactory courseFactory = (CourseRequestFactory) RequestFactoryProducer.getFactory(
                        RequestChoice.COURSE);
                GetAllCoursesByTeacherRequest request = (GetAllCoursesByTeacherRequest) courseFactory
                        .getAllByTeacher();
                List<Course> cList = request.makeRequest(getAllHandler, teacher.getUserNum());
                if (cList.size() > 0) {
                    RemoveStudentFromCourseUi ui = new RemoveStudentFromCourseUi(cList);
                    ui.show();
                } else {
                    JOptionPane.showMessageDialog(getFrame(), "You have no courses belonging to you");
                }
            }
        });
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        // add student
        c.gridx = 1;
        c.gridy = 0;
        pane.add(addStudentBtn, c);
        // view students

        // add student to course
        c.gridx = 1;
        c.gridy = 1;
        pane.add(addStudentToCourseBtn, c);

        // remove student from course
        c.gridx = 1;
        c.gridy = 2;
        pane.add(removeStudentFromCourseBtn, c);
    }

    private void prepareGui() {
        getFrame().setTitle("Student Management");
        getFrame().setSize(600, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(pane);
    }
}
