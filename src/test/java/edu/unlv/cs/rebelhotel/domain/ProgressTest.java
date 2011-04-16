package edu.unlv.cs.rebelhotel.domain;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;


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
	
	@Before
	public void setup(){
		//instance = new Progress();
		
		major = new Major();
		major.setDegreeCode(DEGREE_CODE);
		
		WorkEffortDuration duration = new WorkEffortDuration();
		duration.setHours(DURATION);
		
		Validation noValidation = Validation.NO_VALIDATION;
		Verification accepted = Verification.ACCEPTED;
		
		matchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement = new CatalogRequirement();
		matchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE);
		matchingCatalogRequirements.add(matchingCatalogRequirement);
		matchingWorkEffort.setCatalogRequirements(matchingCatalogRequirements);
		matchingWorkEffort.setDuration(duration);
		matchingWorkEffort.setValidation(noValidation);
		matchingWorkEffort.setVerification(accepted);
		
		
		nonMatchingWorkEffort = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement = new CatalogRequirement();
		nonMatchingCatalogRequirement.setDegreeCodePrefix(DEGREE_CODE_NON);
		nonMatchingCatalogRequirements.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort.setCatalogRequirements(nonMatchingCatalogRequirements);
		nonMatchingWorkEffort.setDuration(duration);
		nonMatchingWorkEffort.setValidation(noValidation);
		nonMatchingWorkEffort.setVerification(accepted);
		
		matchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> matchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement matchingCatalogRequirement2 = new CatalogRequirement();
		matchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE);
		matchingCatalogRequirements2.add(matchingCatalogRequirement2);
		matchingWorkEffort2.setCatalogRequirements(matchingCatalogRequirements2);
		matchingWorkEffort2.setDuration(duration);
		matchingWorkEffort2.setValidation(noValidation);
		matchingWorkEffort2.setVerification(accepted);
		
		nonMatchingWorkEffort2 = new WorkEffort();
		Set<CatalogRequirement> nonMatchingCatalogRequirements2 = new HashSet<CatalogRequirement>();
		CatalogRequirement nonMatchingCatalogRequirement2 = new CatalogRequirement();
		nonMatchingCatalogRequirement2.setDegreeCodePrefix(DEGREE_CODE_NON);
		nonMatchingCatalogRequirements2.add(nonMatchingCatalogRequirement);
		nonMatchingWorkEffort2.setCatalogRequirements(nonMatchingCatalogRequirements2);
		nonMatchingWorkEffort2.setDuration(duration);
		nonMatchingWorkEffort2.setValidation(noValidation);
		nonMatchingWorkEffort2.setVerification(accepted);
		
		workHistory = new HashSet<WorkEffort>();
	}
	
	@Test
	public void oneApplicableWorkEffort(){
		Progress expected = new Progress(DEGREE_CODE, 1, 0);
		
		workHistory.add(matchingWorkEffort);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("", expected.getDegreeCode().equals(actual.getDegreeCode()) && expected.getApprovedHours().equals(actual.getApprovedHours()) && expected.getRemainingHours().equals(actual.getRemainingHours()));
	}
	
	@Test
	public void oneNonApplicableWorkEffort(){
		Progress expected = new Progress(DEGREE_CODE, 0, 0);
		
		workHistory.add(nonMatchingWorkEffort);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("", expected.getDegreeCode().equals(actual.getDegreeCode()) && expected.getApprovedHours().equals(actual.getApprovedHours()) && expected.getRemainingHours().equals(actual.getRemainingHours()));
	}
	
	@Test
	public void multipleApplicableWorkEfforts(){
		Progress expected = new Progress(DEGREE_CODE, 2, 0);
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(matchingWorkEffort2);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("", expected.getDegreeCode().equals(actual.getDegreeCode()) && expected.getApprovedHours().equals(actual.getApprovedHours()) && expected.getRemainingHours().equals(actual.getRemainingHours()));
	}
	
	@Test
	public void multipleNonApplicableWorkEfforts(){
		Progress expected = new Progress(DEGREE_CODE, 0, 0);
		
		workHistory.add(nonMatchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("", expected.getDegreeCode().equals(actual.getDegreeCode()) && expected.getApprovedHours().equals(actual.getApprovedHours()) && expected.getRemainingHours().equals(actual.getRemainingHours()));
	}
	
	@Test
	public void oneApplicableAndOneNonApplicableWorkEfforts(){
		Progress expected = new Progress(DEGREE_CODE, 1, 0);
		
		workHistory.add(matchingWorkEffort);
		workHistory.add(nonMatchingWorkEffort2);
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("", expected.getDegreeCode().equals(actual.getDegreeCode()) && expected.getApprovedHours().equals(actual.getApprovedHours()) && expected.getRemainingHours().equals(actual.getRemainingHours()));
	}
	
	@Test
	public void noWorkEfforts(){
		Progress expected = new Progress(DEGREE_CODE, 0, 0);
		
		Progress actual = new Progress(major, workHistory);
		
		assertTrue("", expected.getDegreeCode().equals(actual.getDegreeCode()) && expected.getApprovedHours().equals(actual.getApprovedHours()) && expected.getRemainingHours().equals(actual.getRemainingHours()));
	}

}
