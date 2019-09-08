package com.studentportal.ui;

import com.studentportal.courses.Course;
import com.studentportal.courses.CourseHelper;
import com.studentportal.http.RequestAbstractFactory;
import com.studentportal.http.RequestChoice;
import com.studentportal.http.RequestFactoryProducer;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.courses.SaveCourseRequest;
import com.studentportal.user.Teacher;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CourseDetailsUi extends Ui {

    private JPanel pane;

    private JLabel courseCodeLbl;
    private JTextField courseCodeTxt;

    private JLabel teacherIdLbl;
    private JTextField teacherIdTxt;

    private JButton cancelBtn;
    private JButton createBtn;

    private Teacher teacher;

    private RequestHandler createCallback;
    private Course course;

    public CourseDetailsUi(Teacher teacher) {
        this.teacher = teacher;
        this.createCallback = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),
                        "Course created");
                close();
            }

            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().contains("409")) {
                    String newCode = JOptionPane.showInputDialog(null, e.getMessage());
                    if (StringUtils.isBlank(newCode)) {
                        JOptionPane.showMessageDialog(null,
                                "Request not sent because nothing was entered");
                    } else {
                        course.setCourseCode(newCode);

                        String json = CourseHelper.convertCourseToJson(course);
                        RequestAbstractFactory courseFactory = RequestFactoryProducer.getFactory(RequestChoice.COURSE);
                        SaveCourseRequest request = (SaveCourseRequest) courseFactory.saveRequest();
                        request.makeRequest(createCallback, json);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                }
            }
        };
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private void initComponents() {
        courseCodeLbl = new JLabel("Course Code: ");
        courseCodeTxt = new JTextField();

        teacherIdLbl = new JLabel("Teacher ID: ");
        teacherIdTxt = new JTextField();
        teacherIdTxt.setText(String.valueOf(teacher.getUserNum()));
        teacherIdTxt.setEditable(false);

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });

        createBtn = new JButton("Create");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = courseCodeTxt.getText();
                int teacherId = new Integer(teacherIdTxt.getText());
                if (StringUtils.isBlank(code)) {
                    JOptionPane.showMessageDialog(getFrame(), "Complete all fields");
                } else {
                    course = new Course(0, code, teacherId);

                    String json = CourseHelper.convertCourseToJson(course);
                    RequestAbstractFactory courseFactory = RequestFactoryProducer.getFactory(RequestChoice.COURSE);
                    SaveCourseRequest request = (SaveCourseRequest) courseFactory.saveRequest();
                    request.makeRequest(createCallback, json);
                }
            }
        });
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        // course code
        c.gridx = 0;
        c.gridy = 0;
        pane.add(courseCodeLbl, c);

        c.gridx = 1;
        c.gridy = 0;
        pane.add(courseCodeTxt, c);

        // teacher email
        c.gridx = 0;
        c.gridy = 1;
        pane.add(teacherIdLbl, c);

        c.gridx = 1;
        c.gridy = 1;
        pane.add(teacherIdTxt, c);

        // cancel button
        c.gridx = 0;
        c.gridy = 2;
        pane.add(cancelBtn, c);

        // create button
        c.gridx = 1;
        c.gridy = 2;
        pane.add(createBtn, c);
    }

    private void prepareGui() {
        getFrame().setTitle("Course Details");
        getFrame().setSize(600, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(pane);
    }
}
