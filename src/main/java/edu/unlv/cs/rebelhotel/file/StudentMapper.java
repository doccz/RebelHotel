package edu.unlv.cs.rebelhotel.file;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.domain.enums.UserGroup;
import edu.unlv.cs.rebelhotel.service.WorkRequirementService;

@Component
public class StudentMapper {
	WorkRequirementService workRequirementService;
	
	@Autowired
	public StudentMapper(WorkRequirementService workRequirementService) {
		this.workRequirementService = workRequirementService;
	}
	
	public Student findOrReplace(FileStudent fileStudent){
		Student student = findStudent(fileStudent);
		student.setUserId(fileStudent.getStudentId());
		student.setFirstName(fileStudent.getFirstName());
		student.setLastName(fileStudent.getLastName());
		student.setMiddleName(fileStudent.getMiddleName());
		student.setEmail(fileStudent.getEmail());
		student.setGradTerm(fileStudent.getGradTerm());
		student.setAdmitTerm(fileStudent.getAdmitTerm());
		
		/* if both major columns are populated, and there is only one requirement term,
		 * then can we assume that it is the same for both majors? I suppose...but what if
		 * they take up another major three years after declaring the first one?
		 */
		Set<Major> majors = workRequirementService.updateStudentInformation(student.getMajors(),fileStudent.getMajors(), fileStudent.getRequirementTerm());
		student.setMajors(majors);

		boolean codeOfConductSigned = student.getCodeOfConductSigned();
		// case 1) a new student, so it will not be true, and so we will initialize it to false
		// case 2) a preexisting student, so it can either be true or false; if true, then true; otherwise, false
		codeOfConductSigned = (codeOfConductSigned) ? true : false;
		student.setCodeOfConductSigned(codeOfConductSigned);
		
		UserAccount student_account = new UserAccount();
		student_account.setUserId(fileStudent.getStudentId());
		student_account.setPassword("password");
		student_account.setUserGroup(UserGroup.ROLE_USER);
		student.setUserAccount(student_account);
		//student.setLastModified();
		return student;
	}
	
	public Student findStudent(FileStudent fileStudent) {
		if (null != Student.findStudentsByUserIdEquals(fileStudent.getStudentId())) {
			return 
		}
	}
}
