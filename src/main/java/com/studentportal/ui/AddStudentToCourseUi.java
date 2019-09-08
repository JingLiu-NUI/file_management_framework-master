package com.studentportal.ui;

import com.studentportal.courses.Course;
import com.studentportal.http.RequestAbstractFactory;
import com.studentportal.http.RequestChoice;
import com.studentportal.http.RequestFactoryProducer;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.courses.CourseUpdateType;
import com.studentportal.http.courses.UpdateCourseDetails;
import com.studentportal.http.courses.UpdateCourseRequest;
import com.studentportal.http.users.GetAllUsersRequest;
import com.studentportal.user.User;
import com.studentportal.user.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddStudentToCourseUi extends Ui {

    private JPanel pane;
    private JPanel studentPane;
    private JPanel buttonPane;

    private JTable studentTable;
    private List<Course> cList;
    private List<User> studentList;

    private JLabel courseCodeSelectionLbl;
    private JComboBox courseCodeSelectionCmBx;
    private String[] courseCodeArr;

    private JButton addStudentBtn;
    private JButton cancelBtn;

    private RequestHandler allStudentsHandler;
    private RequestHandler addUpdateHandler;

    public AddStudentToCourseUi(List<Course> cList) {
        this.cList = cList;
        this.courseCodeArr = new String[cList.size()];
        addCourseCodesToArray();
        initHandlers();
        getAllStudents();
        initTable();
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private void addCourseCodesToArray() {
        for (int i = 0; i < cList.size(); i++) {
            String courseCode = cList.get(i).getCourseCode();
            courseCodeArr[i] = courseCode;
        }
    }

    private void initHandlers() {
        this.allStudentsHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                System.out.println("Retrieved students");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };

        this.addUpdateHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),
                        "Student has been added to the course");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };
    }

    private void getAllStudents() {
        RequestAbstractFactory userFactory = RequestFactoryProducer.getFactory(RequestChoice.USER);
        GetAllUsersRequest request = (GetAllUsersRequest) userFactory.getAllRequest();
        this.studentList =  request.makeRequest(allStudentsHandler, UserRole.STUDENT);
    }

    private void initTable() {
        studentTable = new JTable();
        List<Object[]> list = new ArrayList<>();
        for (User u : studentList) {
            list.add(new Object[]{
                    u.getUserNum(),
                    u.getGivenName(),
                    u.getFamilyName(),
                    u.getUserEmail(),
            });
        }

        TableModel studentModel = new DefaultTableModel(list.toArray(new Object[][]{}),
                new String[]{"ID", "Firstname", "Lastname", "Email"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable.setModel(studentModel);
    }

    private void initComponents() {
        courseCodeSelectionLbl = new JLabel("Course Code: ");
        courseCodeSelectionCmBx = new JComboBox(courseCodeArr);

        addStudentBtn = new JButton("Add");
        addStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add student to course request
                int studentId = getSelectedStudentId();
                String courseCode = courseCodeSelectionCmBx.getSelectedItem()
                        .toString();

                RequestAbstractFactory courseFactory = RequestFactoryProducer.getFactory(
                        RequestChoice.COURSE);
                UpdateCourseRequest request = (UpdateCourseRequest) courseFactory.updateRequest();
                request.makeRequest(addUpdateHandler, new UpdateCourseDetails(courseCode, studentId,
                        CourseUpdateType.add));
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
    }

    private int getSelectedStudentId() {
        return (int) studentTable.getValueAt(studentTable.getSelectedRow(), 0);
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;

        // course selection
        c1.gridx = 0;
        c1.gridy = 0;
        pane.add(courseCodeSelectionLbl, c1);

        c1.gridx = 1;
        c1.gridy = 0;
        pane.add(courseCodeSelectionCmBx, c1);

        studentPane = new JPanel(new BorderLayout());
        studentPane.add(studentTable, BorderLayout.CENTER);
        studentPane.add(new JScrollPane(studentTable));

        buttonPane = new JPanel(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.HORIZONTAL;

        // cancel button
        c2.gridx = 0;
        c2.gridy = 0;
        buttonPane.add(cancelBtn, c2);

        // add button
        c2.gridx = 1;
        c2.gridy = 0;
        buttonPane.add(addStudentBtn, c2);
    }

    private void prepareGui() {
        getFrame().setTitle("Add Student To Course");
        getFrame().setSize(600, 300);
        getFrame().setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(pane, BorderLayout.NORTH);
        getFrame().add(studentPane, BorderLayout.CENTER);
        getFrame().add(buttonPane, BorderLayout.SOUTH);
    }
}
