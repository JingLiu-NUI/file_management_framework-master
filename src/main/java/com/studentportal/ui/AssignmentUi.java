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

public class AssignmentUi extends Ui {

    private JPanel pane;

    private JButton setAssignmentBtn;
    private GridBagConstraints c;

    private Teacher teacher;

    private RequestHandler getAllHandler;

    public AssignmentUi(Teacher teacher) {
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
        prepareUi();
    }

    private void initComponents() {
        setAssignmentBtn = new JButton("Set Assignment");
        setAssignmentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = getAssignmentChoice();
                if (choice == 0) {
                    // quiz
                    CourseRequestFactory courseFactory = (CourseRequestFactory) RequestFactoryProducer.getFactory(
                            RequestChoice.COURSE);
                    GetAllCoursesByTeacherRequest request = (GetAllCoursesByTeacherRequest) courseFactory
                            .getAllByTeacher();
                    List<Course> cList = request.makeRequest(getAllHandler, teacher.getUserNum());
                    if (cList.size() > 0) {
                        QuizDetailsUi ui = new QuizDetailsUi(cList);
                        ui.show();
                    } else {
                        JOptionPane.showMessageDialog(getFrame(), "You have no courses belonging to you");
                    }
                } else if (choice == 1) {
                    // project
                    CourseRequestFactory courseFactory = (CourseRequestFactory) RequestFactoryProducer.getFactory(
                            RequestChoice.COURSE);
                    GetAllCoursesByTeacherRequest request = (GetAllCoursesByTeacherRequest) courseFactory
                            .getAllByTeacher();
                    List<Course> cList = request.makeRequest(getAllHandler, teacher.getUserNum());
                    if (cList.size() > 0) {
                        ProjectDetailsUi ui = new ProjectDetailsUi(cList);
                        ui.show();
                    } else {
                        JOptionPane.showMessageDialog(getFrame(), "You have no courses belonging to you");
                    }
                } else {
                    // do nothing
                }
            }
        });
    }

    private int getAssignmentChoice() {
        // project is choice = 1
        // quiz is choice = 0
        int choice = JOptionPane.showOptionDialog(getFrame(),
                "Quiz or Project",
                "Choose an option",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Quiz", "Project"},
                null);
        return choice;
    }

    private void setComponentsInPane() {
        pane = new JPanel();
        pane = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();

        // set assignment button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 150, 0, 150);
        pane.add(setAssignmentBtn, c);
    }

    private void prepareUi() {
        getFrame().setTitle("Assignments");
        getFrame().setSize(500, 300);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout(3, 1));
        getFrame().setResizable(false);

        getFrame().add(pane);
        getFrame().pack();
    }
}
