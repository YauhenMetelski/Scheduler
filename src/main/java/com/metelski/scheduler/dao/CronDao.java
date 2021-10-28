package com.metelski.scheduler.dao;

import com.metelski.scheduler.entity.CronExp;

import java.util.List;

public interface CronDao {
    public List<CronExp> getAll();
    public void saveCron(CronExp cronExp);
    public void deleteCron(int id);
    public CronExp getById(int id);
}
