package com.studentportal.commands;

import com.studentportal.assignments.CompletedQuiz;
import com.studentportal.assignments.QuizGrader;
import com.studentportal.hibernate.AssignmentService;

public class SubmitCompletedQuizCommand implements Command<String> {

    private QuizGrader grader;
    private AssignmentService assignmentService;

    public SubmitCompletedQuizCommand(QuizGrader grader, AssignmentService assignmentService) {
        this.grader = grader;
        this.assignmentService = assignmentService;
    }

    @Override
    public String execute() {
        CompletedQuiz gradedQuiz = grader.gradeCompletedQuiz();
        double percentage = ((gradedQuiz.getScore() * 100) / gradedQuiz.getAnswers().size());
        return String.valueOf(percentage);
    }
}
