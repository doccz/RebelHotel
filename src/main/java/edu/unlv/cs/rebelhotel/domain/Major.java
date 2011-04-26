package edu.unlv.cs.rebelhotel.domain;

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

	@NotNull
	private String degreeCode;

	@ManyToOne(cascade = {CascadeType.MERGE})
    private Term catalogTerm;
	
    @NotNull
    @ManyToOne
    private Student student;
    
    private boolean reachedMilestone = Boolean.FALSE;

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
}
