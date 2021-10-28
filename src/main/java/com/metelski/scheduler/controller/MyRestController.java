package com.metelski.scheduler.controller;

import com.metelski.scheduler.entity.CronExp;
import com.metelski.scheduler.service.CronService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class MyRestController {
    @Autowired
    private CronService service;

    @GetMapping("crons")
    public List<String> showAllCrons(){
        return service.getAllCrons();
    }
    @PostMapping("crons")
    public void addCron(@RequestBody String cron){
        service.saveCron(cron);
    }
    @GetMapping("crons/ent")
    public List<CronExp> showAllEntities(){
        return service.getAllEntityes();
    }
    @PutMapping("crons/{id}")
    public void update(@PathVariable int id){
        service.update(id);
    }
    @DeleteMapping("crons/{id}")
    public void deleteCron(@PathVariable int id){
        service.deleteCron(id);
    }

    //    "0/30 * * * * *",
    //    "0 06 11 * * *",
    //    "15 06 11 * * 1"
}
