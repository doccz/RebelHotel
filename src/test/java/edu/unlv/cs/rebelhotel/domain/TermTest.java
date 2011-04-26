package edu.unlv.cs.rebelhotel.domain;

import static org.junit.Assert.*;

import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import org.junit.Test;

public class TermTest {

	@Test 
	public void testTermInBetween1(){
		Term instance = new Term(1,Semester.FALL);
		Term start = new Term(1,Semester.FALL);
		Term end = new Term(1,Semester.FALL);
		
		assertTrue("Start and end the same term should be between",instance.isBetween(start, end));
	}
	@Test 
	public void testTermInBetween2(){
		Term instance = new Term(1,Semester.FALL);
		Term start = new Term(1,Semester.SUMMER);
		Term end = new Term(1,Semester.FALL);
		
		assertTrue("Start before and end the same term should be between",instance.isBetween(start, end));
	}
	@Test 
	public void testTermInBetween3(){
		Term instance = new Term(1,Semester.SUMMER);
		Term start = new Term(1,Semester.SPRING);
		Term end = new Term(1,Semester.FALL);
		
		assertTrue("Start before and end after the term should be between",instance.isBetween(start, end));
	}
	@Test 
	public void testTermInBetween4(){
		Term instance = new Term(1,Semester.SUMMER);
		Term start = new Term(1,Semester.FALL);
		Term end = new Term(1,Semester.SPRING);
		
		assertFalse("Start after and end before the term should not be between",instance.isBetween(start, end));
	}
	@Test 
	public void testTermInBetween5(){
		Term instance = new Term(1,Semester.SUMMER);
		Term start = new Term(2,Semester.SPRING);
		Term end = new Term(1,Semester.FALL);
		
		assertFalse("Start after and end before the term should not be between",instance.isBetween(start, end));
	}
}
