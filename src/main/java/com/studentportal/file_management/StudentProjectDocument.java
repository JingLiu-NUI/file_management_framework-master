package com.studentportal.file_management;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.File;

@Entity
@Table(name = "student_projects")
@PrimaryKeyJoinColumn(name = "id")
public class StudentProjectDocument extends Document {

    private int assignmentId;
    private int courseId;
    private int studentId;

    protected StudentProjectDocument() {
        super();
    }

    protected StudentProjectDocument(int id, File file, int assignmentId, int courseId, int studentId) {
        super(id, file);
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public static StudentProjectDocument createStudentProjectDoc(File file, int assignmentId, int courseId,
                                                                 int studentId) {
        return new StudentProjectDocument(0, file, assignmentId, courseId, studentId);
    }

    @Column(name = "assignment_id")
    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Column(name = "course_id")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name = "student_id")
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
