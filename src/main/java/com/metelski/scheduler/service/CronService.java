package com.metelski.scheduler.service;

import com.metelski.scheduler.dao.CronDao;
import com.metelski.scheduler.entity.CronExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CronService {
    @Autowired
    private CronDao cronDao;

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
    }
    @Transactional
    public void deleteCron(int id){
        cronDao.deleteCron(id);
    }
    @Transactional
    public void update(int id){
        CronExp cronExp = cronDao.getById(id);
        cronExp.setCron("0/13 * * * * *");//Update in db
        cronDao.saveCron(cronExp);
    }
}
