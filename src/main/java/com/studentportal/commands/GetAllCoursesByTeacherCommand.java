package com.studentportal.commands;

import com.studentportal.courses.Course;
import com.studentportal.hibernate.CourseService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class GetAllCoursesByTeacherCommand implements Command<List<Course>> {
    private int teacherId;
    private CourseService cService;

    public GetAllCoursesByTeacherCommand(CourseService cService, int teacherId) {
        this.teacherId = teacherId;
        this.cService = cService;
    }

    @Override
    public List<Course> execute() {
        return cService.findAllByTeacherId(teacherId);
    }
}
