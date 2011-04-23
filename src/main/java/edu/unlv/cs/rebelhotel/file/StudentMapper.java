package edu.unlv.cs.rebelhotel.file;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.domain.enums.UserGroup;

@Component
public class StudentMapper {
	private static final Logger LOG = Logger.getLogger(StudentMapper.class);
	public Student findOrReplace(FileStudent fileStudent){
		Student student = existingOrNewStudent(fileStudent);
		
		addStudentToMajors(fileStudent.getMajors(),student);
		
		student.setUserId(fileStudent.getStudentId());
		student.setFirstName(fileStudent.getFirstName());
		student.setLastName(fileStudent.getLastName());
		student.setMiddleName(fileStudent.getMiddleName());
		student.setGradTerm(fileStudent.getGradTerm());
		student.setAdmitTerm(fileStudent.getAdmitTerm());
		student.updateMajors(fileStudent.getMajors());
		student.setCodeOfConductSigned(false);

		UserAccount studentAccount = existingOrNewAccount(fileStudent);
		student.setUserAccount(studentAccount);
		
		return student;
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
	
	private UserAccount existingOrNewAccount(FileStudent fileStudent) {
		UserAccount studentAccount;
		try {
			studentAccount = UserAccount.findUserAccountsByUserId(fileStudent.getStudentId()).getSingleResult();
			return studentAccount;
		} catch(EmptyResultDataAccessException e) {
			studentAccount = new UserAccount();
			studentAccount.setUserId(fileStudent.getStudentId());
			studentAccount.setEmail(fileStudent.getEmail());
			studentAccount.setPassword(studentAccount.generateRandomPassword());
			studentAccount.setUserGroup(UserGroup.ROLE_USER);
			studentAccount.persist();
		}
		return studentAccount;
	}
}
