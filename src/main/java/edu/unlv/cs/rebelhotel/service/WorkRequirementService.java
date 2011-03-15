package edu.unlv.cs.rebelhotel.service;

import java.util.HashSet;
//import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import edu.unlv.cs.rebelhotel.domain.WorkTemplate;

@Component
public class WorkRequirementService {
	
	public Set<Major> updateStudentInformation(Set<Major> current_majors, Set<String> majors, Term requirementTerm){
		//List<WorkTemplate> workTemplates = WorkTemplate.findAllWorkTemplates();
		Set<WorkRequirement> workRequirements = new HashSet<WorkRequirement>();
		
		/* now get the work requirements from those majors. 
		 * this implementation might change later if Sam decides to make the changes he anticipated.
		 */
		
		// Do this just in case the student is a preexisting one
		for (Major each : current_majors) {
			workRequirements.addAll(each.getWorkRequirements());
		}

		Major m;
		Set<Major> newMajors = new HashSet<Major>();
		for (WorkRequirement each : workRequirements) {
			m = each.getMajor();
			for (String majorName : majors) {
				if (!majorName.equals(m.getDepartment())){
					Major new_major = new Major();
					Set<WorkRequirement> wr = new HashSet<WorkRequirement>();
					/* new WorkTemplate() will be replaced with an existing work template */
					wr.add(WorkRequirement.fromWorkTemplate(new WorkTemplate(),new_major));
					new_major.setWorkRequirements(wr);
					new_major.setCatalogTerm(requirementTerm);
					/*new_major.setDepartment(majorName); this has to be a Department enum type*/ 
					new_major.setReachedMilestone(false);
					newMajors.add(new_major);
				}
			}
		}
		current_majors.addAll(newMajors);
		return current_majors;
	}
}