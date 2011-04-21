package edu.unlv.cs.rebelhotel.domain;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@RooJavaBean
@RooToString
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "semester",
		"termYear" }) })
@RooEntity(finders = { "findTermsBySemester",
		"findTermsBySemesterAndTermYearEquals" })
public class Term {

	@NotNull
	private Integer termYear;

	@Enumerated
	private Semester semester;

	public Term(Integer termYear, Semester semester) {
		this.termYear = termYear;
		this.semester = semester;
	}

	public Term() {
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getSemester() + " " + getTermYear());
		return sb.toString();
	}

	public boolean isBetween(final Term start, final Term end) {
		boolean between = start.termYear <= termYear;
		between &= end.termYear >= termYear;
		if(between){
			between = semester.compareTo(start.semester)>=0;
			between &= semester.compareTo(end.semester)<=0;
		}
		return between;
	}
}
