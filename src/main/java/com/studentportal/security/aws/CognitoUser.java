package com.studentportal.security.aws;

import com.amazonaws.services.cognitoidp.model.AdminGetUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminGetUserResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.studentportal.user.*;
import org.w3c.dom.Attr;

import java.util.List;

public class CognitoUser {
    private static AwsCognito awsCognito = AwsCognito.getInstance();

    public static User getUser(String email) {
        AdminGetUserRequest request = new AdminGetUserRequest()
                .withUserPoolId(awsCognito.getCognitoPoolId())
                .withUsername(email);

        AdminGetUserResult result = awsCognito.getClient().adminGetUser(request);

        String userEmail  = null;
        String givenName = null;
        String familyName = null;
        UserRole userRole = null;

        User user = null;

        List<AttributeType> attrList =  result.getUserAttributes();
        for (AttributeType at : attrList) {
            if (at.getName().equals("custom:user_role")) {
                userRole = UserRole.valueOf(at.getValue());
            } else if (at.getName().equals("email")) {
                userEmail = at.getValue();
            } else if (at.getName().equals("given_name")) {
                givenName = at.getValue();
            } else if (at.getName().equals("family_name")) {
                familyName = at.getValue();
            }
        }

        if (userRole.equals(UserRole.ADMIN)) {
            UserFactory<Admin> adminFactory = new AdminFactory();
            Admin admin = adminFactory.createUser();
            admin.setUserNum(0);
            admin.setUserEmail(userEmail);
            admin.setGivenName(givenName);
            admin.setFamilyName(familyName);
            admin.setUserRole(userRole);
            user = admin;
        } else if (userRole.equals(UserRole.TEACHER)) {
            UserFactory<Teacher> teacherFactory = new TeacherFactory();
            Teacher teacher = teacherFactory.createUser();
            teacher.setUserNum(0);
            teacher.setUserEmail(userEmail);
            teacher.setGivenName(givenName);
            teacher.setFamilyName(familyName);
            teacher.setUserRole(userRole);
            user = teacher;
        } else if (userRole.equals(UserRole.STUDENT)) {
            UserFactory<Student> studentFactory = new StudentFactory();
            Student student = studentFactory.createUser();
            student.setUserNum(0);
            student.setUserEmail(userEmail);
            student.setGivenName(givenName);
            student.setFamilyName(familyName);
            student.setUserRole(userRole);
            user = student;
        }
        return user;
    }
}
