package edu.unlv.cs.rebelhotel.domain;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import edu.unlv.cs.rebelhotel.domain.Student;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import edu.unlv.cs.rebelhotel.domain.Employer;
import edu.unlv.cs.rebelhotel.domain.Supervisor;
import javax.persistence.Embedded;
import edu.unlv.cs.rebelhotel.domain.WorkEffortDuration;
import edu.unlv.cs.rebelhotel.domain.enums.VerificationType;
import javax.persistence.Enumerated;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.PayStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import edu.unlv.cs.rebelhotel.domain.CatalogRequirement;

import java.util.HashSet;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findWorkEffortsByStudentEquals" })
public class WorkEffort {
    //private static final Logger LOG = LoggerFactory.getLogger("audit");
	
    @NotNull
    @ManyToOne
    private Student student;

    private String workPosition;

    private String comment;

    @Embedded
    private Supervisor supervisor;

    @Embedded
    private Employer employer;

    @Enumerated
    private VerificationType verificationType;

    @Enumerated
    private Validation validation;

    @Enumerated
    private Verification verification;

    @Enumerated
    private PayStatus payStatus;

    @Embedded
    private WorkEffortDuration duration;

    @ManyToMany
    private Set<CatalogRequirement> catalogRequirements = new HashSet<CatalogRequirement>();
    
    @PrePersist
    public void persistHours() {
    	Set<Major> majors = student.getMajors();
    	Long totalHours[] = new Long[majors.size()];
    	Long majorHours[] = new Long[majors.size()];
    	Major majorArray[] = new Major[majors.size()];
    	
    	int it = 0;
    	for (Major major : majors) {
    		majorArray[it] = major;
    		majorHours[it] = (long) 0;
    		totalHours[it++] = (long) 0;
    	}
    	
    	Set<WorkEffort> jobs = student.getWorkEffort();
    	for (WorkEffort job : jobs) {
    		if (job.getValidation().equals(Validation.FAILED_VALIDATION)) {
    			continue; // do not count the job if it failed validation
    		}
    		if (job.getVerification().equals(Verification.DENIED)) {
    			continue; // likewise, if the job failed verification, skip
    		}
    		for (int i = 0; i < majorArray.length; i++) {
    			boolean found = false;
    			Set<CatalogRequirement> catalogRequirements = job.getCatalogRequirements();
    			for (CatalogRequirement catalogRequirement : catalogRequirements) {
    				if (catalogRequirement.matchesMajor(majorArray[i])) {
    					found = true;
    					break;
    				}
    			}
    			if (found) {
    				majorHours[i] += job.getDuration().getHours();
    			}
    			totalHours[i] += job.getDuration().getHours();
    		}
    	}
    	
    	for (int i = 0; i < majorArray.length; i++) {
			boolean found = false;
			if (validation.equals(Validation.FAILED_VALIDATION)) {
				break; // do not count the job if it failed validation
    		}
    		if (verification.equals(Verification.DENIED)) {
    			break; // likewise, if the job failed verification, skip
    		}
			if (catalogRequirements != null) {
				for (CatalogRequirement catalogRequirement : catalogRequirements) {
					if (catalogRequirement.matchesMajor(majorArray[i])) {
						found = true;
						break;
					}
				}
			}
			if (found) {
				majorHours[i] += duration.getHours();
			}
			totalHours[i] += duration.getHours();
		}
    	
    	for (int i = 0; i < majorArray.length; i++) {
    		majorArray[i].setTotalHours(new Long(totalHours[i]));
    		majorArray[i].setMajorHours(new Long(majorHours[i]));
    	}
    }
    
