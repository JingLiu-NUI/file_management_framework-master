package com.studentportal.ui;

import com.studentportal.assignments.AssignmentHelper;
import com.studentportal.assignments.CompletedQuiz;
import com.studentportal.assignments.QuizAssignment;
import com.studentportal.assignments.QuizQuestion;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.assignments.SubmitCompletedQuizRequest;
import com.studentportal.user.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DoQuizAssignmentUi extends Ui {

    private JPanel pane;
    private JPanel questionsPane;

    private QuizAssignment assignment;
    private int courseId;
    private Student student;

    private JButton submitBtn;
    private JButton cancelBtn;

    private JTable questionsTable;
    private DefaultTableModel questionsModel;
    private RequestHandler submitCallback;

    public DoQuizAssignmentUi(QuizAssignment assignment, int courseId, Student student) {
        this.assignment = assignment;
        this.courseId = courseId;
        this.student = student;
        initTable();
        initHandler();
        initComponents();
        setComponentsInPane();
        prepareGui();
    }

    private void initHandler() {
        this.submitCallback = new RequestHandler() {
            @Override
            public void onSuccess() {
                System.out.println("Successfully graded quiz");
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
            }
        };
    }

    private void initTable() {
        questionsTable = new JTable();
        List<Object> list = new ArrayList<>();
        for (QuizQuestion qq : assignment.getQuizQuestions()) {
            list.add(new Object[]{
                    qq.getQuestion(),
                    qq.getChoices(),
                    null,
            });
        }
        questionsModel = new DefaultTableModel(list.toArray(new Object[][]{}),
                new String[]{"Question", "Choices", "Answer"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 2) {
                    return true;
                }
                return false;
            }
        };
        questionsTable.setModel(questionsModel);
    }

    private void initComponents() {
        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> close());

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(e -> {
            List<String> answers = getAnswersFromRows();
            int studentId = student.getUserNum();
            int assignmentId = assignment.getId();

            CompletedQuiz compQuiz = new CompletedQuiz(0, answers, assignmentId, studentId,
                    courseId, 0);
            String json = AssignmentHelper.convertCompletedQuizToJson(compQuiz);
            System.out.println(json);
            SubmitCompletedQuizRequest request = new SubmitCompletedQuizRequest();
            String score = request.makeRequest(submitCallback, json);

            JOptionPane.showMessageDialog(getFrame(), "You achieved " + score + "%");
            close();
        });
    }

    private List<String> getAnswersFromRows() {
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < questionsTable.getRowCount(); i++) {
            answers.add((String) questionsTable.getValueAt(i, 2));
        }
        return answers;
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;

        // cancel
        c.gridx = 0;
        c.gridy = 0;
        pane.add(cancelBtn, c);

        // submit
        c.gridx = 1;
        c.gridy = 0;
        pane.add(submitBtn, c);

        // questions
        questionsPane = new JPanel(new BorderLayout());
        questionsPane.add(questionsTable);
        questionsPane.add(new JScrollPane(questionsTable));
    }

    private void prepareGui() {
        getFrame().setTitle("Do Quiz");
        getFrame().setSize(700, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new BorderLayout());
        getFrame().setResizable(false);

        getFrame().add(questionsPane, BorderLayout.CENTER);
        getFrame().add(pane, BorderLayout.SOUTH);
    }
}
