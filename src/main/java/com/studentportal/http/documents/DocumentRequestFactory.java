package com.studentportal.http.documents;

import com.studentportal.http.HttpRequest;
import com.studentportal.http.RequestAbstractFactory;

public class DocumentRequestFactory extends RequestAbstractFactory {

    @Override
    public HttpRequest getRequest() {
        return new GetDocumentRequest();
    }

    @Override
    public HttpRequest getAllRequest() {
        return new GetAllDocumentsRequest();
    }

    @Override
    public HttpRequest saveRequest() {
        return new SaveDocumentRequest();
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
