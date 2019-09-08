package com.studentportal.ui;


import com.studentportal.http.RequestHandler;
import com.studentportal.http.reminders.GetRemindersForUserRequest;
import com.studentportal.reminders.ReminderTypes.AssignmentReminder;
import com.studentportal.reminders.ReminderTypes.MeetingReminder;
import com.studentportal.reminders.ReminderTypes.Reminder;
import com.studentportal.reminders.Senders.EmailReminderSender;
import com.studentportal.reminders.Senders.SMSReminderSender;
import com.studentportal.reminders.Senders.SenderType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RemindersUI extends Ui {
    private JButton backButton;
    private JButton fullDetailsButton;
    private JTable reminderTable;
    private List<Reminder> reminders;

    private int userId;

    public RemindersUI(int userId) {
        this.userId = userId;
        populateReminders();
        prepareGUI();
    }

    private void populateReminders() {
        RequestHandler callback = new RequestHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        GetRemindersForUserRequest request = new GetRemindersForUserRequest();
        reminders = request.makeRequest(callback, userId);
    }

    private void prepareGUI() {
        getFrame().setTitle("View Reminders");
        getFrame().setSize(1000, 400);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout(5, 1));

        reminderTable = new JTable();
        List<Object[]> list = new ArrayList<>();
        for(Reminder o : reminders) {
            list.add(new Object[] {
                    o.getTitle(),
                    o.getMessage(),
                    o.getDate().toString(),
            });
        }

        @SuppressWarnings("serial")
        TableModel orderModel = new DefaultTableModel(list.toArray(new Object[][] {}), new String[] {"Title", "Message", "Date"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reminderTable.setModel(orderModel);
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(reminderTable.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(reminderTable, BorderLayout.CENTER);

        backButton = new JButton("Go Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToHome();
            }
        });

        fullDetailsButton = new JButton("Full Details");
        fullDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = reminderTable.getSelectedRow();
                if(row >= 0) {
                    Reminder reminder = reminders.get(row);

                    String data = reminder.getTitle() + "\n" + reminder.getMessage() + "\n" + reminder.getDate().toString();
                    JOptionPane.showMessageDialog(getFrame(), data);
                }
                else JOptionPane.showMessageDialog(getFrame(), "Please select a row to see details");

            }
        });

        getFrame().add(tablePanel);
        getFrame().add(fullDetailsButton);
        getFrame().add(backButton);

    }

    @Override
    public void show() {
        if(reminders.size() == 0) {
            JOptionPane.showMessageDialog(null, "You have no reminders at this time");
            returnToHome();
        } else {
            getFrame().setVisible(true);
        }
    }

    @Override
    public void hide() {
        getFrame().setVisible(false);
    }

    private void returnToHome() {
        //TODO: add return home action
        close();
    }
}
