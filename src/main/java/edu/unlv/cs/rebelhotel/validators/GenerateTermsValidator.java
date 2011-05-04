package edu.unlv.cs.rebelhotel.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.unlv.cs.rebelhotel.form.FormGenerateTerms;

@Component
public class GenerateTermsValidator implements Validator {
	public boolean supports (Class clazz) {
		return FormGenerateTerms.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		FormGenerateTerms term = (FormGenerateTerms) target;
		if (term.getYearLow() == null) {
			errors.rejectValue("yearLow", "formgenerateterms.null_year", "Year is required");
		}
		else if (term.getYearLow() < 0) {
			errors.rejectValue("yearLow", "formgenerateterms.negative_year", "Cannot have a negative year");
		}
		if (term.getYearHigh() == null) {
			errors.rejectValue("yearHigh", "formgenerateterms.null_year", "Year is required");
		}
		else if (term.getYearHigh() < 0) {
			errors.rejectValue("yearHigh", "formgenerateterms.negative_year", "Cannot have a negative year");
		}
	}
}