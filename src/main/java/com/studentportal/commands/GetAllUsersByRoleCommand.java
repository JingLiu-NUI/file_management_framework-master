package com.studentportal.commands;

import com.studentportal.hibernate.UserService;
import com.studentportal.user.User;
import com.studentportal.user.UserRole;

import java.util.List;

public class GetAllUsersByRoleCommand implements Command<List<User>>{
    private UserService uService;
    private UserRole userRole;

    public GetAllUsersByRoleCommand(UserService uService, UserRole userRole) {
        this.uService = uService;
        this.userRole = userRole;
    }

    @Override
    public List<User> execute() {
        return uService.findByRole(userRole);
    }
}
