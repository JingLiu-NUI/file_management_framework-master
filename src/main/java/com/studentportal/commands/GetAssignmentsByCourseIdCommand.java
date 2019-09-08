package com.studentportal.commands;

import com.studentportal.assignments.Assignment;
import com.studentportal.hibernate.AssignmentService;

import java.util.List;

public class GetAssignmentsByCourseIdCommand implements Command<List<Assignment>> {

    private AssignmentService aService;
    private int courseId;

    public GetAssignmentsByCourseIdCommand(AssignmentService aService, int courseId) {
        this.aService = aService;
        this.courseId = courseId;
    }

    @Override
    public List<Assignment> execute() {
        return aService.findAllByCourseId(courseId);
    }
}
