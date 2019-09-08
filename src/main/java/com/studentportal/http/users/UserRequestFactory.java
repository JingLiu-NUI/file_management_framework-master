package com.studentportal.http.users;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestAbstractFactory;

public class UserRequestFactory extends RequestAbstractFactory {
    @Override
    public HttpRequest getRequest() {
        return null;
    }

    @Override
    public HttpRequest getAllRequest() {
        return new GetAllUsersRequest();
    }

    @Override
    public HttpRequest saveRequest() {
        return null;
    }

    @Override
    public HttpRequest updateRequest() {
        return null;
    }

    @Override
    public HttpRequest deleteRequest() {
        return null;
    }

    @Override
    public HttpRequest deleteAllRequest() {
        return null;
    }
}
