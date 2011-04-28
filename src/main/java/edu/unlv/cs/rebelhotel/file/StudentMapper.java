package edu.unlv.cs.rebelhotel.file;

import java.util.Set;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.domain.enums.UserGroup;

@Component
public class StudentMapper {
	public EmailInformation findOrReplace(FileStudent fileStudent){
		Student student = existingOrNewStudent(fileStudent);
		
		boolean ifNewStudent = student.isNewStudent();
		
		addStudentToMajors(fileStudent.getMajors(),student);
		
		student.setUserId(fileStudent.getStudentId());
		student.setFirstName(fileStudent.getFirstName());
		student.setLastName(fileStudent.getLastName());
		student.setMiddleName(fileStudent.getMiddleName());
		student.setGradTerm(fileStudent.getGradTerm());
		student.setAdmitTerm(fileStudent.getAdmitTerm());
		student.updateMajors(fileStudent.getMajors());
		student.setCodeOfConductSigned(false);

		String password = existingOrNewAccount(fileStudent, student);
		
		student.upsert();
		
		return new EmailInformation(student.getUserAccount(), password, ifNewStudent);
	}

	private void addStudentToMajors(Set<Major> majors, Student student) {
		for (Major each : majors){
			each.setStudent(student);
		}
	}

	private Student existingOrNewStudent(FileStudent fileStudent) {
		Student student;
		try {
			student = Student.findStudentsByUserIdEquals(fileStudent.getStudentId()).getSingleResult();
			return student;
		} catch(EmptyResultDataAccessException e) {
			student = new Student();
		}
		return student;
	}
	
	private String existingOrNewAccount(FileStudent fileStudent, Student student) {
		UserAccount studentAccount;
		String password;
		try {
			studentAccount = UserAccount.findUserAccountsByUserId(fileStudent.getStudentId()).getSingleResult();
			student.setUserAccount(studentAccount);
			password = studentAccount.getPassword();
		} catch(EmptyResultDataAccessException e) {
			studentAccount = new UserAccount();
			studentAccount.setUserId(fileStudent.getStudentId());
			studentAccount.setEmail(fileStudent.getEmail());
			password = studentAccount.generateRandomPassword();
			studentAccount.setPassword(password);
			studentAccount.setUserGroup(UserGroup.ROLE_USER);
			studentAccount.persist();
		}
		student.setUserAccount(studentAccount);
		
		return password;
	}
}
