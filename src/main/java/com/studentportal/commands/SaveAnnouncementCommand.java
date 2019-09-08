package com.studentportal.commands;

import com.studentportal.announcement.Announcement;
import com.studentportal.courses.Course;
import com.studentportal.hibernate.AnnouncementService;
import com.studentportal.hibernate.CourseService;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SaveAnnouncementCommand implements Command<Void> {
    private Course course;
    private CourseService cService;

    public SaveAnnouncementCommand(Course course, CourseService cService) {
        this.course = course;
        this.cService = cService;
    }

    @Override
    public Void execute() {
        cService.update(course);
        return null;
    }
}
