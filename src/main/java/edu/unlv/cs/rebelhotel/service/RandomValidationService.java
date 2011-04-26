package edu.unlv.cs.rebelhotel.service;

import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;

import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;


public interface RandomValidationService {
	public int generateRandomIndex(int num_jobs);
	
	public List<WorkEffort> getRandomValidationList(Term current_term, int num_jobs_requested);
	
	public Semester getSemester(String sem);
	
	public boolean inputIsValid(String year, String sample_size);
}
