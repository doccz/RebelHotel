package edu.unlv.cs.rebelhotel.domain;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;

import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.file.Line;

@RooJavaBean
@RooEntity(finders = {"findMajorsByDegreeCodeAndCatalogTerm"})
public class Major {
	
	private static final Logger LOG = Logger.getLogger(Major.class);
	
	@NotNull
	private String degreeCode;

	@ManyToOne(cascade = {CascadeType.MERGE})
    private Term catalogTerm;

	public Major(){}

	public Major(String degreeCode, Term catalogTerm) {
		this.degreeCode = degreeCode;
		this.catalogTerm = catalogTerm;
	}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDegreeCode()).append(", ");
        return sb.toString();
    }
    
    @PrePersist
    public void createNewMajor() {
		//LOG.debug("Created a new major: " + toString() + ", " + catalogTerm.toString());
    }
}
