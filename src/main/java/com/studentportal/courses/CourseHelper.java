package com.studentportal.courses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentportal.announcement.Announcement;
import com.studentportal.file_management.Document;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CourseHelper {

    public static String convertCourseToJson(Course course) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(course);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Course extractCourseFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Course course = null;
        try {
            course = mapper.readValue(json, Course.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return course;
    }

    public static List<Course> extractCourseListFromJson(String json) {
        List<Course> cList = null;
        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                cList = mapper.readValue(json, new TypeReference<List<Course>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return cList;
    }

    public static Announcement extractAnnouncementFromJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Announcement announcement = null;
        try {
            announcement = mapper.readValue(json, Announcement.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return announcement;
    }

    public static String convertAnnouncementToJson(Announcement announcement) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(announcement);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static List<Announcement> extractAnnouncementListFromJson(String json) {
        System.out.println("***JSON***: " + json + "\n");
        ObjectMapper mapper = new ObjectMapper();
        List<Announcement> announcementList = null;
        try {
            announcementList = mapper.readValue(json, new TypeReference<List<Announcement>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return announcementList;
    }
}
