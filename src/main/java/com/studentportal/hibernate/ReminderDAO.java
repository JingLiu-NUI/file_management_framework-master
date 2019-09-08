package com.studentportal.hibernate;

import com.studentportal.reminders.ReminderTypes.Reminder;
import com.studentportal.user.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class ReminderDAO implements GenericDAO<Reminder,Integer> {
    private Session currentSession;
    private Transaction currentTransaction;

    @Override
    public Reminder findById(Integer id) {
        Reminder r  = getCurrentSession().get(Reminder.class, id);
        return r;
    }

    @Override
    public List<Reminder> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Reminder> query = builder.createQuery(Reminder.class);
        Root<Reminder> rRoot = query.from(Reminder.class);
        query.select(rRoot);
        List<Reminder> rList = getCurrentSession().createQuery(query)
                .getResultList();
        return rList;
    }

    @Override
    public void save(Reminder entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Reminder entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void delete(Reminder entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll() {
        List<Reminder> rList = findAll();
        for(Reminder r : rList) {
            delete(r);
        }
    }

    public Session openCurrentSession() {
        currentSession = HibernateConfig.getSessionFactory()
                .openSession();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public Session openCurrentSessionWithTransaction() {
        currentSession = HibernateConfig.getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session session) {
        this.currentSession = session;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction transaction) {
        this.currentTransaction = transaction;
    }
}
