package com.studentportal.http.assignments;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestAbstractFactory;

public class ProjectRequestFactory extends RequestAbstractFactory {
    @Override
    public HttpRequest getRequest() {
        return null;
    }

    @Override
    public HttpRequest getAllRequest() {
        return null;
    }

    public HttpRequest getAllByCourseId() {
        return new GetAssignmentsByCourseIdRequest();
    }

    @Override
    public HttpRequest saveRequest() {
        return new SaveProjectRequest();
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
