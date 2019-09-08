package com.studentportal.commands;

public interface Command<T> {
    public T execute();
}
