package com.studentportal.user;

public class TeacherFactory implements UserFactory<Teacher> {

    @Override
    public Teacher createUser() {
        return new Teacher();
    }
}
