package com.studentportal.serverscheduling;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class JobScheduler {

    public static void scheduleTasks() {

        JobDetail checkReminderDateJob = JobBuilder.newJob(ReminderCheckJob.class)
                .withIdentity("dateCheckJob", "group1").build();

        Trigger nineAMTrigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger1", "group1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(9, 0))
                .build();

        JobDetail testJob = JobBuilder.newJob(TestJob.class)
                .withIdentity("testJob", "group1").build();

        Trigger fiveSecondTrigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger2", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();


        try {
            Scheduler jobScheduler = new StdSchedulerFactory().getScheduler();
            jobScheduler.start();
            jobScheduler.scheduleJob(checkReminderDateJob, nineAMTrigger);
//            jobScheduler.scheduleJob(testJob, fiveSecondTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }
}
