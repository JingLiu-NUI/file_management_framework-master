package com.studentportal.hibernate;

import com.studentportal.user.User;
import com.studentportal.user.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserDAO dao;

    public UserService() {
        dao = new UserDAO();
    }

    public User findByEmail(String email) {
        List<User> uList = findAll();
        for (User user : uList) {
            if (user.getUserEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    public List<User> findByRole(UserRole userRole) {
        List<User> uList = findAll();
        List<User> uListByRole = new ArrayList<>();
        for (User u : uList) {
            if (u.getUserRole().equals(userRole)) {
                uListByRole.add(u);
            }
        }
        return uListByRole;
    }

    public User findById(int id) {
        dao.openCurrentSession();
        User u = dao.findById(id);
        dao.closeCurrentSession();
        return u;
    }

    public List<User> findAll() {
        dao.openCurrentSession();
        List<User> uList = dao.findAll();
        dao.closeCurrentSession();
        return uList;
    }

    public void save(User u) {
        dao.openCurrentSessionWithTransaction();
        dao.save(u);
        dao.closeCurrentSessionWithTransaction();
    }

    public void update(User u) {
        dao.openCurrentSessionWithTransaction();
        dao.update(u);
        dao.closeCurrentSessionWithTransaction();
    }

    public void delete(int id) {
        dao.openCurrentSessionWithTransaction();
        User u = dao.findById(id);
        dao.delete(u);
        dao.closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        dao.openCurrentSessionWithTransaction();
        dao.deleteAll();
        dao.closeCurrentSessionWithTransaction();
    }

    public UserDAO getDAO() {
        return dao;
    }
}
