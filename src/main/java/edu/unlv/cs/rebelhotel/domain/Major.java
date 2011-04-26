package edu.unlv.cs.rebelhotel.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import edu.unlv.cs.rebelhotel.domain.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RooJavaBean
@RooEntity(finders = {"findMajorsByDegreeCodeAndCatalogTerm"})
public class Major {
	private boolean reachedMilestone;

	@NotNull
	private String degreeCode;

	@ManyToOne(cascade = {CascadeType.MERGE})
    	private Term catalogTerm;
	
	@ManyToOne
	private Student student;
	
	private Long totalHours; // calculated progress toward completion is stored here
	private Long majorHours; // major related hours (50% requirement)


	public Major() {
	}

	public Major(String degreeCode, Term catalogTerm) {
		this.degreeCode = degreeCode;
		this.catalogTerm = catalogTerm;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getDegreeCode()).append(", ");
		return sb.toString();
	}

	/**
	 * If a job is related to a major, the hours worked in that job
	 * count towards related hours worked for that major.
	 * @param workHistory
	 * @return
	 */
	public int calculateRelatedHoursWorked(Set<WorkEffort> workHistory) {
		int sum = 0;
		for (WorkEffort job : workHistory) {
			if (job.isApplicable(this)) {
				sum += job.getDuration().getHours();
			}
		}
		return sum;
	}

	/**
	 * This method searches through the existing catalog requirements to find
	 * the hours needed to complete for this major
	 * @return
	 */
	public int findMajorRequiredHours() {
		int hoursNeeded = 0;
		Set<CatalogRequirement> requirements = new HashSet<CatalogRequirement>(
				CatalogRequirement.findAllCatalogRequirements());
		for (CatalogRequirement requirement : requirements) {
			if (this.appliesTo(requirement)) {
				hoursNeeded = Math.max(requirement.getTotalRelatedHoursNeeded(),
						hoursNeeded);
			}
		}
		return hoursNeeded;
	}

	/**
	 * The work hours remaining in order to complete a major. It is the
	 * difference between the approved hours for that major and catalog
	 * requirement hours
	 * 
	 * @param workHistory
	 * @return remaining hours for the major
	 */
	public int calculateHoursRemaining(Set<WorkEffort> workHistory) {
		int remainingHours = 0;

		int approvedHours = calculateRelatedHoursWorked(workHistory);
		int totalHoursNeeded = findMajorRequiredHours();

		remainingHours = totalHoursNeeded - approvedHours;

		return remainingHours;
	}

	/**
	 * This method adds up all the hours worked by a given student
	 * in the jobs and counts it towards total hours worked. 
	 * @param workHistory
	 * @return
	 */
	public int calculateTotalHours(Set<WorkEffort> workHistory){
		int totalHours = 0;
		for (WorkEffort job : workHistory) {
				totalHours += job.getDuration().getHours();
		}

		return totalHours;
	}

	/**
	 * This method returns true if a catalog requirement applies to this major.
	 * False otherwise
	 * 
	 * @param requirement
	 * @return
	 */
	public boolean appliesTo(CatalogRequirement requirement) {
		return this.getDegreeCode().startsWith(
				requirement.getDegreeCodePrefix())
				&& catalogTerm.isBetween(requirement.getStartTerm(),
						requirement.getEndTerm());
	}
}
