package edu.unlv.cs.rebelhotel.domain;

import java.util.List;

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
    
    public CatalogRequirement(){}
    
    public boolean matchesMajor(Major major) {
    	byte prefixBytes[] = degreeCodePrefix.getBytes();
    	byte majorBytes[] = major.getDegreeCode().getBytes();
    	
    	if (major.getDegreeCode().length() < degreeCodePrefix.length()) {
    		return false;
    	}
    	
    	for (int i = 0; i < degreeCodePrefix.length(); i++) {
    		if (prefixBytes[i] != majorBytes[i]) {
    			return false;
    		}
    	}
    	
    	return true;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getDegreeCodePrefix()).append(": ");
        sb.append(getStartTerm()).append(" - ");
        sb.append(getEndTerm());
        return sb.toString();
    }
    
    public static List<CatalogRequirement> findAllCatalogRequirementsOrderedById() {
        return entityManager().createQuery("select o from CatalogRequirement o order by o.id asc", CatalogRequirement.class).getResultList();
    }
}
