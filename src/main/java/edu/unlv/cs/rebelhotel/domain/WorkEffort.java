package edu.unlv.cs.rebelhotel.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import edu.unlv.cs.rebelhotel.domain.Student;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import edu.unlv.cs.rebelhotel.domain.Employer;
import edu.unlv.cs.rebelhotel.domain.Supervisor;
import javax.persistence.Embedded;
import edu.unlv.cs.rebelhotel.domain.WorkEffortDuration;
import edu.unlv.cs.rebelhotel.domain.enums.VerificationType;
import javax.persistence.Enumerated;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.PayStatus;

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


	public boolean isAccepted() {
		return verification == Verification.ACCEPTED;
	}

	public boolean isValidated() {
		return validation.equals(Validation.VALIDATED);
	}

	/**
	 * After the student submits a work effort towards his/her major, that work
	 * effort has to be verified and maybe also be validated if applicable
	 * before those hours from that work effort can be counted towards the
	 * student's completed hours. This method returns true if that work effort
	 * has been verified and validated
	 * @param major 
	 * 
	 * @return
	 */
	public boolean isApplicable(Major major) {

		boolean applicableRequirement = hasApplicableCatalogRequirement(major);
		boolean validation = hasValidation();
		
		boolean accepted = isAccepted();
		boolean validated = isValidated();

		return applicableRequirement && accepted && (validated || validation);
	}

	/**
	 * Returns true if this job is NOT selected for random validation
	 * @return
	 */
	private boolean hasValidation() {
		return this.validation.equals(Validation.NO_VALIDATION);
	}
	
	/**
	 * This method returns true if this job satisfies the catalog requirement
	 * of a given major. Returns false otherwise
	 * @param major
	 * @return
	 */
	private boolean hasApplicableCatalogRequirement(Major major){
		boolean isApplicable = false;
		
		for(CatalogRequirement requirement : this.getCatalogRequirements()){
			isApplicable |= major.appliesTo(requirement);
		}
		return isApplicable;
	}

    @ManyToMany
    private Set<CatalogRequirement> catalogRequirements = new HashSet<CatalogRequirement>();
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Position: ").append(getWorkPosition()).append("\n");
        sb.append("At: ").append(getEmployer().getName()).append("\n");
        sb.append("Duration: ").append(getDuration()).append("\n").append("\n");
        return sb.toString();
    }
}
