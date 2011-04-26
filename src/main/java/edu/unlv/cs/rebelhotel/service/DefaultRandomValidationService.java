package edu.unlv.cs.rebelhotel.service;

import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;

import java.util.ArrayList;
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
	 * Last Updated: 4-24-2011
	 * ==========================
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public List<WorkEffort> getRandomValidationList(Term current_term, int num_jobs_requested) {
		List<WorkEffort> randomValidationList = WorkEffort.findWorkEffortsByValidationAndTermSubmitted(Validation.NO_VALIDATION, current_term).getResultList();
		List<WorkEffort> resultList = new ArrayList<WorkEffort>();
		int 			 curr_index;
		int 		 	 num_jobs_returned = randomValidationList.size();
		int				 jobs_remaining = num_jobs_returned;
		int  			 num_jobs_until_result_list_is_full;
		boolean   		 result_list_is_not_full;
		boolean 		 result_list_does_not_contain_job;
		
		// this if-else block ensures the result list only grows as much as needed
		if (num_jobs_returned <= num_jobs_requested) {
			num_jobs_until_result_list_is_full = num_jobs_returned;
		}
		else { // more jobs were returned than were requested
			num_jobs_until_result_list_is_full = num_jobs_requested;
		}
		
		result_list_is_not_full = true;
		
		while (result_list_is_not_full) {
			
			curr_index = generateRandomIndex(jobs_remaining);
			result_list_does_not_contain_job = !(resultList.contains(randomValidationList.get(curr_index)));
			
			if (result_list_does_not_contain_job)
			{
				// add job to result list, remove it from the list of jobs to choose from,
				// and subtract 1 from the remaining jobs to generate a random index for.
				resultList.add(randomValidationList.get(curr_index));
				randomValidationList.remove(curr_index);
				--jobs_remaining;
			}
			result_list_is_not_full = !(resultList.size() == num_jobs_until_result_list_is_full);
		}
		
		return resultList;
	}
	
	/* ===========================
	 * getSemester()
	 * Alan Chapman
	 * Last Updated: 4-23-2011
	 * ===========================
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public Semester getSemester(String sem) {
		Semester temp_sem;
		
		if (sem.equals("SPRING")) {
			temp_sem = Semester.SPRING;			
		}
		else if (sem.equals("SUMMER")) {
			temp_sem = Semester.SUMMER;
		}
		else if (sem.equals("FALL")) {
			temp_sem = Semester.FALL;
		}
		else {
			throw new IllegalArgumentException("sem must be a valid Semester");
		}
		
			return temp_sem;
	}
	
	/* =========================================================
	 * inputIsValid: TEMPORARY, very rudimentary form validation
	 * Alan Chapman
	 * Last Updated: 4-24-2011
	 * =========================================================
	 */
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public boolean inputIsValid(String year, String sample_size) {
		int temp_year;
		int temp_size;
		
		try {
		Integer.parseInt(year);
		}
		catch (NumberFormatException e) {
			return false;
		}
		try {
		Integer.parseInt(sample_size);
		}
		catch (NumberFormatException e) {
			return false;
		}
		
		temp_year = Integer.valueOf(year);
		temp_size = Integer.valueOf(sample_size);
		
		if ( temp_year < 1975 || temp_year > 2050) {
			return false;
		}
		else if (temp_size < 1 || temp_size > 100) {
			return false;
		}
		else {
			return true;
		}
	}
	
}