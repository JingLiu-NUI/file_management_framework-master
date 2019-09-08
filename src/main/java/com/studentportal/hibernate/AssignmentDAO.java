package com.studentportal.hibernate;

import com.studentportal.assignments.Assignment;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class AssignmentDAO implements GenericDAO<Assignment, Integer> {

    private Session currentSession;
    private Transaction currentTransaction;

    @Override
    public Assignment findById(Integer id) {
        Assignment a  = (Assignment) getCurrentSession().get(Assignment.class, id);
        return a;
    }

    @Override
    public List<Assignment> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Assignment> query = builder.createQuery(Assignment.class);
        Root<Assignment> aRoot = query.from(Assignment.class);
        query.select(aRoot);
        List<Assignment> aList = getCurrentSession().createQuery(query)
                .getResultList();
        return aList;
    }

    @Override
    public void save(Assignment entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Assignment entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void delete(Assignment entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll() {
        List<Assignment> aList = findAll();
        for(Assignment a : aList) {
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
