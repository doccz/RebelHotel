package edu.unlv.cs.rebelhotel.service;

import java.util.HashSet;
//import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import edu.unlv.cs.rebelhotel.domain.WorkTemplate;
import edu.unlv.cs.rebelhotel.domain.enums.Departments;

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

		// Create any new majors
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
					
					// I would have done a case/switch, but that does not work for strings, apparently.
					switch (Departments.valueOf(majorName)){
					case HOSPITALITY_MANAGEMENT: {
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
						break;
					}
					case HOTEL_ADMINISTRATION_BEVERAGE_MANAGEMENT:{
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
						break;
					}
					case FOOD_SERVICE_MANAGEMENT:{
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
						break;
					}
					case LODGING_AND_RESORT_MANAGEMENT:{
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
						break;
					}
					case MEETINGS_AND_EVENTS_MANAGEMENT:{
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
						break;
					}
					case CULINARY_ARTS_MANAGEMENT: {
						new_major.setDepartment(Departments.CULINARY_ARTS_MANAGEMENT);
						break;
					}
					case CULINARY_ARTS_BEVERAGE_MANAGEMENT: {
						new_major.setDepartment(Departments.CULINARY_ARTS_BEVERAGE_MANAGEMENT);
						break;
					}
					case GAMING_MANAGEMENT: {
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
						break;
					}
					default: {
						new_major.setDepartment(Departments.NOVALUE); 
						break;
					}
					}
						
					/*
					if (majorName.equals(Departments.CULINARY_ARTS_BEVERAGE_MANAGEMENT)) {
						new_major.setDepartment(Departments.CULINARY_ARTS_BEVERAGE_MANAGEMENT);
					} else if (majorName.equals(Departments.CULINARY_ARTS_MANAGEMENT)) {
						new_major.setDepartment(Departments.CULINARY_ARTS_MANAGEMENT);
					} else if (majorName.equals(Departments.FOOD_SERVICE_MANAGEMENT)) {
						new_major.setDepartment(Departments.FOOD_SERVICE_MANAGEMENT);
					} else if (majorName.equals(Departments.GAMING_MANAGEMENT)) {
						new_major.setDepartment(Departments.GAMING_MANAGEMENT);
					} else if (majorName.equals(Departments.HOSPITALITY_MANAGEMENT)) {
						new_major.setDepartment(Departments.HOSPITALITY_MANAGEMENT);
					} else if (majorName.equals(Departments.HOTEL_ADMINISTRATION_BEVERAGE_MANAGEMENT)) {
						new_major.setDepartment(Departments.HOTEL_ADMINISTRATION_BEVERAGE_MANAGEMENT);
					} else if (majorName.equals(Departments.))
					*/
					
					new_major.setReachedMilestone(false);
					newMajors.add(new_major);
				}
			}
		}
		current_majors.addAll(newMajors);
		return current_majors;
	}
}