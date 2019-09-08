package com.studentportal.commands;

import com.studentportal.file_management.Document;
import com.studentportal.hibernate.DocumentService;

import java.util.List;

public class GetAllDocumentsCommand implements Command<List<Document>> {

    private DocumentService docService;

    public GetAllDocumentsCommand(DocumentService docService) {
        this.docService = docService;
    }
    
    @Override
    public List<Document> execute() {
        return docService.findAll();
    }
}
