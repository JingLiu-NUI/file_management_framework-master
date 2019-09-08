package com.studentportal.http.courses;

public class UpdateCourseDetails {
    private String courseCode;
    private int studentId;
    private CourseUpdateType updateType;

    public UpdateCourseDetails(String courseCode, int studentId,
                               CourseUpdateType updateType) {
        this.courseCode = courseCode;
        this.studentId = studentId;
        this.updateType = updateType;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public CourseUpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(CourseUpdateType updateType) {
        this.updateType = updateType;
    }
}
