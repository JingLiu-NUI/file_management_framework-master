package com.studentportal.commands;

import com.studentportal.file_management.Document;
import com.studentportal.hibernate.DocumentService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class DownloadDocumentCommand implements Command<Document> {

    private DocumentService docService = new DocumentService();
    private int docId;

    public DownloadDocumentCommand(int docId) {
        this.docId = docId;
    }

    public Document execute() {
        Document doc = docService.findById(docId);
        if (doc != null) {
            return doc;
        } else {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }
}
