package com.studentportal.assignments;

public class QuizQuestionVisitor implements Visitor {
    @Override
    public String visit(QuizQuestion question) {
        return question.getAnswer();
    }
}
