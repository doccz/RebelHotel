package edu.unlv.cs.rebelhotel.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;

public class MajorTest {

	private static final int DURATION = 1;
	private static final String DEGREE_CODE = "GAM";
	private static final String DEGREE_CODE_NON = "HOS";
	private Major instance;
	private WorkEffort matchingWorkEffort;
	private WorkEffort matchingWorkEffort2;

	private WorkEffort nonMatchingWorkEffort2;
	private WorkEffort nonMatchingWorkEffort;
	
	private Term springTerm;
	private Term summerTerm;
	private Term fallTerm;
	
	private Integer termYear = 2010;
	
	private Set<WorkEffort> workHistory;
	@Before
	public void setUp(){
		instance = new Major();
		instance.setDegreeCode(DEGREE_CODE);
		summerTerm = new Term();
		summerTerm.setTermYear(termYear);
		summerTerm.setSemester(Semester.SUMMER);
		instance.setCatalogTerm(summerTerm);
		
		WorkEffortDuration duration = new WorkEffortDuration();
		duration.setHours(DURATION);
		
		springTerm = new Term();
		springTerm.setTermYear(termYear);
		springTerm.setSemester(Semester.SPRING);
		fallTerm = new Term();
		fallTerm.setTermYear(termYear);
		fallTerm.setSemester(Semester.FALL);
		
		Validation noValidation = Validation.NO_VALIDATION;
		Verification accepted = Verification.ACCEPTED;
		
		matchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement = new CatalogRequirement();
		matchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE);
		matchingCatalogRequirement.setStartTerm(springTerm);
		matchingCatalogRequirement.setEndTerm(fallTerm);
		matchingCatalogRequirements.add(matchingCatalogRequirement);
		matchingWorkEffort.setCatalogRequirements(matchingCatalogRequirements);
		matchingWorkEffort.setDuration(duration);
		matchingWorkEffort.setValidation(noValidation);
		matchingWorkEffort.setVerification(accepted);
		
		matchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement2 = new CatalogRequirement();
		matchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE);
		matchingCatalogRequirement2.setStartTerm(springTerm);
		matchingCatalogRequirement2.setEndTerm(fallTerm);
		matchingCatalogRequirements2.add(matchingCatalogRequirement2);
		matchingWorkEffort2.setCatalogRequirements(matchingCatalogRequirements2);
		matchingWorkEffort2.setDuration(duration);
		matchingWorkEffort2.setValidation(noValidation);
		matchingWorkEffort2.setVerification(accepted);
		
		nonMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement = new CatalogRequirement();
		nonMatchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE_NON);
		nonMatchingCatalogRequirement.setStartTerm(springTerm);
		nonMatchingCatalogRequirement.setEndTerm(fallTerm);
		nonMatchingCatalogRequirements.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort.setCatalogRequirements(nonMatchingCatalogRequirements);
		nonMatchingWorkEffort.setDuration(duration);
		nonMatchingWorkEffort.setValidation(noValidation);
		nonMatchingWorkEffort.setVerification(accepted);
		
		nonMatchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement2 = new CatalogRequirement();
		nonMatchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE_NON);
		nonMatchingCatalogRequirement2.setStartTerm(springTerm);
		nonMatchingCatalogRequirement2.setEndTerm(fallTerm);
		nonMatchingCatalogRequirements2.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort2.setCatalogRequirements(nonMatchingCatalogRequirements2);
		nonMatchingWorkEffort2.setDuration(duration);
		nonMatchingWorkEffort2.setValidation(noValidation);
		nonMatchingWorkEffort2.setVerification(accepted);
		
		workHistory = new HashSet<WorkEffort>();
		
	}
	@Test
	public void workHoursZeroForEmptySet(){
		int expected = 0;
		int actual = instance.calculateRelatedHoursWorked(workHistory);
		assertEquals("An empty work history should be zero", expected, actual);
	}
	
	@Test
	public void workHoursForSingleApplicable(){
		int expected = DURATION;
		workHistory.add(matchingWorkEffort);
		int actual = instance.calculateRelatedHoursWorked(workHistory);
		
		assertEquals("One applicable work effort should be duration", expected, actual);
	}
	
	@Test
	public void workHoursForSingleNonApplicable(){
		int expected = 0;
		workHistory.add(nonMatchingWorkEffort);
		int actual = instance.calculateRelatedHoursWorked(workHistory);
		
		assertEquals("One non applicable work effort should be zero", expected, actual);
	}
	
	@Test
	public void workHoursForOneApplicableAndOneNon(){
		int expected = DURATION;
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort);
		int actual = instance.calculateRelatedHoursWorked(workHistory);
		
		assertEquals("Two work efforts, one applicable and the other non applicable, should be durations", expected, actual);
	}
	
	@Test
	public void workHoursForMultipleApplicable(){
		int expected = 2*DURATION;
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		int actual = instance.calculateRelatedHoursWorked(workHistory);
		
		assertEquals("Two applicable work efforts should be the sum of durations", expected, actual);
	}
	
	@Test
	public void workHoursForMultipleNonApplicable(){
		int expected = 0;
		
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		int actual = instance.calculateRelatedHoursWorked(workHistory);
		
		assertEquals("Two applicable work efforts should be the sum of durations", expected, actual);
	}
	
}
