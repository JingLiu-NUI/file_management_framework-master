package com.studentportal.ui;

import com.studentportal.assignments.AssignmentHelper;
import com.studentportal.assignments.QuizAssignment;
import com.studentportal.assignments.QuizQuestion;
import com.studentportal.courses.Course;
import com.studentportal.http.RequestAbstractFactory;
import com.studentportal.http.RequestChoice;
import com.studentportal.http.RequestFactoryProducer;
import com.studentportal.http.RequestHandler;
import com.studentportal.http.assignments.SaveQuizRequest;
import com.studentportal.http.reminders.SaveReminderRequest;
import com.studentportal.reminders.ReminderHelper;
import com.studentportal.reminders.ReminderTypes.AssignmentReminder;
import com.studentportal.reminders.Senders.SenderType;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateQuizAssignmentUi extends Ui {

    private JPanel pane;
    private JPanel tablePane;
    private GridBagConstraints c;

    private JButton addQuestionBtn;
    private JButton removeQuestionBtn;
    private JButton createBtn;

    private JTable qTable;
    private DefaultTableModel qModel;

    private String name;
    private int courseId;
    private Course course;
    private Date startDate;
    private Date endDate;

    private QuizAssignment assignment;

    private RequestHandler createHandler;

    public CreateQuizAssignmentUi(String name, int courseId, Course course, Date startDate, Date endDate) {
        this.name = name;
        this.courseId = courseId;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
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
                        JOptionPane.showInputDialog(null,
                                "Request not sent because nothing was entered");
                    } else {
                        assignment.setName(newName);
                        String json = AssignmentHelper.convertAssignmentToJson(assignment);

                        RequestAbstractFactory quizReqFactory = RequestFactoryProducer
                                .getFactory(RequestChoice.QUIZ);
                        SaveQuizRequest request = (SaveQuizRequest) quizReqFactory.saveRequest();
                        request.makeRequest(createHandler, json);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                }
            }
        };

        initComponents();
        initTable();
        setComponentsInPane();
        prepareUi();
    }

    public void addQuestion() {
        QuizQuestion qq = new QuizQuestion();
        qq.setQuestion("This is a sample question?");
        qq.setChoices("1. X\n 2. Y\n 3. Z");
        qq.setAnswer("0");
        qModel.addRow(new Object[]{qq.getQuestion(), qq.getChoices(), qq.getAnswer()});
    }

    public void removeQuestion() {
        int row = qTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(getFrame(), "Select a row to delete");
        } else {
            qModel.removeRow(row);
        }
    }

    private void initTable() {
        qTable = new JTable();
        List<QuizQuestion> qList = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        for (QuizQuestion qq : qList) {
            list.add(new Object[]{
                    qq.getQuestion(),
                    qq.getChoices(),
                    qq.getAnswer(),
            });
        }
        qModel = new DefaultTableModel(list.toArray(new Object[][]{}),
                new String[]{"Question", "Choices", "Answer"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        qTable.setModel(qModel);
    }

    private void initComponents() {
        addQuestionBtn = new JButton("Add Question");
        addQuestionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        removeQuestionBtn = new JButton("Delete Question");
        removeQuestionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeQuestion();
            }
        });

        createBtn = new JButton("Create");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowCount = qTable.getRowCount();
                if (rowCount > 0) {
                    assignment = new QuizAssignment(0, name, courseId, course.getCourseCode(),
                            startDate, endDate, null);
                    List<QuizQuestion> quizList = getQuestionsFromRows(rowCount, assignment);
                    assignment.setQuizQuestions(quizList);

                    String json = AssignmentHelper.convertAssignmentToJson(assignment);
                    RequestAbstractFactory quizReqFactory = RequestFactoryProducer
                            .getFactory(RequestChoice.QUIZ);
                    SaveQuizRequest request = (SaveQuizRequest) quizReqFactory.saveRequest();
                    request.makeRequest(createHandler, json);

                    sendReminders();
                } else {
                    JOptionPane.showMessageDialog(getFrame(), "No questions for quiz");
                }
            }
        });
    }

    private void sendReminders() {

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

        AssignmentReminder reminder = new AssignmentReminder.AssignmentReminderBuilder(SenderType.EMAIL)
                .date(endDate)
                .targetUserIds(course.getStudentIdList())
                .build();

        String reminderJson = ReminderHelper.convertReminderToJson(reminder);

        SaveReminderRequest saveReminderRequest = new SaveReminderRequest();
        saveReminderRequest.makeRequest(reminderHandler, reminderJson);
    }

    private List<QuizQuestion> getQuestionsFromRows(int rowCount, QuizAssignment a) {
        List<QuizQuestion> quizList = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            QuizQuestion q = new QuizQuestion();
            q.setQuizId(0);
            q.setQuestion((String) qTable.getValueAt(i, 0));
            q.setChoices((String) qTable.getValueAt(i, 1));
            q.setAnswer((String) qTable.getValueAt(i, 2));
            q.setAssignment(a);
            quizList.add(q);
        }
        return quizList;
    }

    private void setComponentsInPane() {
        pane = new JPanel(new GridLayout());
        pane.setSize(300, 300);
        c = new GridBagConstraints();

        // add question button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 150, 0, 150);
        pane.add(addQuestionBtn, c);

        // remove question button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 150, 0, 150);
        pane.add(removeQuestionBtn, c);

        // create button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0, 150, 0, 150);
        pane.add(createBtn, c);


        // table
        tablePane = new JPanel(new GridLayout(1, 1));
        tablePane.add(qTable);
        tablePane.add(new JScrollPane(qTable));
    }

    private void prepareUi() {
        getFrame().setTitle("Create Quiz");
        getFrame().setSize(700, 300);
        getFrame().setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout(2, 1));
        getFrame().setResizable(false);

        getFrame().add(pane);
        getFrame().add(tablePane, 2, 1);
    }
}
