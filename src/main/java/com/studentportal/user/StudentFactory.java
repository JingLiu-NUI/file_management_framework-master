package com.studentportal.user;

public class StudentFactory implements UserFactory<Student> {

    @Override
    public Student createUser() {
        return new Student();
    }
}
