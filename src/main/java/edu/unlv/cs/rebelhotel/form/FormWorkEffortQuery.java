package edu.unlv.cs.rebelhotel.form;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import edu.unlv.cs.rebelhotel.domain.enums.VerificationType;
import edu.unlv.cs.rebelhotel.domain.enums.WorkEffortSortOptions;

import javax.persistence.Enumerated;

@RooJavaBean
@RooToString
public class FormWorkEffortQuery {

	private String studentFirstName;
	
	private String studentLastName;
	
	private String studentMiddleName;
	
	private String userId;

	private String employerName;

	private String employerLocation;

	@Enumerated
	private WorkEffortSortOptions sortOptions;
	
	@Enumerated
	private Validation validation;
	private boolean validationSelected;
	
	@Enumerated
	private Verification verification;
	private boolean verificationSelected;
	
	@Enumerated
	private VerificationType verificationType;
	private boolean verificationTypeSelected;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date startDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "S-")
	private Date endDate;


}
