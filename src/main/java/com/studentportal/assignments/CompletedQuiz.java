package com.studentportal.assignments;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students")
public class CompletedQuiz {

    private int id;
    private List<String> answers;
    private int assignmentId;
    private int studentId;
    private int courseId;
    private int score;

    public CompletedQuiz() { }

    public CompletedQuiz(int id, List<String> answers, int assignmentId, int studentId,
                         int courseId, int score) {
        this.id = id;
        this.answers = answers;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.score = score;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "answers")
    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Column(name = "assignmentId")
    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssigmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    @Column(name = "studentId")
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    @Column(name = "courseId")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Column(name = "score")
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
