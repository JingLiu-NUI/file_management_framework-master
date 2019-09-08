package com.studentportal.user;

import com.studentportal.courses.UpdateOperation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "userNum")
// Adaptor
public class Student extends User implements UpdateOperation {


    // what course they assigned to
    private List<Integer> courseIdList = new ArrayList<>();

    private Set<Integer> projectDocumentIdList = new HashSet<>();

    public Student() {}

    public Student(int userNum, String email, String givenName,
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

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "project_doc_ids")
    public Set<Integer> getProjectDocumentIdList() {
        return projectDocumentIdList;
    }

    public void setProjectDocumentIdList(Set<Integer> projectDocumentIdList) {
        this.projectDocumentIdList = projectDocumentIdList;
    }
    public boolean addProjectDocumentId(Integer id) {
        if (projectDocumentIdList.contains(id)) {
            return false;
        } else {
            projectDocumentIdList.add(id);
            return true;
        }
    }

    public boolean removeProjectDocumentId(Integer id) {
        if (projectDocumentIdList.contains(id)) {
            projectDocumentIdList.remove(id);
            return true;
        } else {
            return false;
        }
    }


    // This is a adapter, and implements ITarget (AddCourseOperation)in terms of Adaptee
    // the addCourseId and RemoveCourseOperation are the specificRequest that must be invokered
    @Override
    public boolean addStudentId(Integer id) {
        return addCourseId(courseIdList,id);
    }

    @Override
    public boolean  removeStudentId(Integer id) { return removeCourseId(courseIdList,id);
    }
}
