package com.studentportal.commands;

import com.studentportal.file_management.Document;
import com.studentportal.hibernate.DocumentService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UploadDocumentCommand implements Command<Void> {
    private DocumentService docService;
    private Document doc;

    public UploadDocumentCommand(Document doc, DocumentService docService) {
        this.doc = doc;
        this.docService = docService;
    }

    @Override
    public Void execute() {
        Document d = docService.findByName(doc.getFileName());
        if (d == null) {
            docService.save(doc);
        } else {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        return null;
    }
}
