package com.studentportal.assignments;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "quiz_assignments")
@PrimaryKeyJoinColumn(name = "id")
public class QuizAssignment extends Assignment {

    private List<QuizQuestion> quizQuestions;

    public QuizAssignment() {
        super();
    }

    public QuizAssignment(int id, String name, int courseId, String courseCode, Date startDate,
                          Date endDate, List<QuizQuestion> quizQuestions) {
        super(id, name, courseId, courseCode, startDate, endDate);
        this.quizQuestions = quizQuestions;
    }

    @JsonManagedReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(mappedBy = "assignment", fetch = FetchType.EAGER)
    public List<QuizQuestion> getQuizQuestions() {
        return quizQuestions;
    }

    public void setQuizQuestions(List<QuizQuestion> quizQuestions) {
        this.quizQuestions = quizQuestions;
    }

    @Override
    public String toString() {
        return "QuizAssignment{" +
                "quizQuestionsSize=" + quizQuestions +
                '}';
    }
}
