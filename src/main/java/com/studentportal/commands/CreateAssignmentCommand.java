package com.studentportal.commands;

import com.studentportal.assignments.Assignment;
import com.studentportal.hibernate.AssignmentService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class CreateAssignmentCommand implements Command<Void> {

    private AssignmentService aService;
    private Assignment a;

    public CreateAssignmentCommand(Assignment a, AssignmentService aService) {
        this.a = a;
        this.aService = aService;
    }

    @Override
    public Void execute() {
        Assignment assignment = aService.findByName(a.getName());
        if (assignment == null) {
            aService.save(a);
        } else {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        return null;
    }
}