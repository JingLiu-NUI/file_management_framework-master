package com.studentportal.api;

import com.studentportal.commands.ReminderGetForUserCommand;
import com.studentportal.hibernate.ReminderService;
import com.studentportal.reminders.ReminderTypes.Reminder;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class ReminderApiTest extends TestCase {
    private ReminderService rService = new ReminderService();

    @Test
    public void testGetRemindersForUser() {
        int userId = 1;
        ReminderGetForUserCommand cmd = new ReminderGetForUserCommand(rService, userId);

        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        assertNotNull((List<Reminder>) apiControl.doWork());
    }
}