package edu.unlv.cs.rebelhotel.service;

import java.io.Serializable;

import edu.unlv.cs.rebelhotel.domain.Student;

public class UserInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 327275037034671906L;
	private Student student;
	
	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
}