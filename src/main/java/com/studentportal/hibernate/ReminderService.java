package com.studentportal.hibernate;

import com.studentportal.reminders.ReminderTypes.Reminder;

import java.util.ArrayList;
import java.util.List;

public class ReminderService {
    private ReminderDAO dao;

    public ReminderService() {
        dao = new ReminderDAO();
    }

    public List<Reminder> findRemindersByUserId(int ownerId) {
        List<Reminder> rList = findAll();
        ArrayList<Reminder> rListByUserId = new ArrayList<>();
        for(Reminder reminder : rList) {
            for(int userId : reminder.getTargetUserIds()) {
                if(userId == ownerId)
                    rListByUserId.add(reminder);
            }
        }
        return rList;
    }

    public List<Reminder> findByTitle(String title) {
        List<Reminder> rList = findAll();
        ArrayList<Reminder> rListByTitle = new ArrayList<>();
        for (Reminder reminder : rList) {
            if (reminder.getTitle().equals(title)) {
                rListByTitle.add(reminder);
            }
        }
        return rListByTitle;
    }

    public List<Reminder> findByMessage(String message) {
        List<Reminder> rList = findAll();
        ArrayList<Reminder> rListByMessage = new ArrayList<>();
        for (Reminder reminder : rList) {
            if (reminder.getMessage().equals(message)) {
                rListByMessage.add(reminder);
            }
        }
        return rListByMessage;
    }

    public Reminder findById(int id) {
        dao.openCurrentSession();
        Reminder r = dao.findById(id);
        dao.closeCurrentSession();
        return r;
    }

    public List<Reminder> findAll() {
        dao.openCurrentSession();
        List<Reminder> rList = dao.findAll();
        dao.closeCurrentSession();
        return rList;
    }

    public void save(Reminder r) {
        dao.openCurrentSessionWithTransaction();
        dao.save(r);
        dao.closeCurrentSessionWithTransaction();
    }

    public void update(Reminder r) {
        dao.openCurrentSessionWithTransaction();
        dao.update(r);
        dao.closeCurrentSessionWithTransaction();
    }

    public void delete(int id) {
        dao.openCurrentSessionWithTransaction();
        Reminder r = dao.findById(id);
        dao.delete(r);
        dao.closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        dao.openCurrentSessionWithTransaction();
        dao.deleteAll();
        dao.closeCurrentSessionWithTransaction();
    }

    public ReminderDAO getDAO() {
        return dao;
    }
}
