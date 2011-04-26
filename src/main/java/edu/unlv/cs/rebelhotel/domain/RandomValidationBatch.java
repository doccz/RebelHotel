package edu.unlv.cs.rebelhotel.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooEntity
public class RandomValidationBatch {
	private long	batch_number;
	@DateTimeFormat(style="S-")
	private Date	batch_run_date;
	private int		num_pending;
	private int		num_validated;
	private int 	num_failed_validation;
	private int   	num_no_validation;
	private int		sample_size;
	private boolean is_completed;
	
	@OneToMany(cascade = CascadeType.ALL )
	private Set<Student> students = new HashSet<Student>();
	
}