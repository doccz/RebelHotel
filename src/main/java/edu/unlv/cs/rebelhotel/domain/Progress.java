package edu.unlv.cs.rebelhotel.domain;

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

	public void setDegreeCode(String degreeCode) {
		// TODO Auto-generated method stub
		this.degreeCode = degreeCode;
	}

	public void setApprovedHours(Integer approvedHours) {
		// TODO Auto-generated method stub
		this.approvedHours = approvedHours;
		
	}
	
	private String getDegreeCode() {
		// TODO Auto-generated method stub
		return this.degreeCode;
	}
	
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(getDegreeCode()).append(", ");
//        return sb.toString();
//    }

	public Progress(){}
	
	public Progress(String degreeCode, Integer approvedHours, Integer remainingHours) {
		this.degreeCode = degreeCode;
		this.approvedHours = approvedHours;
		this.remainingHours = remainingHours;
	}
	
}
