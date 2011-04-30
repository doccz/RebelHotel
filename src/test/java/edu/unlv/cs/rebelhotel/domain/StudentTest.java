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
	
	private Term springTerm;
	private Term summerTerm;
	private Term fallTerm;
	private Integer termYear = 2010;

	private Set<WorkEffort> workHistory;
	private Set<Major> majors;

	private WorkEffort multipleMatchingWorkEffort;

	@Before
	public void setup(){
		instance = new Student();
		
		major1 = new Major();
		major1.setDegreeCode(DEGREE_CODE1);
		summerTerm = new Term();
		summerTerm.setTermYear(termYear);
		summerTerm.setSemester(Semester.SUMMER);
		major1.setCatalogTerm(summerTerm);
		
		major2 = new Major();
		major2.setDegreeCode(DEGREE_CODE2);
		summerTerm = new Term();
		summerTerm.setTermYear(termYear);
		summerTerm.setSemester(Semester.SUMMER);
		major2.setCatalogTerm(summerTerm);
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
		matchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE1);
		matchingCatalogRequirement.setStartTerm(springTerm);
		matchingCatalogRequirement.setEndTerm(fallTerm);
		matchingCatalogRequirement.setTotalRelatedHoursNeeded(2);
		matchingCatalogRequirements.add(matchingCatalogRequirement);
		matchingWorkEffort.setCatalogRequirements(matchingCatalogRequirements);
		matchingWorkEffort.setDuration(duration);
		matchingWorkEffort.setValidation(noValidation);
		matchingWorkEffort.setVerification(accepted);
		matchingCatalogRequirement.persist();
		//matchingCatalogRequirement.flush();
		
		matchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement2 = new CatalogRequirement();
		matchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE1);
		matchingCatalogRequirement2.setStartTerm(springTerm);
		matchingCatalogRequirement2.setEndTerm(fallTerm);
		matchingCatalogRequirement2.setTotalRelatedHoursNeeded(2);
		matchingCatalogRequirements2.add(matchingCatalogRequirement2);
		matchingWorkEffort2.setCatalogRequirements(matchingCatalogRequirements2);
		matchingWorkEffort2.setDuration(duration);
		matchingWorkEffort2.setValidation(noValidation);
		matchingWorkEffort2.setVerification(accepted);
		matchingCatalogRequirement2.persist();
		//matchingCatalogRequirement2.flush();
		
		nonMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement = new CatalogRequirement();
		nonMatchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE2);
		nonMatchingCatalogRequirement.setStartTerm(springTerm);
		nonMatchingCatalogRequirement.setEndTerm(fallTerm);
		nonMatchingCatalogRequirement.setTotalRelatedHoursNeeded(2);
		nonMatchingCatalogRequirements.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort.setCatalogRequirements(nonMatchingCatalogRequirements);
		nonMatchingWorkEffort.setDuration(duration);
		nonMatchingWorkEffort.setValidation(noValidation);
		nonMatchingWorkEffort.setVerification(accepted);
		nonMatchingCatalogRequirement.persist();
		//nonMatchingCatalogRequirement.flush();
		
		nonMatchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement2 = new CatalogRequirement();
		nonMatchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE3);
		nonMatchingCatalogRequirement2.setStartTerm(springTerm);
		nonMatchingCatalogRequirement2.setEndTerm(fallTerm);
		nonMatchingCatalogRequirement2.setTotalRelatedHoursNeeded(2);
		nonMatchingCatalogRequirements2.add(nonMatchingCatalogRequirement2);
		nonMatchingWorkEffort2.setCatalogRequirements(nonMatchingCatalogRequirements2);
		nonMatchingWorkEffort2.setDuration(duration);
		nonMatchingWorkEffort2.setValidation(noValidation);
		nonMatchingWorkEffort2.setVerification(accepted);
		nonMatchingCatalogRequirement2.persist();
		//nonMatchingCatalogRequirement2.flush();
		
		multipleMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> multipleMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement multipleMatchingCatalogRequirement = new CatalogRequirement();
		multipleMatchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE1);
		multipleMatchingCatalogRequirement.setStartTerm(springTerm);
		multipleMatchingCatalogRequirement.setEndTerm(fallTerm);
		multipleMatchingCatalogRequirement.setTotalRelatedHoursNeeded(2);
		CatalogRequirement multipleMatchingCatalogRequirement2 = new CatalogRequirement();
		multipleMatchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE2);
		multipleMatchingCatalogRequirement2.setStartTerm(springTerm);
		multipleMatchingCatalogRequirement2.setEndTerm(fallTerm);
		multipleMatchingCatalogRequirement2.setTotalRelatedHoursNeeded(2);
		multipleMatchingCatalogRequirements.add(multipleMatchingCatalogRequirement);
		multipleMatchingCatalogRequirements.add(multipleMatchingCatalogRequirement2);
		multipleMatchingWorkEffort.setCatalogRequirements(multipleMatchingCatalogRequirements);
		multipleMatchingWorkEffort.setDuration(duration);
		multipleMatchingWorkEffort.setValidation(noValidation);
		multipleMatchingWorkEffort.setVerification(accepted);
		multipleMatchingCatalogRequirement.persist();
		//multipleMatchingCatalogRequirement.flush();
		multipleMatchingCatalogRequirement2.persist();
		//multipleMatchingCatalogRequirement2.flush();
		
		workHistory = new HashSet<WorkEffort>();
		majors = new HashSet<Major>();
	}
	
	@Test
	public void singleMajorWithSingleApplicableWorkEffort(){
		Progress progress = new Progress(); 
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setRelatedHours(1);
		progress.setRemainingHours(1);
		progress.setTotalHours(1);
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		
		majors.add(major1);
		workHistory.add(matchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("One major, so one progress with approved hours set to duration should exist", actual.equals(expected));
	}
	
	@Test
	public void singleMajorWithSingleNonApplicableWorkEffort(){
		Progress progress = new Progress(); 
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setRelatedHours(0);
		progress.setRemainingHours(2);
		progress.setTotalHours(1);

		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		
		majors.add(major1);
		workHistory.add(nonMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("One major, so one progress with general hours set to duration should exist", actual.equals(expected));
	}
	
	@Test
	public void singleMajorWithMultipleApplicableWorkEfforts(){
		Progress progress = new Progress(); 
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setRelatedHours(2);
		progress.setRemainingHours(0);
		progress.setTotalHours(2);

		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		
		majors.add(major1);
		workHistory.add(matchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("One major, so one progress with approved hours set to 2 durations should exist", actual.equals(expected));
	}
	
	@Test
	public void singleMajorWithMultipleNonApplicableWorkEfforts(){
		Progress progress = new Progress();
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setRelatedHours(0);
		progress.setRemainingHours(2);
		progress.setTotalHours(2);

		
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		majors.add(major1);
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("One major, so one progress with general hours set to 2 durations should exist", actual.equals(expected));
	}
	
	@Test
	public void singleMajorWithOneApplicableAndOneNonApplicableWorkEfforts(){
		Progress progress = new Progress();
		progress.setDegreeCode(DEGREE_CODE1);
		progress.setRelatedHours(1);
		progress.setRemainingHours(1);
		progress.setTotalHours(2);

		
		Set<Progress> expected = new HashSet<Progress>();
		expected.add(progress);
		majors.add(major1);
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("One progress with both approved and general hours set to duration, and total hours to 2 durations", actual.equals(expected));
	}
	
	@Test
	public void multipleMajorsWithSingleApplicableWorkEffortForAll(){
		Progress major1Progress = new Progress(); 
		major1Progress.setDegreeCode(DEGREE_CODE1);
		major1Progress.setRelatedHours(1);
		major1Progress.setRemainingHours(1);
		major1Progress.setTotalHours(1);
		
		Progress major2Progress = new Progress(); 
		major2Progress.setDegreeCode(DEGREE_CODE2);
		major2Progress.setRelatedHours(1);
		major2Progress.setRemainingHours(1);
		major2Progress.setTotalHours(1);

		Set<Progress> expected = new HashSet<Progress>();
		expected.add(major1Progress);
		expected.add(major2Progress);
		
		majors.add(major1);
		majors.add(major2);
		workHistory.add(multipleMatchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Two progresses with approved hours set to duration for each", actual.equals(expected));
	}
	
	@Test
	public void multipleMajorsWithSingleApplicableWorkEffortForOne(){
		Progress major1Progress = new Progress(); 
		major1Progress.setDegreeCode(DEGREE_CODE1);
		major1Progress.setRelatedHours(1);
		major1Progress.setRemainingHours(1);
		major1Progress.setTotalHours(1);

		Progress major2Progress = new Progress();
		major2Progress.setDegreeCode(DEGREE_CODE2);
		major2Progress.setRelatedHours(0);
		major2Progress.setRemainingHours(2);
		major2Progress.setTotalHours(1);

		Set<Progress> expected = new HashSet<Progress>();
		expected.add(major1Progress);
		expected.add(major2Progress);
		
		majors.add(major1);
		majors.add(major2);
		workHistory.add(matchingWorkEffort);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Two progresses, one with approved hours set to duration, and the other with general hours set to duration should exist", actual.equals(expected));
	}
	
	@Test
	public void multipleMajorsWithSingleNonApplicableWorkEffortForBoth(){
		Progress major1Progress = new Progress();
		major1Progress.setDegreeCode(DEGREE_CODE1);
		major1Progress.setRelatedHours(0);
		major1Progress.setRemainingHours(2);
		major1Progress.setTotalHours(1);

		Progress major2Progress = new Progress(); 
		major2Progress.setDegreeCode(DEGREE_CODE2);
		major2Progress.setRelatedHours(0);
		major2Progress.setRemainingHours(2);
		major2Progress.setTotalHours(1);

		Set<Progress> expected = new HashSet<Progress>();
		expected.add(major1Progress);
		expected.add(major2Progress);
		
		majors.add(major1);
		majors.add(major2);
		workHistory.add(nonMatchingWorkEffort2);
		instance.setMajors(majors);
		instance.setWorkEffort(workHistory);
		
		Set<Progress> actual = instance.calculateProgress();
		
		assertTrue("Two progresses with approved hours set to duration for each", actual.equals(expected));
	}
	
	public boolean compareSets(Set<Progress> set1, Set<Progress> set2){
		boolean areEqual = true;
		boolean contains;
		
		for(Progress progress1 : set1){
			contains = false;
			for(Progress progress2 : set2){
				contains |= progress2.equals(progress1);
			}
			areEqual &= contains;
		}
		
		return areEqual;
	}

}
