package com.studentportal.http;

import com.studentportal.http.assignments.ProjectRequestFactory;
import com.studentportal.http.assignments.QuizRequestFactory;
import com.studentportal.http.courses.CourseRequestFactory;
import com.studentportal.http.documents.DocumentRequestFactory;
import com.studentportal.http.users.UserRequestFactory;

public class RequestFactoryProducer {

    public static RequestAbstractFactory getFactory(RequestChoice choice) {
        if (choice.equals(RequestChoice.DOCUMENT)) {
            return new DocumentRequestFactory();
        } else if (choice.equals(RequestChoice.PROJECT)) {
            return new ProjectRequestFactory();
        } else if (choice.equals(RequestChoice.QUIZ)) {
            return new QuizRequestFactory();
        } else if (choice.equals(RequestChoice.COURSE)) {
            return new CourseRequestFactory();
        } else if (choice.equals(RequestChoice.USER)) {
            return new UserRequestFactory();
        }
        return null;
    }
}
