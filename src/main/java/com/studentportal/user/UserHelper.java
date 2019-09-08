package com.studentportal.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentportal.courses.Course;

import java.io.IOException;
import java.util.List;

public class UserHelper {
    public static User extractUserFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(json, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<User> extractUserListFromJson(String json) {
        List<User> uList = null;
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                uList = mapper.readValue(json, new TypeReference<List<User>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return uList;
    }
}
