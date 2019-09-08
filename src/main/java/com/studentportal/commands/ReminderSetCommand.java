package com.studentportal.commands;


import com.studentportal.hibernate.ReminderService;
import com.studentportal.reminders.ReminderTypes.Reminder;


public class ReminderSetCommand implements Command<Void>{
    private ReminderService rService;
    private Reminder reminder;

    public ReminderSetCommand(ReminderService rService, Reminder reminder) {
        this.rService = rService;
        this.reminder = reminder;
    }
    @Override
    public Void execute() {
        rService.save(reminder);
        return null;
    }
}
