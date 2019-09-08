package com.studentportal.reminders.Senders;

import java.util.List;


public interface ReminderSender {
    public void sendReminder(String title, String body, List<Integer> targetUserIds);
}
