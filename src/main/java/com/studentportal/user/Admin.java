package com.studentportal.user;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "userNum")
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(int userNum, String email, String givenName,
                 String familyName, UserRole userRole) {
        super(userNum, email, givenName, familyName, userRole);
    }
}
