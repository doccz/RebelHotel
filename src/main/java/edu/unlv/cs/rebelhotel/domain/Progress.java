package edu.unlv.cs.rebelhotel.domain;

import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class Progress {

    private String degreeCode;

    private Integer approvedHours;

    private Integer remainingHours;
    
    private Integer generalHours;
    
    private Integer totalHours;

	public void setDegreeCode(String degreeCode) {
		this.degreeCode = degreeCode;
	}
	
	public String getDegreeCode() {
		return this.degreeCode;
	}
	
	public void setApprovedHours(Integer approvedHours) {
		this.approvedHours = approvedHours;
		
	}
	
	public Integer getApprovedHours() {
		return this.approvedHours;
	}
	
	public void setRemainingHours(Integer remainingHours) {
		this.remainingHours = remainingHours;
	}

	public Integer getRemainingHours() {
		return this.remainingHours;
	}


	
	public Progress(){}
	
	public Progress(String degreeCode, Integer approvedHours, Integer remainingHours) {
		this.degreeCode = degreeCode;
		this.approvedHours = approvedHours;
		this.remainingHours = remainingHours;
	}

	public Progress(Major major, Set<WorkEffort> workHistory) {
		this.degreeCode = major.getDegreeCode();
		this.approvedHours = major.calculateHoursWorked(workHistory);
		this.remainingHours = major.calculateHoursRemaining(workHistory);
		this.generalHours = major.calculateGeneralHours(workHistory);
		this.totalHours = this.getApprovedHours() + this.getGeneralHours();
	}

	
}
