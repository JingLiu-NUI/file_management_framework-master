package com.studentportal.user;

public interface UserFactory<T> {
    T createUser();
}