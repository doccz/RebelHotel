package edu.unlv.cs.rebelhotel.domain;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.form.FormStudent;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findStudentsByUserIdEquals", "findStudentsByUserAccount" })
public class Student implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4834725252016589564L;


	@NotNull
    @Column(unique = true)
    private String userId;


    @NotNull
    @Size(min = 2)
    private String firstName;

    private String middleName;

    private String lastName;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Major> majors = new HashSet<Major>();

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Term admitTerm;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Term gradTerm;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<WorkEffort> workEffort = new HashSet<WorkEffort>();

    private Boolean codeOfConductSigned;
    
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "S-")
    private Date lastModified;

    @OneToOne(optional = false, cascade= { CascadeType.PERSIST, CascadeType.REMOVE } )
    private UserAccount userAccount;
    
    private static final Logger LOG = LoggerFactory.getLogger("audit");
    private static final Logger DEBUG_LOG = LoggerFactory.getLogger(Student.class);
	
    @PreUpdate
    public void updateLastModified() {
    	lastModified = new Date();
    	DEBUG_LOG.debug("Updated existing student: " + toString());
	audit();
    }
    
    @PrePersist
    public void createNewStudent(){
    	lastModified = new Date();
    	DEBUG_LOG.debug("Created new student: " + toString());
	audit();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(" + userId + ")");
        sb.append(" " + firstName);
        if (lastName != null) {
            sb.append(" " + lastName);
        }
        return sb.toString();
    }

    public void addWorkEffort(WorkEffort we) {
        workEffort.add(we);
    }
    
    public String getName() {
    	String name = firstName;
    	if (lastName != null) {
    		name += " " + lastName;
    	}
    	return name;
    }
    
    public void updateMajors(Set<Major> newMajors){
    	if (isNewStudent()) {
    		addMajors(newMajors);
    	} else {
    		updateMajorsAsExistingStudent(newMajors);
    	}
    }
    
    private void updateMajorsAsExistingStudent(Set<Major> newMajors) {
    	Set<Major> oldMajors = getMajors();
    	Set<Major> stillDeclaredMajors = new HashSet<Major>();
    	for (Major oldMajor : oldMajors) {
    		if (stillDeclaredMajor(oldMajor,newMajors)){
    			stillDeclaredMajors.add(oldMajor);
    		}
    	}
    	setMajors(stillDeclaredMajors);
    	
		for (Major newMajor : newMajors) {
			if (!hasDeclaredMajor(newMajor)) {
				addMajor(newMajor);
			}
		}
	}

	private boolean stillDeclaredMajor(Major oldMajor, Set<Major> newMajors) {
		return newMajors.contains(oldMajor);
	}

	private boolean hasDeclaredMajor(Major newMajor) {
		return getMajors().contains(newMajor);
	}

	private void addMajor(Major major) {
		getMajors().add(major);
	}

	private void addMajors(Set<Major> majors) {
		getMajors().addAll(majors);
	}

	/**
	 * If the student has an empty set of majors, that means they are new to the system.
	 * 
	 * @return
	 */
	@Transient
	public boolean isNewStudent() {
		return this.majors.isEmpty();
	}
    
    public String getEmail() {
    	return userAccount.getEmail();
    }
    
    public void setEmail(String email) {
    	userAccount.setEmail(email);
    	userAccount.merge();
    }
    
    public void copyFromFormStudent(FormStudent formStudent) {
    	setUserId(formStudent.getUserId());
    	setEmail(formStudent.getEmail());
    	setFirstName(formStudent.getFirstName());
    	setMiddleName(formStudent.getMiddleName());
    	setLastName(formStudent.getLastName());
    	setAdmitTerm(formStudent.getAdmitTerm());
    	setGradTerm(formStudent.getGradTerm());
    	setCodeOfConductSigned(formStudent.getCodeOfConductSigned());
    }
    
    /**
     * This method creates a list of progress reports for each
     * of a given student's majors and returns this list as a
     * java set.
     * @return
     */
    public Set<Progress> calculateProgress(){
    	Set<Progress> progressSet = new HashSet<Progress>();    	
    	for(Major major : this.getMajors()){
    		Progress progress = new Progress(major,this.getWorkEffort());
    		progressSet.add(progress);
    	}
    	
    	return progressSet;
    }
	
	public void audit(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasAuthentication = (null != authentication);
		String userName = "";
		if (hasAuthentication) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				userName = ((UserDetails) principal).getUsername();
			} else {
				userName = principal.toString();
			}
		}

		LOG.info("User {} updated student {}.", new Object[]{userName, userId});
	}

	public boolean exists() {
		return Student.findStudentsByUserIdEquals(getUserId()).getResultList().size() > 0;
	}
	
	public void upsert() {
		if(exists()){
			merge();
		} else {
			persist();
		}
	}
	
	public List<CatalogRequirement> getCatalogRequirements() {
		List<CatalogRequirement> requirements = new LinkedList<CatalogRequirement>();
		List<CatalogRequirement> allRequirements = CatalogRequirement.findAllCatalogRequirementsOrderedById();
		for (CatalogRequirement requirement : allRequirements) {
			boolean found = false;
			for (Major major : majors) {
				if (major.getCatalogTerm().isBetween(requirement.getStartTerm(), requirement.getEndTerm())) {
					found = true;
					break;
				}
			}
			if (found) {
				requirements.add(requirement);
			}
		}
		return requirements;
	}
}
