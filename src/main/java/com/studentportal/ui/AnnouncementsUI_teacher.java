package com.studentportal.ui;

import com.studentportal.announcement.Announcement;
import com.studentportal.announcement.CareTaker;
import com.studentportal.announcement.Memento;
import com.studentportal.announcement.Originator;
import com.studentportal.courses.Course;
import com.studentportal.courses.CourseHelper;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.announcements.SaveAnnouncementRequest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AnnouncementsUI_teacher extends Ui {

    private JLabel MessageLabel;
    private JLabel TitleLabel;
    private JTextArea messageField;
    private JTextArea titleField;
    private JButton Cancel;
    private JButton Submit;
    private JButton Undo;
    private String[] message;

    Originator ori = new Originator();
    Memento m;
    CareTaker careTaker = new CareTaker();

    private JLabel courseCodeLbl;
    private JComboBox courseCodeCmBx;
    private String[] courseCodeArr;
    private List<Course> cList;

    public AnnouncementsUI_teacher(List<Course> cList) {
        this.cList = cList;
        this.courseCodeArr = new String[cList.size()];
        addCourseCodesToArray();
        getFrame().setSize(400, 200);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout(3, 3));

        courseCodeLbl = new JLabel("Course Code");
        courseCodeCmBx = new JComboBox(courseCodeArr);

        getFrame().add(courseCodeLbl);
        getFrame().add(courseCodeCmBx);

        TitleLabel = new JLabel("Title : ");
        getFrame().add(TitleLabel);
        titleField = new JTextArea();
        getFrame().add(titleField);

        MessageLabel = new JLabel("Message Area: ");
        getFrame().add(MessageLabel);
        messageField = new JTextArea();
        messageField.setLineWrap(true);
        getFrame().add(messageField);

        messageField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32) {
                    message = messageField.getText().split(" ");
                    careTaker.saveMemento(ori.createMemento(new ArrayList<>(Arrays.asList(message))));
                }
            }
        });
        Cancel = new JButton("Cancel");
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        getFrame().add(Cancel);

        Submit = new JButton("Submit");
        getFrame().add(Submit);
        Submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = 0;
                String title = titleField.getText();
                String message = messageField.getText();
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String releaseDate = format.format(date);

                String courseCode = courseCodeCmBx.getSelectedItem().toString();
                Course course = getCourseBasedOnCode(courseCode);

                if (course != null) {
                    course.addAnnouncement(new Announcement(id, title, message, releaseDate, course, courseCode));
                    String json = CourseHelper.convertCourseToJson(course);
                    SaveAnnouncementRequest request = new SaveAnnouncementRequest();
                    RequestHandler callback = new RequestHandler() {
                        @Override
                        public void onSuccess() {
                            JOptionPane.showMessageDialog(getFrame(), "Announcement saved.");
                        }

                        @Override
                        public void onFailure(Exception e) {
                            JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                        }
                    };
                    request.makeRequest(callback, json);
                }
            }
        });

        Undo = new JButton("Undo");
        Undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                m = careTaker.getLastMemento();
                if (m == null) return;
                messageField.setText(ori.Undo(m));

            }
        });
        getFrame().add(Undo);

    }

    private Course getCourseBasedOnCode(String courseCode) {
        for (Course c : cList) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null;
    }

    private void addCourseCodesToArray() {
        for (int i = 0; i < cList.size(); i++) {
            String courseCode = cList.get(i).getCourseCode();
            courseCodeArr[i] = courseCode;
        }
    }
}
