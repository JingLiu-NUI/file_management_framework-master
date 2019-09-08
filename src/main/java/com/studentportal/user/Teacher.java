package com.studentportal.user;

import com.studentportal.courses.UpdateOperation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "userNum")
public class Teacher extends User implements UpdateOperation {

    // what course they are in charge of
    private List<Integer> courseIdList = new ArrayList<>();

    public Teacher() {
    }

    public Teacher(int userNum, String email, String givenName,
                   String familyName, UserRole userRole) {
        super(userNum, email, givenName, familyName, userRole);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "course_ids")

    public List<Integer> getCourseIdList() {
        return courseIdList;
    }

    public void setCourseIdList(List<Integer> courseIdList) {
        this.courseIdList = courseIdList;
    }

    // This is a adapter, and implements ITarget (AddCourseOperation)in terms of Adaptee
    // the addCourseId and RemoveCourseOperation are the specificRequest that must be invokered


    @Override
    public boolean addStudentId(Integer id) {
        return addCourseId(courseIdList,id);
    }

    @Override
    public boolean removeStudentId(Integer id) {
        return removeCourseId(courseIdList,id);
    }
}