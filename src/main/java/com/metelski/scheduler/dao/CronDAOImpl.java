package com.metelski.scheduler.dao;

import com.metelski.scheduler.entity.CronExp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CronDAOImpl implements CronDao{
    @Autowired
    private EntityManager entityManager;
    @Override
    public List<CronExp> getAll() {
        Query query = entityManager.createQuery("from CronExp");
        return (List<CronExp>) query.getResultList();
    }

    @Override
    public void saveCron(CronExp cronExp) {
            CronExp expr = entityManager.merge(cronExp);
            cronExp.setId(expr.getId());
    }

    @Override
    public void deleteCron(int id) {
          Query query = entityManager.createQuery("delete from CronExp where id=:cronId");
          query.setParameter("cronId",id);
          query.executeUpdate();
    }

    @Override
    public CronExp getById(int id) {
  return entityManager.find(CronExp.class,id);
    }
}
