package com.studentportal.hibernate;

import com.studentportal.assignments.Assignment;

import java.util.ArrayList;
import java.util.List;

public class AssignmentService {

    private AssignmentDAO dao;

    public AssignmentService() {
        this.dao = new AssignmentDAO();
    }

    public Assignment findByName(String name) {
        List<Assignment> aList = findAll();
        Assignment assignment = null;
        for (Assignment a : aList) {
            if (a.getName().equals(name)) {
                assignment = a;
            }
        }
        return assignment;
    }

    public List<Assignment> findAllByCourseId(int id) {
        List<Assignment> aList = new ArrayList<>();
        for (Assignment a : findAll()) {
            if (a.getCourseId() == id) {
                aList.add(a);
            }
        }
        return aList;
    }

    public Assignment findById(int id) {
        dao.openCurrentSession();
        Assignment a = dao.findById(id);
        dao.closeCurrentSession();
        return a;
    }

    public List<Assignment> findAll() {
        dao.openCurrentSession();
        List<Assignment> aList = dao.findAll();
        dao.closeCurrentSession();
        return aList;
    }

    public void save(Assignment a) {
        dao.openCurrentSessionWithTransaction();
        dao.save(a);
        dao.closeCurrentSessionWithTransaction();
    }

    public void update(Assignment a) {
        dao.openCurrentSessionWithTransaction();
        dao.update(a);
        dao.closeCurrentSessionWithTransaction();
    }

    public void delete(int id) {
        dao.openCurrentSessionWithTransaction();
        Assignment a = dao.findById(id);
        dao.delete(a);
        dao.closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        dao.openCurrentSessionWithTransaction();
        dao.deleteAll();
        dao.closeCurrentSessionWithTransaction();
    }

    public AssignmentDAO getDAO() {
        return dao;
    }
}
