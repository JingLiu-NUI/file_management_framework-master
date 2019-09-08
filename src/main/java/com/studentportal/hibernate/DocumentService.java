package com.studentportal.hibernate;

import com.studentportal.file_management.Document;

import java.util.List;

public class DocumentService {

    private static DocumentDAO dao;

    public DocumentService() {
        dao = new DocumentDAO();
    }

    public Document findByName(String name) {
        List<Document> docs = findAll();
        Document doc = null;
        for (Document d : docs) {
            if (d.getFileName().equals(name)) {
                doc = d;
            }
        }
        return doc;
    }

    public Document findById(int id) {
        dao.openCurrentSession();
        Document doc = dao.findById(id);
        dao.closeCurrentSession();
        return doc;
    }

    public List<Document> findAll() {
        dao.openCurrentSession();
        List<Document> docs = dao.findAll();
        dao.closeCurrentSession();
        return docs;
    }

    public void save(Document doc) {
        dao.openCurrentSessionWithTransaction();
        dao.save(doc);
        dao.closeCurrentSessionWithTransaction();
    }

    public void update(Document doc) {
        dao.openCurrentSessionWithTransaction();
        dao.update(doc);
        dao.closeCurrentSessionWithTransaction();
    }

    public void delete(int id) {
        dao.openCurrentSessionWithTransaction();
        Document doc = dao.findById(id);
        dao.delete(doc);
        dao.closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        dao.openCurrentSessionWithTransaction();
        dao.deleteAll();
        dao.closeCurrentSessionWithTransaction();
    }

    public DocumentDAO getDAO() {
        return dao;
    }
}
