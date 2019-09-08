package com.studentportal.commands;

import com.studentportal.file_management.Document;
import com.studentportal.file_management.StudentProjectDocument;
import com.studentportal.hibernate.DocumentService;
import com.studentportal.hibernate.UserService;
import com.studentportal.user.Student;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SaveProjectDocumentCommand implements Command<Void> {

    private DocumentService docService;
    private UserService uService;
    private StudentProjectDocument spd;

    public SaveProjectDocumentCommand(DocumentService docService,
            UserService uService, StudentProjectDocument spd) {
        this.docService = docService;
        this.uService = uService;
        this.spd = spd;
    }

    @Override
    public Void execute() {
        Document d = docService.findByName(spd.getFileName());
        if (d == null) {
            docService.save(spd);
            Document savedProject = docService.findByName(spd.getFileName());
            Student student = (Student) uService.findById(spd.getStudentId());
            student.addProjectDocumentId(savedProject.getId());
            uService.update(student);
        } else {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        return null;
    }
}
