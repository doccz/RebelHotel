package edu.unlv.cs.rebelhotel.service;

import java.io.Serializable;

import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.UserAccount;

// NOTE that the variables stored in the UserInformation object are NOT SYNCHRONIZED with the database
// the safest procedure would be to use these variables only to get the ID of the object, then grab the object from the database to receive an up-to-date object
// ie, Student student = Student.findStudent(userInformation.getStudent().getId())

// TODO consider storing object IDs instead of the objects themselves to prevent people from accessing out-of-date information and trying to merge the session
// scoped variables
public class UserInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 327275037034671906L;
	private Student student;
	private UserAccount userAccount;
	
	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
}
