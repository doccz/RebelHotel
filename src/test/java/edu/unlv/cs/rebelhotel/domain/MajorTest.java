package edu.unlv.cs.rebelhotel.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")

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
		summerTerm.persist();
		
		WorkEffortDuration duration = new WorkEffortDuration();
		duration.setHours(DURATION);
		
		springTerm = new Term();
		springTerm.setTermYear(termYear);
		springTerm.setSemester(Semester.SPRING);
		springTerm.persist();
		
		fallTerm = new Term();
		fallTerm.setTermYear(termYear);
		fallTerm.setSemester(Semester.FALL);
		fallTerm.persist();
		
		Validation noValidation = Validation.NO_VALIDATION;
		Verification accepted = Verification.ACCEPTED;
		
		matchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement = new CatalogRequirement();
		matchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE);
		matchingCatalogRequirement.setStartTerm(springTerm);
		matchingCatalogRequirement.setEndTerm(fallTerm);
		matchingCatalogRequirement.setTotalRelatedHoursNeeded(2);
		matchingCatalogRequirements.add(matchingCatalogRequirement);
		matchingWorkEffort.setCatalogRequirements(matchingCatalogRequirements);
		matchingWorkEffort.setDuration(duration);
		matchingWorkEffort.setValidation(noValidation);
		matchingWorkEffort.setVerification(accepted);
		matchingCatalogRequirement.persist();
		
		matchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement2 = new CatalogRequirement();
		matchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE);
		matchingCatalogRequirement2.setStartTerm(springTerm);
		matchingCatalogRequirement2.setEndTerm(fallTerm);
		matchingCatalogRequirement2.setTotalRelatedHoursNeeded(2);
		matchingCatalogRequirements2.add(matchingCatalogRequirement2);
		matchingWorkEffort2.setCatalogRequirements(matchingCatalogRequirements2);
		matchingWorkEffort2.setDuration(duration);
		matchingWorkEffort2.setValidation(noValidation);
		matchingWorkEffort2.setVerification(accepted);
		matchingCatalogRequirement2.persist();
		
		nonMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement = new CatalogRequirement();
		nonMatchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE_NON);
		nonMatchingCatalogRequirement.setStartTerm(springTerm);
		nonMatchingCatalogRequirement.setEndTerm(fallTerm);
		nonMatchingCatalogRequirement.setTotalRelatedHoursNeeded(2);
		nonMatchingCatalogRequirements.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort.setCatalogRequirements(nonMatchingCatalogRequirements);
		nonMatchingWorkEffort.setDuration(duration);
		nonMatchingWorkEffort.setValidation(noValidation);
		nonMatchingWorkEffort.setVerification(accepted);
		nonMatchingCatalogRequirement.persist();
		
		nonMatchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement2 = new CatalogRequirement();
		nonMatchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE_NON);
		nonMatchingCatalogRequirement2.setStartTerm(springTerm);
		nonMatchingCatalogRequirement2.setEndTerm(fallTerm);
		nonMatchingCatalogRequirement2.setTotalRelatedHoursNeeded(2);
		nonMatchingCatalogRequirements2.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort2.setCatalogRequirements(nonMatchingCatalogRequirements2);
		nonMatchingWorkEffort2.setDuration(duration);
		nonMatchingWorkEffort2.setValidation(noValidation);
		nonMatchingWorkEffort2.setVerification(accepted);
		nonMatchingCatalogRequirement2.persist();
		
		workHistory = new HashSet<WorkEffort>();
		
	}
	
	// Tests for related hours calculation
	
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
	
	
	// Tests for remaining hours calculation
	
	@Test
	public void remainingHoursForSingleApplicable(){
		int expected = 1;
		workHistory.add(matchingWorkEffort);
		int actual = instance.calculateHoursRemaining(workHistory);
		
		assertEquals("One applicable work effort should have 1 hour remaining", expected, actual);
	}
	
	@Test
	public void remainingHoursForSingleNonApplicable(){
		int expected = 2;
		workHistory.add(nonMatchingWorkEffort);
		int actual = instance.calculateHoursRemaining(workHistory);
		
		assertEquals("One non applicable work effort should have all required hours remaining", expected, actual);
	}
	
	@Test
	public void remainingHoursForOneApplicableAndOneNon(){
		int expected = 1;
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort);
		int actual = instance.calculateHoursRemaining(workHistory);
		
		assertEquals("Two work efforts, one applicable and the other non applicable, should have 1 hour remaining", expected, actual);
	}
	
	@Test
	public void remainingHoursForMultipleApplicable(){
		int expected = 0;
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		int actual = instance.calculateHoursRemaining(workHistory);
		
		assertEquals("Two applicable work efforts should have all required hours remaining", expected, actual);
	}
	
	@Test
	public void remainingHoursForMultipleNonApplicable(){
		int expected = 2;
		
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		int actual = instance.calculateHoursRemaining(workHistory);
		
		assertEquals("Two applicable work efforts should have 1 hour remaining", expected, actual);
	}
	
	@Test
	public void totalHoursForAllWorkEfforts(){
		int expected = 4;
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		workHistory.add(nonMatchingWorkEffort2);
		int actual = instance.calculateTotalHours(workHistory);
		
		assertEquals("This should be the sum of all work efforts durations of that student (4)", expected, actual);
	}
}
