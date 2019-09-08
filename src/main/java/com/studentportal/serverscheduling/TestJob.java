package com.studentportal.serverscheduling;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Job Executed at : " + jobExecutionContext.getFireTime().toString() + " !");
    }
}
