package edu.unlv.cs.rebelhotel.file;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.service.WorkRequirementService;

@Component
public class StudentMapper {
	WorkRequirementService workRequirementService;
	
	@Autowired
	public StudentMapper(WorkRequirementService workRequirementService) {
		this.workRequirementService = workRequirementService;
	}
	
	public Student findOrReplace(FileStudent fileStudent){
		Student student;
		student = Student.findStudentsByUserIdEquals(fileStudent.getStudentId()).getSingleResult();
		if (student == null) {
			student = new Student();
		}
		
		student.setUserId(fileStudent.getStudentId());
		student.setFirstName(fileStudent.getFirstName());
		student.setLastName(fileStudent.getLastName());
		student.setMiddleName(fileStudent.getMiddleName());
		student.setEmail(fileStudent.getEmail());
		student.setGradTerm(fileStudent.getGradTerm());

		/* if both major columns are populated, and there is only one requirement term,
		 * then can we assume that it is the same for both majors? I suppose...but what if
		 * they take up another major three years after declaring the first one?
		 */
		
		Set<Major> majors = workRequirementService.updateStudentInformation(student.getMajors(),fileStudent.getMajors(), fileStudent.getRequirementTerm());
		student.setMajors(majors);
		student.setAdmitTerm(fileStudent.getAdmitTerm());
		
		return student;
	}
}
