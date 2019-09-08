package com.studentportal.assignments;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentportal.courses.Course;

import java.io.IOException;
import java.util.List;

public class AssignmentHelper {

    public static String convertAssignmentToJson(Assignment a) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(a);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Assignment extractAssignmentFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Assignment a = null;
        try {
            a = mapper.readValue(json, Assignment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

    public static List<Assignment> extractAssignmentListFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        List<Assignment> aList = null;
        if (json != null) {
            try {
                aList = mapper.readValue(json, new TypeReference<List<Assignment>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return aList;
    }

    public static String convertCompletedQuizToJson(CompletedQuiz compQuiz) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(compQuiz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static CompletedQuiz extractCompletedQuizFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        CompletedQuiz compQuiz = null;
        try {
            compQuiz = mapper.readValue(json, CompletedQuiz.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compQuiz;
    }
}
