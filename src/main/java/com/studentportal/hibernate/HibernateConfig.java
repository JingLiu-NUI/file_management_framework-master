package com.studentportal.hibernate;

import com.studentportal.assignments.Assignment;
import com.studentportal.assignments.ProjectAssignment;
import com.studentportal.assignments.QuizAssignment;
import com.studentportal.assignments.QuizQuestion;
import com.studentportal.courses.Course;
import com.studentportal.file_management.Document;
import com.studentportal.file_management.StudentProjectDocument;
import com.studentportal.user.Admin;
import com.studentportal.user.Student;
import com.studentportal.user.Teacher;
import com.studentportal.user.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateConfig {

    private static SessionFactory sessionFactory = null;

    private static void configure() {
//        Configuration conf = new Configuration();

//        // class mappings
//        conf.addAnnotatedClass(Document.class);
//        conf.addAnnotatedClass(Assignment.class);
//        conf.addAnnotatedClass(QuizAssignment.class);
//        conf.addAnnotatedClass(QuizQuestion.class);
//        conf.addAnnotatedClass(ProjectAssignment.class);
//        conf.addAnnotatedClass(Course.class);
//        conf.addAnnotatedClass(Student.class);
//        conf.addAnnotatedClass(Teacher.class);
//        conf.addAnnotatedClass(User.class);
//        conf.addAnnotatedClass(Admin.class);
//        conf.addAnnotatedClass(StudentProjectDocument.class);

//        conf.configure();
//
//        // props
//        conf.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
//        conf.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/data-store?useSSL=false");
//        conf.setProperty("hibernate.connection.username", "root");
//        conf.setProperty("hibernate.connection.password", "root");
//        conf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
//        conf.setProperty("hibernate.hbm2ddl.auto", "update");
//        conf.setProperty("hibernate.show_sql", "true");
//        conf.setProperty("hibernate.connection.pool_size", "20");
//        StandardServiceRegistryBuilder b = new StandardServiceRegistryBuilder()
//                        .applySettings(conf.getProperties());
//        sessionFactory = new Configuration().configure().buildSessionFactory(b.build());

        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            configure();
        }
        return sessionFactory;
    }

    public static synchronized void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
