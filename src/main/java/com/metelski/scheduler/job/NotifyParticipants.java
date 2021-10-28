package com.metelski.scheduler.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.time.LocalTime;

public class NotifyParticipants implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        System.out.println("Start job with key:" + jobKey + ". Notify users. Time: " + LocalTime.now());

    }
}
