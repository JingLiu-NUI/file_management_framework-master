package com.studentportal.hibernate;

import com.studentportal.file_management.Document;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DocumentDAO implements GenericDAO<Document, Integer> {

    private Session currentSession;
    private Transaction currentTransaction;

    public DocumentDAO() {
    }

    @Override
    public Document findById(Integer id) {
        Document doc = getCurrentSession().get(Document.class, id);
        return doc;
    }

    @Override
    public List<Document> findAll() {
        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Document> query = builder.createQuery(Document.class);
        Root<Document> docRoot = query.from(Document.class);
        query.select(docRoot);
        List<Document> docs = getCurrentSession().createQuery(query)
                .getResultList();
        return docs;
    }

    @Override
    public void save(Document entity) {
        getCurrentSession().save(entity);
    }

    @Override
    public void update(Document entity) {
        getCurrentSession().update(entity);
    }

    @Override
    public void delete(Document entity) {
        getCurrentSession().delete(entity);
    }

    @Override
    public void deleteAll() {
        List<Document> docs = findAll();
        for (Document doc : docs) {
            delete(doc);
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
