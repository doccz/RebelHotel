package edu.unlv.cs.rebelhotel.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;



public class StudentTest {
	
	private Student instance;
	
	private static final int DURATION = 1;

	private Major major1;
	private String DEGREE_CODE1 = "GAM";
	private String DEGREE_CODE2 = "HOS";
	private String DEGREE_CODE3 = "HOT";
	private Major major2;
	
	private WorkEffort matchingWorkEffort;
	private WorkEffort matchingWorkEffort2;

	private WorkEffort nonMatchingWorkEffort2;
	private WorkEffort nonMatchingWorkEffort;
	
	private Set<WorkEffort> workHistory;
	private Set<Major> majors;

	private WorkEffort multipleMatchingWorkEffort;

	@Before
	public void setup(){
		instance = new Student();
		
		major1 = new Major();
		major1.setDegreeCode(DEGREE_CODE1);
		
		major2 = new Major();
		major2.setDegreeCode(DEGREE_CODE2);
		
		WorkEffortDuration duration = new WorkEffortDuration();
		duration.setHours(DURATION);
		
		Validation noValidation = Validation.NO_VALIDATION;
		Verification accepted = Verification.ACCEPTED;
		
		matchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement = new CatalogRequirement();
		matchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE1);
		matchingCatalogRequirements.add(matchingCatalogRequirement);
		matchingWorkEffort.setCatalogRequirements(matchingCatalogRequirements);
		matchingWorkEffort.setDuration(duration);
		matchingWorkEffort.setValidation(noValidation);
		matchingWorkEffort.setVerification(accepted);
		
		matchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement2 = new CatalogRequirement();
		matchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE1);
		matchingCatalogRequirements2.add(matchingCatalogRequirement2);
		matchingWorkEffort2.setCatalogRequirements(matchingCatalogRequirements2);
		matchingWorkEffort2.setDuration(duration);
		matchingWorkEffort2.setValidation(noValidation);
		matchingWorkEffort2.setVerification(accepted);
		
		nonMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement = new CatalogRequirement();
		nonMatchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE2);
		nonMatchingCatalogRequirements.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort.setCatalogRequirements(nonMatchingCatalogRequirements);
		nonMatchingWorkEffort.setDuration(duration);
		nonMatchingWorkEffort.setValidation(noValidation);
		nonMatchingWorkEffort.setVerification(accepted);
		
		nonMatchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement2 = new CatalogRequirement();
		nonMatchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE3);
		nonMatchingCatalogRequirements2.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort2.setCatalogRequirements(nonMatchingCatalogRequirements2);
		nonMatchingWorkEffort2.setDuration(duration);
		nonMatchingWorkEffort2.setValidation(noValidation);
		nonMatchingWorkEffort2.setVerification(accepted);
		
		
		multipleMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> multipleMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement multipleMatchingCatalogRequirement = new CatalogRequirement();
		CatalogRequirement multipleMatchingCatalogRequirement2 = new CatalogRequirement();
		multipleMatchingCatalogRequirements.add(matchingCatalogRequirement);
		multipleMatchingCatalogRequirements.add(nonMatchingCatalogRequirement);
		multipleMatchingWorkEffort.setCatalogRequirements(multipleMatchingCatalogRequirements);
		multipleMatchingWorkEffort.setDuration(duration);
		multipleMatchingWorkEffort.setValidation(noValidation);
		multipleMatchingWorkEffort.setVerification(accepted);
		
		workHistory = new HashSet<WorkEffort>();
		majors = new HashSet<Major>();
	}
	
	@Test
	public void singleMajorWithSingleApplicableWorkEffort(){
		Progress progress = new Progress(DEGREE_CODE1, 1, 0);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		
		majors.add(major1);
		workHistory.add(matchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since one major for student, one progres should exist", actual.size() == 1);
	}
	
	public void singleMajorWithSingleNonApplicableWorkEffort(){
		Progress progress = new Progress(DEGREE_CODE1, 0, 0);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		
		majors.add(major1);
		workHistory.add(nonMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since one major for student, one progres should exist", actual.size() == 1);
	}
	
	@Test
	public void singleMajorWithMultipleApplicableWorkEfforts(){
		Progress progress = new Progress(DEGREE_CODE1, 2, 0);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		
		majors.add(major1);
		workHistory.add(matchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since one major for student, one progres with sum of durations should exist", actual.size() == 1);
	}
	
	@Test
	public void singleMajorWithMultipleNonApplicableWorkEfforts(){
		Progress progress = new Progress();
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setApprovedHours(0);
		progress.setRemainingHours(0);
		
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		majors.add(major1);
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since one major, one progres with duration of zero should exist", actual.equals(expected));
	}
	
	@Test
	public void singleMajorWithOneApplicableAndOneNonApplicableWorkEfforts(){
		Progress progress = new Progress();
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setApprovedHours(0);
		progress.setRemainingHours(0);
		
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		majors.add(major1);
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since one major, one progress with duration of one should exist", actual.equals(expected));
	}
	
	@Test
	public void multipleMajorsWithSingleApplicableWorkEffortForAll(){
		Progress major1Progress = new Progress(DEGREE_CODE1, 1, 0);
		Progress major2Progress = new Progress(DEGREE_CODE2, 1, 0);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(major1Progress);
		expected.add(major2Progress);
		
		majors.add(major1);
		majors.add(major2);
		workHistory.add(multipleMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since two majors with single applicable work effort for both, 2 progresses with duration of 1 for both should exist", actual.size() == 2);
	}
	
	@Test
	public void multipleMajorsWithSingleApplicableWorkEffortForOne(){
		Progress major1Progress = new Progress(DEGREE_CODE1, 1, 0);
		Progress major2Progress = new Progress(DEGREE_CODE2, 0, 0);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(major1Progress);
		expected.add(major2Progress);
		
		majors.add(major1);
		majors.add(major2);
		workHistory.add(matchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since two majors with single applicable work effort for one, 2 progresses with duration of 0 for one should exist", actual.equals(expected));
	}
	
	@Test
	public void multipleMajorsWithSingleNonApplicableWorkEffortForOne(){
		Progress major1Progress = new Progress(DEGREE_CODE1, 0, 0);
		Progress major2Progress = new Progress(DEGREE_CODE2, 0, 0);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(major1Progress);
		expected.add(major2Progress);
		
		majors.add(major1);
		majors.add(major2);
		workHistory.add(nonMatchingWorkEffort2);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Since two majors with single non applicable work effort for both, 2 progresses with duration of 0 for both should exist", actual.equals(expected));
	}

}
