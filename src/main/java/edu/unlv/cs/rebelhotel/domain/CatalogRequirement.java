package edu.unlv.cs.rebelhotel.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import edu.unlv.cs.rebelhotel.domain.Term;

import javax.persistence.ManyToOne;

@RooJavaBean
@RooToString
@RooEntity
public class CatalogRequirement {
	private String degreeCodePrefix;

    @ManyToOne
    private Term startTerm;
    
    @ManyToOne
    private Term endTerm;
    
    private Integer totalHoursNeeded;
    
    private Integer totalRelatedHoursNeeded;

}
