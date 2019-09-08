package com.studentportal.api;

import com.studentportal.commands.*;
import com.studentportal.courses.Course;
import com.studentportal.hibernate.CourseService;
import com.studentportal.hibernate.UserService;
import junit.framework.TestCase;

import java.util.List;

public class CourseApiTest extends TestCase {
    private CourseService cService = new CourseService();
    private UserService uService = new UserService();

    public void testCreate() throws Exception {
        Course course = new Course(0, "test", 0);
        SaveCourseCommand cmd = new SaveCourseCommand(cService, uService, course);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        assertNull(apiControl.doWork());
    }

    public void testGetAllByTeacher() throws Exception {
        GetAllCoursesByTeacherCommand cmd = new GetAllCoursesByTeacherCommand(cService, 1);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        assertNotNull((List<Course>) apiControl.doWork());
    }

    public void testAddStudentToCourse() throws Exception {
        AddStudentToCourseCommand cmd = new AddStudentToCourseCommand(cService, uService, "test", 0);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        assertNull(apiControl.doWork());
    }

    public void testRemoveStudentFromCourse() throws Exception {
        RemoveStudentFromCourseCommand cmd = new RemoveStudentFromCourseCommand(cService, uService, "test", 0);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        assertNull(apiControl.doWork());
    }

    public void testAddAnnouncementToCourse() {
        Course course = new Course(0, "test", 0);
        SaveAnnouncementCommand cmd = new SaveAnnouncementCommand(course, cService);
        ApiControl control = new ApiControl();
        control.setCommand(cmd);
        assertNull(control.doWork());
    }
}