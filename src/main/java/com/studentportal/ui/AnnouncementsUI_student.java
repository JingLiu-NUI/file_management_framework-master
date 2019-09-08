package com.studentportal.ui;

import com.studentportal.announcement.Announcement;
import com.studentportal.courses.Course;
import com.studentportal.hibernate.AnnouncementService;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.announcements.GetAnnouncementsFromCourseRequest;
import com.studentportal.user.Student;
import org.omg.CORBA.portable.ResponseHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class AnnouncementsUI_student extends Ui {
    private JTable table;
    private DefaultTableModel model;
    private Student student;

    private List<Announcement> announcementList = new ArrayList<>();

    public AnnouncementsUI_student(Student student) {
        this.student = student;
        getAnnouncementsForStudent();
        getFrame().setTitle("Announcement");
        getFrame().setSize(400, 200);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);


        String[][] datas = {};
        String[] titles = {"Subject", "Message", "Course", "Released Date"};
        model = new DefaultTableModel(datas, titles);
        table = new JTable(model);

        for (Announcement a : announcementList) {
            model.addRow(new String[] {
                    a.getTitle(),
                    a.getAnnouncement(),
                    a.getCourseCode(),
                    a.getDate()
            });
        }
        getFrame().add(new JScrollPane(table));
    }

    private void getAnnouncementsForStudent() {
        RequestHandler callback = new RequestHandler() {
            @Override
            public void onSuccess() {
                System.out.println("announcements retrieved");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };

        for (Integer id : student.getCourseIdList()) {
            GetAnnouncementsFromCourseRequest request = new GetAnnouncementsFromCourseRequest();
            announcementList.addAll(request.makeRequest(callback, id));
        }
    }

}
