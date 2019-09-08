package com.studentportal.courses;

import java.util.List;

public interface UpdateOperation {

    // this is Itarget,the interface the the client(savaCourseCommand)wants to use
    public boolean addStudentId(Integer id);

    public boolean removeStudentId(Integer id);

}
