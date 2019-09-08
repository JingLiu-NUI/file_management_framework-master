package com.studentportal.ui;

import com.studentportal.assignments.Assignment;
import com.studentportal.assignments.ProjectAssignment;
import com.studentportal.assignments.QuizAssignment;
import com.studentportal.file_management.DocumentHelper;
import com.studentportal.file_management.StudentProjectDocument;
import com.studentportal.http.RequestAbstractFactory;
import com.studentportal.http.RequestChoice;
import com.studentportal.http.RequestFactoryProducer;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.assignments.GetAssignmentsByCourseIdRequest;
import com.studentportal.http.documents.SaveDocumentRequest;
import com.studentportal.http.documents.SaveStudentProjectDocRequest;
import com.studentportal.user.Student;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StudentAssignmentsUi extends Ui {

    private JPanel pane;
    private JPanel assignmentPane;
    private JButton cancelButton;
    private JButton doButton;

    private List<Assignment> aList;
    private int studentId;
    private JTable assignmentTable;
    private RequestHandler getAllHandler;

    private RequestHandler uploadHandler;
    private StudentProjectDocument spd;

    private Student student;

    public StudentAssignmentsUi(Student student) {
        super();
        initHandlers();
        this.student = student;
        this.aList = getAssignmentsById(student.getCourseIdList());
        this.studentId = student.getUserNum();
        initTable();
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private List<Assignment> getAssignmentsById(List<Integer> courseIdList) {
        GetAssignmentsByCourseIdRequest request = new GetAssignmentsByCourseIdRequest();
        List<Assignment> list = new ArrayList<>();
        for (int id : courseIdList) {
            List<Assignment> l = request.makeRequest(getAllHandler, id);
            list.addAll(l);
        }
        return list;
    }

    private void initTable() {
        assignmentTable = new JTable();
        List<Object[]> list = new ArrayList<>();
        for (Assignment a : aList) {
            if (a instanceof ProjectAssignment) {
                list.add(new Object[]{
                        a.getId(),
                        a.getCourseId(),
                        a.getCourseCode(),
                        "Project",
                        a.getName(),
                        a.getStartDate(),
                        a.getEndDate(),
                });
            } else if (a instanceof QuizAssignment) {
                list.add(new Object[]{
                        a.getId(),
                        a.getCourseId(),
                        a.getCourseCode(),
                        "Quiz",
                        a.getName(),
                        a.getStartDate(),
                        a.getEndDate(),
                });
            }
        }

        TableModel assignmentModel = new DefaultTableModel(list.toArray(new Object[][]{}),
                new String[]{"ID", "Course ID", "Course Code", "Type", "Name", "Start Date", "End Date"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        assignmentTable.setModel(assignmentModel);
    }

    private void initHandlers() {
        this.getAllHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                System.out.println("Assignments retrieved");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };

        this.uploadHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(), "Successfully uploaded");
            }

            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().contains("409")) {
                    String name = JOptionPane.showInputDialog(getFrame(), e.getMessage());
                    if (StringUtils.isBlank(name)) {
                        JOptionPane.showMessageDialog(null,
                                "Request not sent because nothing was entered");
                    } else {
                        spd.setFileName(name);
                        String json = DocumentHelper.convertDocToJson(spd);

                        RequestAbstractFactory docFactory = RequestFactoryProducer.getFactory(RequestChoice.DOCUMENT);
                        SaveDocumentRequest request = (SaveDocumentRequest) docFactory.saveRequest();
                        request.makeRequest(uploadHandler, json);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                }
            }
        };
    }

    private void initComponents() {
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> close());

        doButton = new JButton("Do Assignment");
        doButton.addActionListener(e -> {
            String type = getTypeFromTable();
            System.out.println("***TYPE***" + type);
            if (type.equals("Project")) {
                File file = openChooserAndGetFile();
                if (file != null) {
                    int assignmentId = getAssignmentIdFromTable();
                    int courseId = getCourseIdFromTable();
                    this.spd = StudentProjectDocument.createStudentProjectDoc(file, assignmentId, courseId, studentId);

                    String json = DocumentHelper.convertDocToJson(spd);
                    SaveStudentProjectDocRequest request = new SaveStudentProjectDocRequest();
                    request.makeRequest(uploadHandler, json);
                }
            } else if (type.equals("Quiz")) {
                int assignmentId = getAssignmentIdFromTable();
                int courseId = getCourseIdFromTable();

                Assignment assignment = null;
                for (Assignment a : aList) {
                    if (a.getId() == assignmentId) {
                        assignment = a;
                    }
                }

                if (assignment != null) {
                    DoQuizAssignmentUi ui = new DoQuizAssignmentUi((QuizAssignment) assignment, courseId,
                            student);
                    ui.show();
                }
            }
        });
    }

    private File openChooserAndGetFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.showOpenDialog(getFrame());
        return fc.getSelectedFile();
    }

    private String getTypeFromTable() {
        return (String) assignmentTable.getValueAt(assignmentTable.getSelectedRow(), 3);
    }

    private int getAssignmentIdFromTable() {
        return (int) assignmentTable.getValueAt(assignmentTable.getSelectedRow(), 0);
    }

    private int getCourseIdFromTable() {
        return (int) assignmentTable.getValueAt(assignmentTable.getSelectedRow(), 1);
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        // cancel button
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        pane.add(cancelButton, c);

        // do assignment button
        c.gridx = 1;
        c.gridy = 0;
        pane.add(doButton, c);

        // assignment table
        assignmentPane = new JPanel(new BorderLayout());
        assignmentPane.add(assignmentTable, BorderLayout.CENTER);
        assignmentPane.add(new JScrollPane(assignmentTable));
    }

    private void prepareGui() {
        getFrame().setTitle("Assignments");
        getFrame().setSize(950, 400);
        getFrame().setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(assignmentPane, BorderLayout.CENTER);
        getFrame().add(pane, BorderLayout.SOUTH);
    }
}
