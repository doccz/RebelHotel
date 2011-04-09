package edu.unlv.cs.rebelhotel.service;

//import org.springframework.transaction.annotation.Transactional;

import edu.unlv.cs.rebelhotel.domain.Student;

//@Transactional
public class UserInformation {
	private Student student;
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
}