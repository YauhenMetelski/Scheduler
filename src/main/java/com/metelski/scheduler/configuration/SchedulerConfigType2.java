package com.metelski.scheduler.configuration;

import com.metelski.scheduler.entity.CronExp;
import com.metelski.scheduler.service.CronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

//@Configuration
//@EnableScheduling
public class SchedulerConfigType2 implements SchedulingConfigurer {
    private List<String> cronExpressions;
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    @Autowired
    private CronService service;
    @Value("${cron.schedule.checking.db: 0/10 * * * * *}")
    private String startExpression;
    private Map<Integer,CronTask> tasksMap = new HashMap<>();
//    private List
    private int idForStartTask=0;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        System.out.println("Start  SchedulerConfigType2 ");
        cronExpressions = service.getAllCrons();
        List<CronExp> exp = service.getAllEntityes();
        System.out.println("First trigger was added");
        Runnable runnable = () -> {System.out.println("First task check DB "
                + LocalTime.now() + " START expression: " + startExpression);
            CronTask taskFromMap = tasksMap.get(18);
            System.out.println(taskFromMap.getExpression() + " Cron expression from task db");
//            if(!taskFromMap.getExpression().equals(exp.get(0))){
//                taskFromMap
//            }

        };
        CronTask firstTask = new CronTask(runnable, startExpression);
        tasksMap.put(idForStartTask, firstTask);
        taskRegistrar.addCronTask(firstTask);
        //TODO first task check db and change if it need tasks list

        System.out.println("CRONS: " + cronExpressions);
//        cronExpressions.stream().forEach(cronExpression -> {
        exp.stream().forEach(expEntity ->{
            Runnable mainTask = () -> System.out.println("Triggered task " + LocalTime.now()
                    + " cron expression: " + expEntity.getCron());
            CronTask task = new CronTask(mainTask, expEntity.getCron());
            tasksMap.put(expEntity.getId(),task);
            taskRegistrar.addCronTask(task);
        });
        printTasks(taskRegistrar,tasksMap);
    }

    private boolean taskRegistrator(ScheduledTaskRegistrar taskRegistrar) {
        return true;
    }
    private void printTasks(ScheduledTaskRegistrar taskRegistrar,Map<Integer,CronTask> map) {
        System.out.println("+++++++++++LIST++++++++++++");
        List<CronTask> cronTasks = taskRegistrar.getCronTaskList();
        cronTasks.forEach(System.out::println);
        System.out.println("+++++++++++MAP++++++++++++");
        for (Map.Entry<Integer,CronTask> entry : map.entrySet()) {
            System.out.println("key: " + entry.getKey()+ " value: " + entry.getValue());
        }

    }
}
