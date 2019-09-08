package com.studentportal.ui;


import com.studentportal.courses.Course;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuizDetailsUi extends Ui {

    private JPanel pane;

    private JLabel nameLbl;
    private JTextField nameTxt;

    private JLabel courseCodeLbl;
    private JComboBox courseCmBx;
    private String[] courseCodeArr;

    private JLabel startDateLbl;
    private JTextField startDateTxt;

    private JLabel endDateLbl;
    private JTextField endDateTxt;

    private JButton cancelBtn;
    private JButton doneBtn;

    private List<Course> cList;

    public QuizDetailsUi(List<Course> cList) {
        this.cList = cList;
        this.courseCodeArr = new String[cList.size()];
        addCourseCodesToArray();
        initComponents();
        setComponentsInPane();
        prepareUi();
    }

    private void addCourseCodesToArray() {
        for (int i = 0; i < cList.size(); i++) {
            String courseCode = cList.get(i).getCourseCode();
            courseCodeArr[i] = courseCode;
        }
    }

    private void initComponents() {
        nameLbl = new JLabel("Name:");
        nameTxt = new JTextField();
        courseCodeLbl = new JLabel("Course Code: ");
        courseCmBx = new JComboBox(courseCodeArr);
        startDateLbl = new JLabel("Start Date(dd/MM/yyyy)");
        startDateTxt = new JTextField();
        endDateLbl = new JLabel("End Date(dd/MM/yyyy)");
        endDateTxt = new JTextField();

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFrame().dispose();
            }
        });

        doneBtn = new JButton("Done");
        doneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = courseCmBx.getSelectedItem().toString();
                if (StringUtils.isBlank(courseCode)) {
                    JOptionPane.showMessageDialog(getFrame(), "No course code selected");
                } else {
                    String name = nameTxt.getText();
                    int courseId = getCourseId(courseCmBx.getSelectedItem().toString());
                    String startDateStr = startDateTxt.getText();
                    String endDateStr = endDateTxt.getText();

                    if (StringUtils.isBlank(name) || StringUtils.isBlank(startDateStr)
                            || StringUtils.isBlank(endDateStr)) {
                        JOptionPane.showMessageDialog(getFrame(), "No fields can be empty");
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date startDate = null;
                        Date endDate = null;
                        try {
                            startDate = sdf.parse(startDateStr);
                            endDate = sdf.parse(endDateStr);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }

                        Course course = null;
                        for (Course c : cList) {
                            if (c.getCourseCode().equals(courseCode)) {
                                course = c;
                            }
                        }

                        CreateQuizAssignmentUi ui = new CreateQuizAssignmentUi(name, courseId, course,
                                startDate, endDate);
                        ui.show();
                        getFrame().dispose();
                    }
                }
            }
        });
    }

    private int getCourseId(String courseCode) {
        int id = -1;
        for (Course c : cList) {
            if (c.getCourseCode().equals(courseCode)) {
                id = c.getId();
            }
        }
        return id;
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        // name
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        pane.add(nameLbl, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        pane.add(nameTxt, c);

        // lecturer
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.5;
        pane.add(courseCodeLbl, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        pane.add(courseCmBx, c);

        // start date
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.5;
        pane.add(startDateLbl, c);

        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.5;
        pane.add(startDateTxt, c);

        // end date
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 0.5;
        pane.add(endDateLbl, c);

        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0.5;
        pane.add(endDateTxt, c);

        // cancel button
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0.5;
        pane.add(cancelBtn, c);

        // done button
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 0.5;
        pane.add(doneBtn, c);
    }

    private void prepareUi() {
        getFrame().setTitle("Quiz Details");
        getFrame().setSize(600, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(pane);
    }
}
