package com.studentportal.hibernate;

import com.studentportal.assignments.Assignment;
import com.studentportal.user.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAO implements GenericDAO<User,Integer> {
    private Session currentSession;
    private Transaction currentTransaction;

    @Override
    public User findById(Integer id) {
        User u  = getCurrentSession().get(User.class, id);
        return u;
    }

    @Override
    public List<User> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> uRoot = query.from(User.class);
        query.select(uRoot);
        List<User> uList = getCurrentSession().createQuery(query)
                .getResultList();
        return uList;
    }

    @Override
    public void save(User entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(User entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void delete(User entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll() {
        List<User> uList = findAll();
        for(User u : uList) {
            delete(u);
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
