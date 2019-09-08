package com.studentportal.commands;

import com.studentportal.courses.Course;
import com.studentportal.courses.UpdateOperation;
import com.studentportal.hibernate.CourseService;
import com.studentportal.hibernate.UserService;
import com.studentportal.user.Teacher;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

public class SaveCourseCommand implements Command<Void> {
    private Course course;
    private CourseService cService;
    private UserService uService;

    public SaveCourseCommand(CourseService cService, UserService uService,
                             Course course) {
        this.course = course;
        this.cService = cService;
        this.uService = uService;
    }

    @Override
    public Void execute() {
        String code = course.getCourseCode();
        int teacherId = course.getTeacherId();
        Course c = cService.findByCode(code);
        if (c == null) {
            cService.save(course);
            c = cService.findByCode(code);
            Teacher t = (Teacher) uService.findById(teacherId);
            // invoke the method in UpdateOperation(Itarget) interface.
            t.addStudentId(c.getId());
            uService.update(t);
        } else {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        return null;
    }
}
