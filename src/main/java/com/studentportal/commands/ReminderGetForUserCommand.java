package com.studentportal.commands;


import com.studentportal.hibernate.ReminderService;
import com.studentportal.reminders.ReminderTypes.Reminder;

import java.util.List;

public class ReminderGetForUserCommand implements Command<List<Reminder>> {
    private ReminderService rService;
    private int userId;

    public ReminderGetForUserCommand(ReminderService rService, int userId) {
        this.rService = rService;
        this.userId = userId;
    }

    @Override
    public List<Reminder> execute() {
        return rService.findRemindersByUserId(userId);
    }
}
