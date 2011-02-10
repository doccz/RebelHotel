// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.Student;
import java.lang.Long;
import java.lang.String;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Student_Roo_Finder {
    
    public static TypedQuery<Student> Student.findStudentsByFirstNameEquals(String firstName) {
        if (firstName == null || firstName.length() == 0) throw new IllegalArgumentException("The firstName argument is required");
        EntityManager em = Student.entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT Student FROM Student AS student WHERE student.firstName = :firstName", Student.class);
        q.setParameter("firstName", firstName);
        return q;
    }
    
    public static TypedQuery<Student> Student.findStudentsByFirstNameLike(String firstName) {
        if (firstName == null || firstName.length() == 0) throw new IllegalArgumentException("The firstName argument is required");
        firstName = firstName.replace('*', '%');
        if (firstName.charAt(0) != '%') {
            firstName = "%" + firstName;
        }
        if (firstName.charAt(firstName.length() - 1) != '%') {
            firstName = firstName + "%";
        }
        EntityManager em = Student.entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT Student FROM Student AS student WHERE LOWER(student.firstName) LIKE LOWER(:firstName)", Student.class);
        q.setParameter("firstName", firstName);
        return q;
    }
    
    public static TypedQuery<Student> Student.findStudentsByMajor1Equals(String major1) {
        if (major1 == null || major1.length() == 0) throw new IllegalArgumentException("The major1 argument is required");
        EntityManager em = Student.entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT Student FROM Student AS student WHERE student.major1 = :major1", Student.class);
        q.setParameter("major1", major1);
        return q;
    }
    
    public static TypedQuery<Student> Student.findStudentsByMajor2Equals(String major2) {
        if (major2 == null || major2.length() == 0) throw new IllegalArgumentException("The major2 argument is required");
        EntityManager em = Student.entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT Student FROM Student AS student WHERE student.major2 = :major2", Student.class);
        q.setParameter("major2", major2);
        return q;
    }
    
    public static TypedQuery<Student> Student.findStudentsByNSHEEquals(Long NSHE) {
        if (NSHE == null) throw new IllegalArgumentException("The NSHE argument is required");
        EntityManager em = Student.entityManager();
        TypedQuery<Student> q = em.createQuery("SELECT Student FROM Student AS student WHERE student.NSHE = :NSHE", Student.class);
        q.setParameter("NSHE", NSHE);
        return q;
    }
    
}