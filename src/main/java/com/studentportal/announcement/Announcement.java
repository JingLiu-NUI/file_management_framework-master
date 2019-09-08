package com.studentportal.announcement;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.studentportal.courses.Course;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "announcement")
@PrimaryKeyJoinColumn(name = "announcement_id")
public class Announcement {
    private int id;
    private String title;
    private String announcement;
    private Course course;
    private String courseCode;
    private String Date;

    public Announcement() {
    }

    public Announcement(int id, String title, String announcement, String Date, Course course, String courseCode) {
        this.id = id;
        this.title = title;
        this.announcement = announcement;
        this.Date = Date;
        this.course = course;
        this.courseCode = courseCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "announcement_id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "announcement")
    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Column(name = "date")
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Column(name = "courseCode")
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + id +
                ", title=" + title +
                ", announcement=" + announcement +
                ", course='" + course.getCourseCode() + '\'' +
                ", date=" + Date +
                '}';
    }


}
