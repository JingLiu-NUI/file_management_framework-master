package com.studentportal.api;


import com.studentportal.commands.ReminderGetForUserCommand;
import com.studentportal.commands.ReminderSetCommand;
import com.studentportal.hibernate.ReminderService;
import com.studentportal.reminders.ReminderHelper;
import com.studentportal.reminders.ReminderTypes.Reminder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/reminder")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReminderApi {

    private ReminderService rService = new ReminderService();

    @POST
    @Path("/setReminder")
    public void setReminder(String json) {
        Reminder r = ReminderHelper.extractReminderFromJson(json);
        ReminderSetCommand cmd = new ReminderSetCommand(rService, r);

        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();

    }

    @GET
    @Path("/byUser/{userId}")
    public List<Reminder> getRemindersForUser(@PathParam("userId") int userId) {
        ReminderGetForUserCommand cmd = new ReminderGetForUserCommand(rService, userId);

        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (List<Reminder>) apiControl.doWork();
    }
}
