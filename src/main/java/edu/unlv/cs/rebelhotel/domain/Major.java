package edu.unlv.cs.rebelhotel.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.persistence.CascadeType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import edu.unlv.cs.rebelhotel.domain.enums.Departments;
import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import edu.unlv.cs.rebelhotel.domain.Term;

@RooJavaBean
@RooEntity
public class Major {

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<WorkRequirement> workRequirements = new HashSet<WorkRequirement>();

	private boolean reachedMilestone;

	@Enumerated
	private Departments department;

	@ManyToOne
	private Term catalogTerm;

	private boolean completed_work_requirements = false;
	/**
	 * The student has at least one major, and each major has to meet its own
	 * work requirements in order to graduate. This method checks to see if each
	 * work requirement for this major has been met. If each work requirement
	 * has been met, the field 'completedWorRequirement' is set to True. False
	 * if not met.
	 */
	public boolean updateMajorStatus() {

		boolean completedWorkRequirement = true;
		Set<WorkRequirement> workrequirements = this.getWorkRequirements();

		for (WorkRequirement workrequirement : workrequirements) {

			completedWorkRequirement &= workrequirement.isMet();
		}
		
		return completedWorkRequirement;

	}
	
	/**
	 * This method adds up all the hours worked by a student with this major
	 * in order to complete this major.
	 * @return
	 */
	public Integer calculateTotalHoursWorked(){
		
		Integer totalHoursWorked = 0;
		Set<WorkRequirement> workrequirements = this.getWorkRequirements();

		for (WorkRequirement workrequirement : workrequirements) {

			totalHoursWorked += workrequirement.getTotalApprovedHours();
		}
		
		return totalHoursWorked;
	}
	
	/**
	 * This method adds up all the hours that need to be worked by a student with this major
	 * in order to complete this major.
	 * @return
	 */
	public Integer calculateTotalHoursRequired(){
		
		Integer totalHoursRequired = 0;
		Set<WorkRequirement> workrequirements = this.getWorkRequirements();

		for (WorkRequirement workrequirement : workrequirements) {
			totalHoursRequired += workrequirement.getHours();
		}
		
		return totalHoursRequired;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		        //sb.append("WorkRequirements: ").append(getWorkRequirements() == null ? "null" : getWorkRequirements().size()).append(", ");
		        //sb.append("ReachedMilestone: ").append(isReachedMilestone()).append(", ");
		        sb.append(getDepartment());//.append(" ");
		        //sb.append("CatalogTerm: ").append(getCatalogTerm());
		        return sb.toString();
		}
}
