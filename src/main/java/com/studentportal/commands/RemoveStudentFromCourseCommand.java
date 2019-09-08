package com.studentportal.commands;

import com.studentportal.courses.Course;
import com.studentportal.courses.UpdateOperation;
import com.studentportal.hibernate.CourseService;
import com.studentportal.hibernate.UserService;
import com.studentportal.user.Student;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class RemoveStudentFromCourseCommand implements Command<Void> {

    private CourseService cService;
    private UserService uService;
    private String courseCode;
    private int studentId;

    public RemoveStudentFromCourseCommand(CourseService cService, UserService uService,
                                          String courseCode, int studentId) {
        this.cService = cService;
        this.uService = uService;
        this.courseCode = courseCode;
        this.studentId = studentId;
    }

    @Override
    public Void execute() {
        Course course = cService.findByCode(courseCode);
        Student student = (Student) uService.findById(studentId);

//        System.out.println(course.getStudentIdList().toString());
//        System.out.println(student.getCourseIdList().toString());
        if (course.getStudentIdList().contains(studentId)) {
            course.removeStudentId(studentId);
            student.removeStudentId(course.getId());
            cService.update(course);
            uService.update(student);
        } else {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
        return null;
    }
}