    @PreUpdate
    public void updateHours() {
    	Set<Major> majors = student.getMajors();
    	Long totalHours[] = new Long[majors.size()];
    	Long majorHours[] = new Long[majors.size()];
    	Major majorArray[] = new Major[majors.size()];
    	
    	int it = 0;
    	for (Major major : majors) {
    		majorArray[it] = major;
    		majorHours[it] = (long) 0;
    		totalHours[it++] = (long) 0;
    	}
    	
    	Set<WorkEffort> jobs = student.getWorkEffort();
    	for (WorkEffort job : jobs) {
    		if (job.getValidation().equals(Validation.FAILED_VALIDATION)) {
    			continue; // do not count the job if it failed validation
    		}
    		if (job.getVerification().equals(Verification.DENIED)) {
    			continue; // likewise, if the job failed verification, skip
    		}
    		if (job.getId().equals(getId())) { // skip this job in this loop's calculations
    			continue;
    		}
    		for (int i = 0; i < majorArray.length; i++) {
    			boolean found = false;
    			Set<CatalogRequirement> catalogRequirements = job.getCatalogRequirements();
    			for (CatalogRequirement catalogRequirement : catalogRequirements) {
    				if (catalogRequirement.matchesMajor(majorArray[i])) {
    					found = true;
    					break;
    				}
    			}
    			if (found) {
    				majorHours[i] += job.getDuration().getHours();
    			}
    			totalHours[i] += job.getDuration().getHours();
    		}
    	}
    	
    	for (int i = 0; i < majorArray.length; i++) {
			boolean found = false;
			if (validation.equals(Validation.FAILED_VALIDATION)) {
    			break; // do not count the job if it failed validation
    		}
    		if (verification.equals(Verification.DENIED)) {
    			break; // likewise, if the job failed verification, skip
    		}
			if (catalogRequirements != null) {
				for (CatalogRequirement catalogRequirement : catalogRequirements) {
					if (catalogRequirement.matchesMajor(majorArray[i])) {
						found = true;
						break;
					}
				}
			}
			if (found) {
				majorHours[i] += duration.getHours();
			}
			totalHours[i] += duration.getHours();
		}
    	
    	for (int i = 0; i < majorArray.length; i++) {
    		majorArray[i].setTotalHours(new Long(totalHours[i]));
    		majorArray[i].setMajorHours(new Long(majorHours[i]));
    	}
    }
    
    @PreRemove
    public void removeHours() {
    	Set<Major> majors = student.getMajors();
    	Long totalHours[] = new Long[majors.size()];
    	Long majorHours[] = new Long[majors.size()];
    	Major majorArray[] = new Major[majors.size()];
    	
    	int it = 0;
    	for (Major major : majors) {
    		majorArray[it] = major;
    		majorHours[it] = (long) 0;
    		totalHours[it++] = (long) 0;
    	}
    	
    	Set<WorkEffort> jobs = student.getWorkEffort();
    	for (WorkEffort job : jobs) {
    		if (job.getValidation().equals(Validation.FAILED_VALIDATION)) {
    			continue; // do not count the job if it failed validation
    		}
    		if (job.getVerification().equals(Verification.DENIED)) {
    			continue; // likewise, if the job failed verification, skip
    		}
    		if (job.getId().equals(getId())) { // skip this job in this loop's calculations
    			continue;
    		}
    		for (int i = 0; i < majorArray.length; i++) {
    			boolean found = false;
    			Set<CatalogRequirement> catalogRequirements = job.getCatalogRequirements();
    			for (CatalogRequirement catalogRequirement : catalogRequirements) {
    				if (catalogRequirement.matchesMajor(majorArray[i])) {
    					found = true;
    					break;
    				}
    			}
    			if (found) {
    				majorHours[i] += job.getDuration().getHours();
    			}
    			totalHours[i] += job.getDuration().getHours();
    		}
    	}
    	
    	for (int i = 0; i < majorArray.length; i++) {
    		majorArray[i].setTotalHours(new Long(totalHours[i]));
    		majorArray[i].setMajorHours(new Long(majorHours[i]));
    	}
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: ").append(getWorkPosition()).append("\n");
        sb.append("At: ").append(getEmployer().getName()).append("\n");
        sb.append("Duration: ").append(getDuration()).append("\n").append("\n");
        return sb.toString();
    }
}
