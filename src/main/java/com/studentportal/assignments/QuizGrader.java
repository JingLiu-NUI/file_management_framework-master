package com.studentportal.assignments;

import com.studentportal.hibernate.AssignmentService;

import java.util.ArrayList;
import java.util.List;

public class QuizGrader {

    private AssignmentService assignmentService;
    private QuizAssignment assignment;
    private CompletedQuiz compQuiz;

    public QuizGrader(AssignmentService assignmentService, CompletedQuiz compQuiz) {
        this.assignmentService = assignmentService;
        this.compQuiz = compQuiz;
        getQuizAssignment();
    }

    private void getQuizAssignment() {
        this.assignment = (QuizAssignment) assignmentService
                .findById(compQuiz.getAssignmentId());
    }

    public CompletedQuiz gradeCompletedQuiz() {
        List<String> correctAnswers = new ArrayList<>();
        List<String> studentAnswers = compQuiz.getAnswers();

        List<QuizQuestion> list = assignment.getQuizQuestions();
        QuizQuestionVisitor visitor = new QuizQuestionVisitor();
        for (Element element : list) {
            correctAnswers.add(element.accept(visitor));
        }

        int score = 0;
        for (int i = 0; i < correctAnswers.size(); i++) {
            String correctAns = correctAnswers.get(i);
            String studentAns = studentAnswers.get(i);
            if (correctAns.equals(studentAns)) {
                score += 1;
            }
        }
        this.compQuiz.setScore(score);
        return compQuiz;
    }
}
