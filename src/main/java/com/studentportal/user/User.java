package com.studentportal.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.util.List;

/**
 * This is the base class for all users in the system
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Admin.class, name = "Admin"),
        @JsonSubTypes.Type(value = Teacher.class, name = "Teacher"),
        @JsonSubTypes.Type(value = Student.class, name = "Student")}
)
public abstract class User {

    private int userNum;
    private String userEmail;
    private String givenName;
    private String familyName;
    private UserRole userRole;

    public User() {
        super();
    }

    public User(int userNum, String userEmail, String givenName,
                String familyName, UserRole userRole) {
        this.userNum = userNum;
        this.userEmail = userEmail;
        this.givenName = givenName;
        this.familyName = familyName;
        this.userRole = userRole;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_num")
    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    @Column(name = "email")
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Column(name = "given_name")
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @Column(name = "family_name")
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }


    // This is a Adaptee, the implementation that must be adapted to the interface ITarget.
    public boolean addCourseId(List<Integer> courseIdList, Integer courseId) {
        if (courseIdList.contains(courseId)) {
            courseIdList.remove(courseId);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeCourseId(List<Integer> courseIdList, Integer courseId) {
        if (courseIdList.contains(courseId)) {
            return false;
        } else {
            courseIdList.add(courseId);
            return true;
        }
    }



    @Override
    public String toString() {
        return "User{" +
                "userNum=" + userNum +
                ", userEmail='" + userEmail + '\'' +
                ", givenName='" + givenName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
