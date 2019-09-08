package com.studentportal.courses;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studentportal.announcement.Announcement;
import com.studentportal.user.Teacher;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course implements UpdateOperation {

    private int id;
    private String courseCode;
    private int teacherId;
    private List<Integer> studentIdList = new ArrayList<>();
    private Set<Announcement> announcementSet = new HashSet<>();

    public Course() {
    }

    public Course(int id, String courseCode, int teacherId) {
        this.id = id;
        this.courseCode = courseCode;
        this.teacherId = teacherId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "course_code")
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Column(name = "teacherId")
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "studentIdList")
    public List<Integer> getStudentIdList() {
        return studentIdList;
    }

    public void setStudentIdList(List<Integer> studentIdList) {
        this.studentIdList = studentIdList;
    }

    @JsonManagedReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    public Set<Announcement> getAnnouncementSet() {
        return announcementSet;
    }

    public void setAnnouncementSet(Set<Announcement> announcementSet) {
        this.announcementSet = announcementSet;
    }

    public void addAnnouncement(Announcement announcement) {
        this.announcementSet.add(announcement);
    }

    public void removeAnnouncement(Integer id) {
        for (Announcement a : announcementSet) {
            if (a.getId() == id) {
                announcementSet.remove(a);
            }
        }
    }

    @Override
    public boolean addStudentId(Integer studentId) {
        if (studentIdList.contains(studentId)) {
            return false;
        } else {
            studentIdList.add(studentId);
            return true;
        }
    }

    @Override
    public boolean removeStudentId(Integer studentId) {
        if (studentIdList.contains(studentId)) {
            studentIdList.remove(studentId);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseCode + '\'' +
                ", teacherId=" + teacherId + '\'' +
                ", studentList=" + studentIdList.size() +
                '}';
    }
}

