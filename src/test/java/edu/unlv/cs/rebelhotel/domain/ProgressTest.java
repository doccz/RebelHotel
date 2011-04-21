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


public class ProgressTest {
	
	private static final int DURATION = 1;
	private static final String DEGREE_CODE = "GAM";
	private static final String DEGREE_CODE_NON = "HOS";

	//private Progress instance;
	private Major major;
	private WorkEffort matchingWorkEffort;
	private WorkEffort nonMatchingWorkEffort;
	
	private Set<WorkEffort> workHistory;
	private WorkEffort matchingWorkEffort2;
	private WorkEffort nonMatchingWorkEffort2;
	
	private Term springTerm;
	private Term summerTerm;
	private Term fallTerm;
	
	private Integer termYear = 2010;
	
	@Before
	public void setup(){
		//instance = new Progress();
		
		major = new Major();
		major.setDegreeCode(DEGREE_CODE);
		summerTerm = new Term();
		summerTerm.setTermYear(termYear);
		summerTerm.setSemester(Semester.SUMMER);
		major.setCatalogTerm(summerTerm);
		summerTerm.persist();
		
		
		springTerm = new Term();
		springTerm.setTermYear(termYear);
		springTerm.setSemester(Semester.SPRING);
		springTerm.persist();
		
		fallTerm = new Term();
		fallTerm.setTermYear(termYear);
		fallTerm.setSemester(Semester.FALL);
		fallTerm.persist();
		
		WorkEffortDuration duration = new WorkEffortDuration();
		duration.setHours(DURATION);
		
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
		matchingCatalogRequirement.flush();
		
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
		nonMatchingCatalogRequirement.flush();
		
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
		matchingCatalogRequirement2.flush();
		
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
		nonMatchingCatalogRequirement2.flush();
		
		workHistory = new HashSet<WorkEffort>();
	}
	
	@Test
	public void oneApplicableWorkEffort(){
		Progress expected = new Progress();
		expected.setDegreeCode(DEGREE_CODE);
		expected.setRelatedHours(1);
		expected.setRemainingHours(1);
		expected.setTotalHours(1);
		
		workHistory.add(matchingWorkEffort);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("one applicable work effort should have duration approved hours", expected.equals(actual));
	}
	
	@Test
	public void oneNonApplicableWorkEffort(){
		Progress expected = new Progress(); 
		expected.setDegreeCode(DEGREE_CODE);
		expected.setRelatedHours(0);
		expected.setRemainingHours(2);
		expected.setTotalHours(1);
		
		workHistory.add(nonMatchingWorkEffort);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("one non-applicable work effort should set general hours to duration", expected.equals(actual));
	}
	
	@Test
	public void multipleApplicableWorkEfforts(){
		Progress expected = new Progress(); 
		expected.setDegreeCode(DEGREE_CODE);
		expected.setRelatedHours(2);
		expected.setRemainingHours(0);
		expected.setTotalHours(2);
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("Multiple applicable work efforts should have 2*duration approved hours", expected.equals(actual));
	}
	
	@Test
	public void multipleNonApplicableWorkEfforts(){
		Progress expected = new Progress(); 
		expected.setDegreeCode(DEGREE_CODE);
		expected.setRelatedHours(0);
		expected.setRemainingHours(2);
		expected.setTotalHours(2);
		
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("Multiple non-applicable work efforts should set general hours to 2 durations", expected.equals(actual));
	}
	
	@Test
	public void oneApplicableAndOneNonApplicableWorkEfforts(){
		Progress expected = new Progress(); 
		expected.setDegreeCode(DEGREE_CODE);
		expected.setRelatedHours(1);
		expected.setRemainingHours(1);
		expected.setTotalHours(2);
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("1 applicable and 1 non-applicable work efforts should set approved hours to duration and general hours to duration", expected.equals(actual));
	}
	
	@Test
	public void noWorkEfforts(){
		Progress expected = new Progress(); 
		expected.setDegreeCode(DEGREE_CODE);
		expected.setRelatedHours(0);
		expected.setRemainingHours(2);
		expected.setTotalHours(0);
		
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("no work efforts should set approved hours and general hours to zero", expected.equals(actual));
	}

}
