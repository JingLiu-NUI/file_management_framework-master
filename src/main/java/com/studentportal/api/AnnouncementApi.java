package com.studentportal.api;

import com.studentportal.announcement.Announcement;
import com.studentportal.commands.GetAnnouncementsFromCourseCommand;
import com.studentportal.commands.SaveAnnouncementCommand;
import com.studentportal.courses.CourseHelper;
import com.studentportal.hibernate.AnnouncementService;
import com.studentportal.hibernate.CourseService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/announcements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AnnouncementApi {

    private AnnouncementService aService = new AnnouncementService();

    @GET
    @Path("/get/all/from/{courseId}")
    public List<Announcement> getAllAnnouncementsFromCourse(@PathParam("courseId") int courseId) {
        GetAnnouncementsFromCourseCommand cmd = new GetAnnouncementsFromCourseCommand(courseId, aService);
        ApiControl control = new ApiControl();
        control.setCommand(cmd);
        return (List<Announcement>) control.doWork();
    }
}
