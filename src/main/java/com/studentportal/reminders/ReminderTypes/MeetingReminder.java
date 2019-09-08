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
@Table(name = "meeting_reminders")
@PrimaryKeyJoinColumn(name = "reminder_num")
public class MeetingReminder extends Reminder {

    private MeetingReminder() {
        super();
        setDefaultValues();
    }

    public MeetingReminder(MeetingReminderBuilder builder) {
        this.senderType = builder.senderType;
        this.title = builder.title;
        this.message = builder.message;
        this.date = builder.date;
        this.targetUserIds = builder.targetUserIds;
    }

    private void setDefaultValues() {
        date = new Date();
        title = "Meeting Due";
        message = "You have a meeting " + date + ".";
    }

    @Override
    public void send() {
        ReminderSender sender = ReminderSenderFactory.getReminderSenderForType(this.senderType);
        if (sender != null) {
            sender.sendReminder(title, message, targetUserIds);
        } else System.out.print("Sender is null!");

    }

    public static class MeetingReminderBuilder {
        private final SenderType senderType;
        private String title;
        private String message;
        private Date date;
        private List<Integer> targetUserIds;


        public MeetingReminderBuilder(SenderType senderType) {
            this.senderType = senderType;
        }

        public MeetingReminderBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MeetingReminderBuilder message(String message) {
            this.message = message;
            return this;
        }

        public MeetingReminderBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public MeetingReminderBuilder ownerId(List<Integer> targetUserIds) {
            this.targetUserIds = targetUserIds;
            return this;
        }

        public Reminder build() {
            return new MeetingReminder(this);
        }

    }
}
