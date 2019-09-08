package com.studentportal.http;

public abstract class RequestAbstractFactory {
    public abstract HttpRequest getRequest();
    public abstract HttpRequest getAllRequest();
    public abstract HttpRequest saveRequest();
    public abstract HttpRequest updateRequest();
    public abstract HttpRequest deleteRequest();
    public abstract HttpRequest deleteAllRequest();
}
