package com.studentportal.ui;

import com.studentportal.assignments.QuizQuestion;
import com.studentportal.courses.Course;
import com.studentportal.http.RequestAbstractFactory;
import com.studentportal.http.RequestChoice;
import com.studentportal.http.RequestFactoryProducer;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.courses.CourseUpdateType;
import com.studentportal.http.courses.UpdateCourseDetails;
import com.studentportal.http.courses.UpdateCourseRequest;
import com.studentportal.http.users.GetAllUsersRequest;
import com.studentportal.user.Student;
import com.studentportal.user.User;
import com.studentportal.user.UserRole;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class RemoveStudentFromCourseUi extends Ui {
    private JPanel pane;
    private JPanel studentPane;
    private JPanel buttonPane;

    private JTable studentTable;
    private DefaultTableModel studentModel;
    private List<Course> cList;
    private List<User> studentList;

    private JLabel courseCodeSelectionLbl;
    private JComboBox courseCodeSelectionCmBx;
    private String[] courseCodeArr;

    private JButton removeStudentBtn;
    private JButton cancelBtn;

    private RequestHandler allStudentsHandler;
    private RequestHandler removeUpdateHandler;

    public RemoveStudentFromCourseUi(List<Course> cList) {
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

        this.removeUpdateHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),
                        "Student has been removed from the course");
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
        this.studentList = request.makeRequest(allStudentsHandler, UserRole.STUDENT);
    }

    private void initTable() {
        studentTable = new JTable();
        List<User> emptyUserList = new ArrayList<>();
        List<Object[]> list = new ArrayList<>();
        for (User u : emptyUserList) {
            list.add(new Object[]{
                    u.getUserNum(),
                    u.getGivenName(),
                    u.getFamilyName(),
                    u.getUserEmail(),
            });
        }

        studentModel = new DefaultTableModel(list.toArray(new Object[][]{}),
                new String[]{"ID", "Firstname", "Lastname", "Email"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable.setModel(studentModel);
        addStudentsToTable(courseCodeArr[0]);
    }

    private void initComponents() {
        courseCodeSelectionLbl = new JLabel("Course Code: ");
        courseCodeSelectionCmBx = new JComboBox(courseCodeArr);
        courseCodeSelectionCmBx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String courseCode = courseCodeSelectionCmBx.getSelectedItem()
                            .toString();
                    removeStudentsFromTable();
                    addStudentsToTable(courseCode);
                }
            }
        });

        removeStudentBtn = new JButton("Remove");
        removeStudentBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // remove student from course request
                int studentId = getSelectedStudentId();
                if (studentId == -1) {
                    JOptionPane.showMessageDialog(getFrame(), "No student to remove");
                } else {
                    String courseCode = courseCodeSelectionCmBx.getSelectedItem()
                            .toString();

                    RequestAbstractFactory courseFactory = RequestFactoryProducer.getFactory(
                            RequestChoice.COURSE);
                    UpdateCourseRequest request = (UpdateCourseRequest) courseFactory.updateRequest();
                    request.makeRequest(removeUpdateHandler, new UpdateCourseDetails(courseCode, studentId,
                            CourseUpdateType.remove));
                }
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

    private void addStudentsToTable(String courseCode) {
        for (Course course : cList) {
            if (course.getCourseCode().equals(courseCode)) {
                int courseId = course.getId();
                for (User user : studentList) {
                    Student student = (Student) user;
                    if (student.getCourseIdList().contains(courseId)) {
                        studentModel.addRow(new Object[]{
                                student.getUserNum(),
                                student.getGivenName(),
                                student.getFamilyName(),
                                student.getUserEmail()
                        });
                    }
                }
            }
        }
    }

    private void removeStudentsFromTable() {
        int numOfRows = studentTable.getRowCount();
        if (numOfRows > 0) {
            for (int i = 0; i < numOfRows; i++) {
                studentModel.removeRow(i);
            }
        }
    }

    private int getSelectedStudentId() {
        try {
            return (int) studentTable.getValueAt(studentTable.getSelectedRow(), 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
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
        buttonPane.add(removeStudentBtn, c2);
    }

    private void prepareGui() {
        getFrame().setTitle("Remove Student From Course");
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
