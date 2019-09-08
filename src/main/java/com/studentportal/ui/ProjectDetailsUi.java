package com.studentportal.ui;

import com.studentportal.assignments.AssignmentHelper;
import com.studentportal.assignments.ProjectAssignment;
import com.studentportal.courses.Course;
import com.studentportal.hibernate.CourseService;
import com.studentportal.http.*;
import com.studentportal.http.assignments.SaveProjectRequest;
import com.studentportal.http.reminders.SaveReminderRequest;
import com.studentportal.reminders.ReminderHelper;
import com.studentportal.reminders.ReminderTypes.AssignmentReminder;
import com.studentportal.reminders.Senders.SenderType;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjectDetailsUi extends Ui {

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

    private JLabel specificationLbl;
    private JTextArea specificationTxtArea;

    private JButton cancelBtn;
    private JButton createBtn;

    private ProjectAssignment assignment;
    private RequestHandler createHandler;

    private List<Course> cList;

    public ProjectDetailsUi(List<Course> cList) {
        this.cList = cList;
        this.courseCodeArr = new String[cList.size()];
        this.createHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(), "Successfully created assignment");
                getFrame().dispose();
            }

            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().contains("409")) {
                    String newName = JOptionPane.showInputDialog(null, e.getMessage());
                    if (StringUtils.isBlank(newName)) {
                        JOptionPane.showMessageDialog(null,
                                "Request not sent because nothing was entered");
                    } else {
                        assignment.setName(newName);
                        String json = AssignmentHelper.convertAssignmentToJson(assignment);

                        RequestAbstractFactory projectFactory = RequestFactoryProducer.getFactory(RequestChoice.PROJECT);
                        SaveProjectRequest request = (SaveProjectRequest) projectFactory.saveRequest();
                        request.makeRequest(createHandler, json);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                }
            }
        };
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
        specificationLbl = new JLabel("Specification");
        specificationTxtArea = new JTextArea();
        specificationTxtArea.setLineWrap(true);

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFrame().dispose();
            }
        });

        createBtn = new JButton("Create");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String courseCode = courseCmBx.getSelectedItem().toString();
                if (StringUtils.isBlank(courseCode)) {
                    JOptionPane.showMessageDialog(getFrame(), "No course code selected");
                } else {
                    String name = nameTxt.getText();
                    int courseId = getCourseId(courseCode);
                    String startDateStr = startDateTxt.getText();
                    String endDateStr = endDateTxt.getText();
                    String specification = specificationTxtArea.getText();

                    if (StringUtils.isBlank(name) || StringUtils.isBlank(startDateStr) ||
                            StringUtils.isBlank(endDateStr) || StringUtils.isBlank(specification)) {
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
                        assignment = new ProjectAssignment(0, name, courseId, courseCode, startDate, endDate,
                                specification);
                        String json = AssignmentHelper.convertAssignmentToJson(assignment);
                        RequestAbstractFactory projectFactory = RequestFactoryProducer.getFactory(RequestChoice.PROJECT);
                        SaveProjectRequest request = (SaveProjectRequest) projectFactory.saveRequest();
                        request.makeRequest(createHandler, json);

                        sendReminders(courseCode, endDate);
                    }
                }
            }
        });
    }

    private void sendReminders(String courseCode, Date endDate) {

        RequestHandler reminderHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                System.out.println("Reminder successfully saved");
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        };

//        CourseService courseService = new CourseService();
//        Course course = courseService.findByCode(courseCode);
        Course course = null;
        for (Course c : cList) {
            if (c.getCourseCode().equals(courseCode)) {
                course = c;
            }
        }


        AssignmentReminder reminder = new AssignmentReminder.AssignmentReminderBuilder(SenderType.EMAIL)
                .date(endDate)
                .targetUserIds(course.getStudentIdList())
                .build();

        String reminderJson = ReminderHelper.convertReminderToJson(reminder);

        SaveReminderRequest saveReminderRequest = new SaveReminderRequest();
        saveReminderRequest.makeRequest(reminderHandler, reminderJson);
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
        c.gridy = 6;
        c.weightx = 0.5;
        pane.add(cancelBtn, c);

        // done button
        c.gridx = 1;
        c.gridy = 6;
        c.weightx = 0.5;
        pane.add(createBtn, c);

        // specification
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 0.5;
        pane.add(specificationLbl, c);

        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.ipady = 20;
        pane.add(specificationTxtArea, c);
    }

    private void prepareUi() {
        getFrame().setTitle("Project Details");
        getFrame().setSize(600, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(pane);
    }
}
