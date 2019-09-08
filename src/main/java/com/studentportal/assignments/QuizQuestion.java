package com.studentportal.assignments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Table(name = "quiz_questions")
public class QuizQuestion implements Element {
    private int quiz_id;
    private String question;
    private String choices;
    private String answer;

    private Assignment assignment;

    public QuizQuestion() {}

    public QuizQuestion(int quiz_id, String question, String choices, String answer, Assignment assignment) {
        this.quiz_id = quiz_id;
        this.question = question;
        this.choices = choices;
        this.answer = answer;
        this.assignment = assignment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quiz_id")
    public int getQuizId() {
        return quiz_id;
    }

    public void setQuizId(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    @Column(name = "question")
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Column(name = "choices")
    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    @Column(name = "answer")
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "question='" + question + '\'' +
                ", choices='" + choices + '\'' +
                ", answer=" + answer +
                '}';
    }

    @Override
    public String accept(Visitor v) {
        return v.visit(this);
    }
}
