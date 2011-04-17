package edu.unlv.cs.rebelhotel.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import edu.unlv.cs.rebelhotel.domain.Term;

@RooJavaBean
@RooEntity
public class Major {
	private boolean reachedMilestone;

	@NotNull
	private String degreeCode;

	@ManyToOne
	private Term catalogTerm;
	
	@Deprecated
	private boolean completed_work_requirements = false;
	
	public Major(){}
	
	public Major(String degreeCode, Term catalogTerm) {
		this.degreeCode = degreeCode;
		this.catalogTerm = catalogTerm;
		this.reachedMilestone = false;
	}
	
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDegreeCode()).append(", ");
        return sb.toString();
    }
    
    public int calculateHoursWorked(Set<WorkEffort> workHistory){
    	int sum = 0;
    	for(WorkEffort job : workHistory){
    		if(job.isApplicable(this)){
    			sum+=job.getDuration().getHours();
    		}
    	}
    	return sum;
    }

	public int findMajorRequiredHours(){
    	int hoursNeeded = 0;
    	Set<CatalogRequirement> requirements = new HashSet<CatalogRequirement>(CatalogRequirement.findAllCatalogRequirements());
    	for(CatalogRequirement requirement : requirements){
    		if(this.appliesTo(requirement)){
    			hoursNeeded = requirement.getTotalHoursNeeded();
    		}
    	}
    	return hoursNeeded;
	}
	
    public int calculateHoursRemaining(Set<WorkEffort> workHistory){
    	int remainingHours = 0;
    	
    	int approvedHours = calculateHoursWorked(workHistory);
    	int totalHoursNeeded = findMajorRequiredHours();
    	
    	remainingHours = totalHoursNeeded - approvedHours;
    	
    	return remainingHours;	
    }
    
    public int calculateGeneralHours(Set<WorkEffort> workHistory){
    	int sum = 0;
    	for(WorkEffort job : workHistory){
    		if(!job.isApplicable(this)){
    			sum+=job.getDuration().getHours();
    		}
    	}
    	
    	return sum;
    }
    
	public boolean appliesTo(CatalogRequirement requirement) {
		//return this.getDegreeCode().startsWith(requirement.getDegreeCodePrefix());
		return this.getDegreeCode().equals(requirement.getDegreeCodePrefix());
	}
}
