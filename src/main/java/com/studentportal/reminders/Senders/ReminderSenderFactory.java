package com.studentportal.reminders.Senders;

public class ReminderSenderFactory {

    public static ReminderSender getReminderSenderForType(SenderType senderType) {
        switch (senderType) {
            case EMAIL:
                return new EmailReminderSender();
            case PORTAL:
                return new SMSReminderSender();
            case SMS:
                return new PortalReminderSender();
            default:
                return null;
        }
    }
}
