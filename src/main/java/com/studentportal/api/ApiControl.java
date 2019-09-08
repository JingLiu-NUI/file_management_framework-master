package com.studentportal.api;

import com.studentportal.commands.Command;

public class ApiControl {

    private Command command;

    public  ApiControl() {}

    public void setCommand(Command command) {
        this.command = command;
    }

    public Object doWork() {
        return command.execute();
    }
}
