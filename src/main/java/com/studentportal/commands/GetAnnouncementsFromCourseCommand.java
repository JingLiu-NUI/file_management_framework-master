package com.studentportal.commands;

import com.studentportal.announcement.Announcement;
import com.studentportal.hibernate.AnnouncementService;

import java.util.List;

public class GetAnnouncementsFromCourseCommand implements Command<List<Announcement>> {

    private int courseId;
    private AnnouncementService aService;

    public GetAnnouncementsFromCourseCommand(int courseId, AnnouncementService aService) {
        this.courseId = courseId;
        this.aService = aService;
    }

    @Override
    public List<Announcement> execute() {
        return aService.findAllByCourseId(courseId);
    }
}
