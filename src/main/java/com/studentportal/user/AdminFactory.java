package com.studentportal.user;

public class AdminFactory implements UserFactory<Admin> {
    @Override
    public Admin createUser() {
        return new Admin();
    }
}
