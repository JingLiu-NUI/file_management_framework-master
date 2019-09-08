package com.studentportal.hibernate;

import com.studentportal.courses.Course;
import com.studentportal.user.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CourseDAO implements GenericDAO<Course, Integer> {

    private Session currentSession;
    private Transaction currentTransaction;

    @Override
    public Course findById(Integer id) {
        Course c = getCurrentSession().get(Course.class, id);
        return c;
    }

    @Override
    public List<Course> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        Root<Course> cRoot = query.from(Course.class);
        query.select(cRoot);
        List<Course> cList = getCurrentSession().createQuery(query)
                .getResultList();
        return cList;
    }

    @Override
    public void save(Course entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Course entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void delete(Course entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll() {
        List<Course> cList = findAll();
        for(Course c : cList) {
            delete(c);
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
