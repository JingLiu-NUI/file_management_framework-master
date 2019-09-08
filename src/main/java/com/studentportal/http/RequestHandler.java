package com.studentportal.http;

public interface RequestHandler {
    public void onSuccess();
    public void onFailure(Exception e);
}
