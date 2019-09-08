package com.studentportal.hibernate;

import com.studentportal.announcement.Announcement;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


public class AnnouncementDAO implements GenericDAO<Announcement, Integer> {

    private Session currentSession;
    private Transaction currentTransaction;

    @Override
    public Announcement findById(Integer id) {
        Announcement a = (Announcement) getCurrentSession().get(Announcement.class, id);
        return a;
    }

    @Override
    public List<Announcement> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Announcement> query = builder.createQuery(Announcement.class);
        Root<Announcement> aRoot = query.from(Announcement.class);
        query.select(aRoot);
        List<Announcement> aList = getCurrentSession().createQuery(query)
                .getResultList();
        return aList;
    }

    @Override
    public void save(Announcement entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Announcement entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void delete(Announcement entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll() {
        List<Announcement> aList = findAll();
        for (Announcement a : aList) {
            delete(a);
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

