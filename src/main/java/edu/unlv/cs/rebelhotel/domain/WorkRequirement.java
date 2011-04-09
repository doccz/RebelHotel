package edu.unlv.cs.rebelhotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.xml.datatype.Duration;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooEntity
public class WorkRequirement {

	@NotNull
	private String name;

	private Integer hours;

	private boolean milestone;
	
	@ManyToOne
	private Student student;

	/**
	 * Each student is required to work a certain hours in order to graduate.
	 * When the student submits the hours worked, those hours have to be verified
	 * and sometimes validated. This method counts all the hours this student worked
	 * as long as those hours have been verified and validated. It returns the sum
	 * of all the hours this student has worked
	 * @return
	 */
	public Integer getTotalApprovedHours() {

		Integer approvedHours = 0;
		Set<WorkEffort> workEfforts = this.getWorkEffort();
		for (WorkEffort workeffort : workEfforts) {
			if (workeffort.isApplicable()){
				approvedHours += workeffort.getDuration().getHours();
			}
		}
		return approvedHours;
	}

	/**
	 * In order for a student to progress towards graduating, the required
	 * work requirements need to be completed. This method takes a look at
	 * the total hours worked by the student to the hours that need to be worked.
	 * It returns true if the student worked at least that many hours, false otherwise.
	 * @return
	 */
	public boolean isMet() {
		return this.getHours() <= this.getTotalApprovedHours();
	}
	
    
    /*@ManyToOne
    private Major major;*/
    
    @ManyToMany
    private Set<WorkEffort> workEffort = new HashSet<WorkEffort>();
    
    // A method to construct a work requirement from a work template
    public static WorkRequirement fromWorkTemplate(WorkTemplate wt, Major major) {
    	WorkRequirement wr = new WorkRequirement();
    	wr.setHours(wt.getHours());
    	wr.setName(wt.getName());
    	//wr.setMajor(major);
    	return wr;
    }
}
