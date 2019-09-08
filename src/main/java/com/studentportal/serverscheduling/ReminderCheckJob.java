package com.studentportal.serverscheduling;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.studentportal.hibernate.ReminderService;
import com.studentportal.reminders.ReminderTypes.Reminder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReminderCheckJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        ReminderService reminderService = new ReminderService();
        List<Reminder> reminderList = reminderService.findAll();

        for(Reminder reminder : reminderList) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();

            cal1.setTime(reminder.getDate());
            cal2.setTime(context.getFireTime());

            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

            if(sameDay) {
                reminder.send();
            }
        }
    }
}
