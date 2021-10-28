package com.metelski.scheduler.service;

import com.metelski.scheduler.configuration.SchedulerConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
//    @Scheduled(fixedRate = 10000)
    public void checkConfig(){
//        System.out.println("check config");
//        SchedulerConfig config = new SchedulerConfig();
//        config.configureTasks(new ScheduledTaskRegistrar());
    }
}
