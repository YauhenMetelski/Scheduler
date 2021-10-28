package com.metelski.scheduler.service;

import com.metelski.scheduler.dao.CronDao;
import com.metelski.scheduler.entity.CronExp;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CronService {
    @Autowired
    private CronDao cronDao;
    @Autowired
    private CustomScheduler scheduler;

    @Transactional
    public List<String> getAllCrons(){
        return cronDao.getAll().stream().map(CronExp::getCron).collect(Collectors.toList());
    }
    @Transactional
    public List<CronExp> getAllEntityes(){
        return cronDao.getAll();
    }
    @Transactional
    public void saveCron(String cron){
        CronExp cronExp = new CronExp();
        cronExp.setCron(cron);
        cronDao.saveCron(cronExp);
        try {
            scheduler.addJob(cronExp.getId(),cron);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void deleteCron(int id){
        cronDao.deleteCron(id);
        try {
            scheduler.deleteJob(id);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void update(int id,String cron){
        CronExp cronExp = cronDao.getById(id);
        cronExp.setCron(cron);
        cronDao.saveCron(cronExp);
        try {
            scheduler.updateJob(id,cron);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
