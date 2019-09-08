package com.studentportal.hibernate;

import com.studentportal.announcement.Announcement;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementService {
    private AnnouncementDAO dao;

    public AnnouncementService() {
        this.dao = new AnnouncementDAO();
    }

    public Announcement findById(int id) {
        dao.openCurrentSession();
        Announcement a = dao.findById(id);
        dao.closeCurrentSession();
        return a;
    }

    public List<Announcement> findAllByCourseId(int courseId) {
        List<Announcement> aList = new ArrayList<>();
        for (Announcement a : findAll()) {
            if (a.getCourse().getId() == courseId) {
                aList.add(a);
            }
        }
        return aList;
    }

    public List<Announcement> findAll() {
        dao.openCurrentSession();
        List<Announcement> aList = dao.findAll();
        dao.closeCurrentSession();
        return aList;
    }

    public void save(Announcement a) {
        dao.openCurrentSessionWithTransaction();
        dao.save(a);
        dao.closeCurrentSessionWithTransaction();
    }

    public void update(Announcement a) {
        dao.openCurrentSessionWithTransaction();
        dao.update(a);
        dao.closeCurrentSessionWithTransaction();
    }

    public void delete(int id) {
        dao.openCurrentSessionWithTransaction();
        Announcement a = dao.findById(id);
        dao.delete(a);
        dao.closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        dao.openCurrentSessionWithTransaction();
        dao.deleteAll();
        dao.closeCurrentSessionWithTransaction();
    }

    public AnnouncementDAO getDAO() {
        return dao;
    }


}
