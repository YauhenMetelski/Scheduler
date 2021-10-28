package com.metelski.scheduler.service;

import com.metelski.scheduler.dao.CronDao;
import com.metelski.scheduler.entity.CronExp;
import com.metelski.scheduler.job.TransferLog;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class CustomScheduler {
    @Autowired
    private CronService cronService;
    private SchedulerFactory schedulerFactory;
    private Scheduler scheduler;

    @PostConstruct
    public void init() throws SchedulerException {
        schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        start();
    }

    public void start() throws SchedulerException {
        List<CronExp> cronExpList = cronService.getAllEntityes();
        System.out.println(cronExpList);
        cronExpList.forEach(cronExp -> {
            String id = Integer.toString(cronExp.getId());
            JobDetail job = newJob(TransferLog.class)
                    .withIdentity(id)
                    .build();
            CronTrigger trigger = newTrigger()
                    .withIdentity(id)
                    .withSchedule(cronSchedule(cronExp.getCron()))
                    .build();
            System.out.println("Working with task from db. Id:" + id +" cron expression: " + cronExp.getCron());
            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        scheduler.start();
    }

    public void addJob(int idFront, String cron) throws SchedulerException {
        String id = Integer.toString(idFront);
        JobDetail job = newJob(TransferLog.class)
                .withIdentity(id)
                .build();
        CronTrigger trigger = newTrigger()
                .withIdentity(id)
                .withSchedule(cronSchedule(cron))
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public void deleteJob(int idFront) throws SchedulerException {
        String id = Integer.toString(idFront);
        JobKey jobKey = new JobKey(id);
        scheduler.deleteJob(jobKey);
    }

    public void updateJob(int idFront, String cron) throws SchedulerException {
        String id = Integer.toString(idFront);
        JobKey jobKey = new JobKey(id);
        JobDetail job = scheduler.getJobDetail(jobKey);
        TriggerKey triggerKey = new TriggerKey(id);
        System.out.println("Now: "+ LocalTime.now() +"Next fire: " +scheduler.getTrigger(triggerKey).getNextFireTime());
        scheduler.deleteJob(jobKey);
        CronTrigger trigger = newTrigger()
                .withIdentity(id)
                .withSchedule(cronSchedule(cron))
                .build();
        scheduler.scheduleJob(job, trigger);
        System.out.println("Now: "+ LocalTime.now() +"Next fire: " +scheduler.getTrigger(triggerKey).getNextFireTime());
    }
}
