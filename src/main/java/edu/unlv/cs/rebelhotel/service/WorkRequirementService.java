package edu.unlv.cs.rebelhotel.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import edu.unlv.cs.rebelhotel.domain.WorkTemplate;

@Component
public class WorkRequirementService {
	
	public void updateStudentInformation(Set<String> majors){
		//List<WorkTemplate> workTemplates = WorkTemplate.findAllWorkTemplates();
		Set<WorkRequirement> workRequirements = new HashSet<WorkRequirement>();
		
		/* now get the work requirements from those majors. 
		 * this implementation might change later if Sam decides to make the changes he anticipated.
		
		for (Major each : majors) {
			workRequirements.addAll(each.getWorkRequirements());
		}
		*/

		Major m;
		Set<Major> newMajors = new HashSet<Major>();
		for (WorkRequirement each : workRequirements) {
			m = each.getMajor();
			for (String majorName : majors) {
				if (!majorName.equals(m.getDepartment())){
					
				}
			}
		}
		// you will want to return something at some point!
	}
}