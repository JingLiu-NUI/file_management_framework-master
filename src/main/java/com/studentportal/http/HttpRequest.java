package com.studentportal.http;

public interface HttpRequest<T, E> {
    public T makeRequest(RequestHandler callback, E element);
}
