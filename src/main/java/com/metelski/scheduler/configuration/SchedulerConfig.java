package com.metelski.scheduler.configuration;

import com.metelski.scheduler.dao.CronDao;
import com.metelski.scheduler.service.CronService;
import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//@Configuration
//@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {
    private List<String> cronExpressions;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    @Autowired
    private CronService service;
    @Value("${cron.schedule.checking.db}")
    private String startExpression;
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        System.out.println("Start  configureTasks");
        cronExpressions = service.getAllCrons();
            System.out.println("First trigger was added");
            Runnable runnable = () -> System.out.println("Triggered task " + LocalTime.now() + " START expression: " + startExpression);
            Trigger trigger = new Trigger() {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    List<String> newExpressions = service.getAllCrons();
                    Collections.sort(cronExpressions);
                    Collections.sort(newExpressions);
                    if (!newExpressions.equals(cronExpressions)) {
                        System.out.println("Inside if");
                        taskRegistrar.setTriggerTasksList(new ArrayList<>());
                        configureTasks(taskRegistrar); // calling recursively.
                        taskRegistrar.destroy(); // destroys previously scheduled tasks.
                        taskRegistrar.setScheduler(executor);
                        taskRegistrar.afterPropertiesSet(); // this will schedule the task with new cron changes.
                        return null; // return null when the cron changed so the trigger will stop.
                    }
                    CronTrigger cronTrigger = new CronTrigger(startExpression);
                    return cronTrigger.nextExecutionTime(triggerContext);
                }
            };
            taskRegistrar.addTriggerTask(runnable, trigger);

        System.out.println("CRONS: " + cronExpressions);
        cronExpressions.stream().forEach(cronExpression -> {
            Runnable runnable2 = () -> System.out.println("Triggered task " + LocalTime.now() + " cron expression: " + cronExpression);
            Trigger trigger2 = new Trigger() {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    CronTrigger cronTrigger = new CronTrigger(cronExpression);
                    return cronTrigger.nextExecutionTime(triggerContext);
                }
            };
            taskRegistrar.addTriggerTask(runnable2, trigger2);
        });
    }
}
