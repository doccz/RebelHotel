package edu.unlv.cs.rebelhotel.file;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class LineTest {
	private static final String VALID_DATA = "1000011622,Holmes,Katherine,Marilyn,HOLMESK4@UNLV.NEVADA.EDU,HOSBSHA,2048, , , , ,2048, ";
	private static final String NO_MAJORS = "1000011622,Holmes,Katherine,Marilyn,HOLMESK4@UNLV.NEVADA.EDU, , , , , , ,2048, ";
	private static final String INVALID_TERM = "1000011622,Holmes,Katherine,Marilyn,HOLMESK4@UNLV.NEVADA.EDU,HOSBSHA, , , , , ,2048, ";
	private static final String INVALID_SEMESTER = "1000011622,Holmes,Katherine,Marilyn,HOLMESK4@UNLV.NEVADA.EDU,HOSBSHA,2049, , , , ,2048, ";
	private static final String INVALID_CENTURY = "1000011622,Holmes,Katherine,Marilyn,HOLMESK4@UNLV.NEVADA.EDU,HOSBSHA,3048, , , , ,2048, ";
	private static final Logger LOG = Logger.getLogger(LineTest.class);
	
	@Test(expected=InvalidLineException.class)
	public void shouldFailIfGivenIncorrectSize() {
		int lineNumber = 1;
		List<String> tokens = new ArrayList<String>();
		Line instance = new Line(tokens,lineNumber);
	}
	
	@Test
	public void shouldPassIfGivenCorrectSize() {
		Line instance = createLine(VALID_DATA);
		LOG.error("WOO HOO TEST PASSED FOR LINE: " + VALID_DATA);
	}
	
	@Test(expected=InvalidTokenException.class)
	public void shouldFailIfStudentHasNoMajors() {
		Line instance = createLine(NO_MAJORS);
	}
	
	@Test(expected=InvalidTokenException.class)
	public void shouldFailIfInvalidTerm() {
		Line instance = createLine(INVALID_TERM);
	}
	
	@Test(expected=InvalidTokenException.class)
	public void shouldFailIfInvalidSemester() {
		Line instance = createLine(INVALID_SEMESTER);
	}
	
	@Test(expected=InvalidTokenException.class)
	public void shouldFailIfInvalidCentury() {
		Line instance = createLine(INVALID_CENTURY);
	}
	
	private Line createLine(String line) {
		int lineNumber = 1;
		List<String> tokens = Arrays.asList(line.split(","));
		return new Line(tokens,lineNumber);
	}
}
