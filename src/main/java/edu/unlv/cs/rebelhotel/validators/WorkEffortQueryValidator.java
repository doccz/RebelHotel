package edu.unlv.cs.rebelhotel.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import edu.unlv.cs.rebelhotel.form.FormWorkEffortQuery;

@Component
public class WorkEffortQueryValidator {
	public boolean supports(Class clazz) {
		return FormWorkEffortQuery.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {

		FormWorkEffortQuery fweq = (FormWorkEffortQuery) target;

		if (fweq.getStartDate() != null && fweq.getEndDate() != null) {
			if (fweq.getEndDate().before(fweq.getStartDate())) {

				errors.rejectValue("startDate", "date.invalid_combination",
						"The first date must not be greater than the second date");

			}
		}

		if (fweq.getEmployerLocation().isEmpty() && fweq.getEmployerName().isEmpty() && fweq.getStudentFirstName().isEmpty() && fweq.getStudentMiddleName().isEmpty() && fweq.getStudentLastName().isEmpty()
				&& fweq.getUserId().isEmpty() && (!fweq.isValidationSelected()) && (!fweq.isVerificationSelected()) && (!fweq.isVerificationTypeSelected()) && fweq.getStartDate()==null && fweq.getEndDate()==null) {
				errors.reject("error.empty.fields","Must Have Non-Empty Field");
		}

	}

}
