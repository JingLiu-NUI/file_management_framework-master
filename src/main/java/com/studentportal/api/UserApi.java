package com.studentportal.api;

import com.studentportal.commands.GetAllUsersByRoleCommand;
import com.studentportal.hibernate.UserService;
import com.studentportal.user.User;
import com.studentportal.user.UserRole;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserApi {

    private UserService uService = new UserService();

    @GET
    @Path("/all/{userRole}")
    public List<User> getAllUsersByRole(@PathParam("userRole")
                                            String userRole) {
        GetAllUsersByRoleCommand cmd = new GetAllUsersByRoleCommand(
                uService, UserRole.valueOf(userRole));
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (List<User>) apiControl.doWork();
    }
}
