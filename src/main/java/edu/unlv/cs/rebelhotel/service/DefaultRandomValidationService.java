package edu.unlv.cs.rebelhotel.service;

import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
public class DefaultRandomValidationService implements RandomValidationService {
	
	Random random_index = new Random();
	
	/* ========================== 
	 * generateRandomIndex()
	 * Alan Chapman
	 * Last Updated: 4-18-2011
	 * ==========================
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public int generateRandomIndex(int num_jobs) {
		int    next_index;
		next_index = random_index.nextInt(num_jobs);
		return next_index;
	}
	
	/* ==========================
	 * getRandomValidationList()
	 * Alan Chapman
	 * Last Updated: 4-18-2011
	 * ==========================
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public List<WorkEffort> getRandomValidationList(Term current_term, int num_jobs_requested) {
		List<WorkEffort> randomValidationList = WorkEffort.findWorkEffortsByValidationAndTermSubmitted(Validation.NO_VALIDATION, current_term).getResultList();
		
		List<WorkEffort> resultList = Collections.emptyList();
		int curr_index;
		int i = 0;
		int num_jobs = num_jobs_requested;
		int num_jobs_returned = randomValidationList.size();
		
		if (num_jobs_returned < num_jobs_requested) {
			num_jobs = num_jobs_returned;
		}
		
		boolean notFull = !(resultList.size() == num_jobs);
		
		while (notFull) {
			curr_index = this.generateRandomIndex(num_jobs);
			
			resultList.add(i , randomValidationList.get(curr_index));
			
			notFull = !(resultList.size() == num_jobs);
			i++;
		}
		
		return resultList;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public Semester getSemester(String sem) {
		Semester tempSem;
		
		if (sem.equals("SPRING")) {
			tempSem = Semester.SPRING;			
		}
		else if (sem.equals("SUMMER")) {
			tempSem = Semester.SUMMER;
		}
		else if (sem.equals("FALL")) {
			tempSem = Semester.FALL;
		}
		else
			throw new IllegalArgumentException("sem must be a valid Semester");
		
			return tempSem;
	}
	
}