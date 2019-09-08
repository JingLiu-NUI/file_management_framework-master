package com.studentportal.hibernate;

import com.studentportal.courses.Course;
import com.studentportal.user.User;

import java.util.ArrayList;
import java.util.List;

public class CourseService {

    private CourseDAO dao;

    public CourseService() {
        this.dao = new CourseDAO();
    }

    public Course findByCode(String courseCode) {
        List<Course> cList = findAll();
        for (Course c : cList) {
            if (c.getCourseCode().equals(courseCode)) {
                return c;
            }
        }
        return null;
    }

    public List<Course> findAllByTeacherId(int teacherId) {
        List<Course> allCourses = findAll();
        List<Course> allCourseByTeacherId = new ArrayList<>();

        for (Course c : allCourses) {
            if (c.getTeacherId() == teacherId) {
                allCourseByTeacherId.add(c);
            }
        }
        return allCourseByTeacherId;
    }

    public Course findById(int id) {
        dao.openCurrentSession();
        Course c = dao.findById(id);
        dao.closeCurrentSession();
        return c;
    }

    public List<Course> findAll() {
        dao.openCurrentSession();
        List<Course> cList = dao.findAll();
        dao.closeCurrentSession();
        return cList;
    }

    public void save(Course c) {
        dao.openCurrentSessionWithTransaction();
        dao.save(c);
        dao.closeCurrentSessionWithTransaction();
    }

    public void update(Course c) {
        dao.openCurrentSessionWithTransaction();
        dao.update(c);
        dao.closeCurrentSessionWithTransaction();
    }

    public void delete(int id) {
        dao.openCurrentSessionWithTransaction();
        Course c = dao.findById(id);
        dao.delete(c);
        dao.closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        dao.openCurrentSessionWithTransaction();
        dao.deleteAll();
        dao.closeCurrentSessionWithTransaction();
    }

    public CourseDAO getDAO() {
        return dao;
    }
}
