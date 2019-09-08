package com.studentportal.reminders.ReminderTypes;

import com.studentportal.reminders.Senders.ReminderSender;
import com.studentportal.reminders.Senders.ReminderSenderFactory;
import com.studentportal.reminders.Senders.SenderType;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "assignment_reminders")
@PrimaryKeyJoinColumn(name = "reminder_num")
public class AssignmentReminder extends Reminder {

    private AssignmentReminder() {
        super();
        setDefaultValues();
    }

    public AssignmentReminder(AssignmentReminderBuilder builder) {
        this.senderType = builder.senderType;
        this.title = builder.title;
        this.message = builder.message;
        this.date = builder.date;
        this.targetUserIds = builder.targetUserIds;
    }

    private AssignmentReminder(SenderType senderType) {
        super();
        this.senderType = senderType;
        setDefaultValues();
    }

    private void setDefaultValues() {
        date = new Date();
        title = "Assignment Due";
        message = "You have an assignment due!";
    }

    @Override
    public void send() {
        ReminderSender sender = ReminderSenderFactory.getReminderSenderForType(this.senderType);
        if (sender != null) {
            sender.sendReminder(title, message, targetUserIds);
        } else System.out.print("Sender is null!");
    }

    public static class AssignmentReminderBuilder {
        private final SenderType senderType;
        private String title;
        private String message;
        private Date date;
        private List<Integer> targetUserIds;

        public AssignmentReminderBuilder(SenderType senderType) {
            this.senderType = senderType;
        }

        public AssignmentReminderBuilder title(String title) {
            this.title = title;
            return this;
        }

        public AssignmentReminderBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AssignmentReminderBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public AssignmentReminderBuilder targetUserIds(List<Integer> targetUserIds) {
            this.targetUserIds = targetUserIds;
            return this;
        }

        public AssignmentReminder build() {
            if (this.title == null)
                title = "Assignment Due";
            if (this.message == null)
                message = "You have an assignment due today!";
            if (this.date == null) {
                date = new Date();
            }
            return new AssignmentReminder(this);
        }

    }
}
