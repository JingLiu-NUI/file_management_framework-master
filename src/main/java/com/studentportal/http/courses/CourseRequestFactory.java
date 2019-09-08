package com.studentportal.http.courses;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestAbstractFactory;

public class CourseRequestFactory extends RequestAbstractFactory {
    @Override
    public HttpRequest getRequest() {
        return null;
    }

    @Override
    public HttpRequest getAllRequest() {
        return null;
    }

    public HttpRequest getAllByTeacher() {
        return new GetAllCoursesByTeacherRequest();
    }

    @Override
    public HttpRequest saveRequest() {
        return new SaveCourseRequest();
    }

    @Override
    public HttpRequest updateRequest() {
        return new UpdateCourseRequest();
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
