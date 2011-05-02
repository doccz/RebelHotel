package edu.unlv.cs.rebelhotel.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import edu.unlv.cs.rebelhotel.form.FormStudentQuickFind;

@Component
public class StudentQuickFindValidator {

	public boolean supports(Class clazz) {
		return FormStudentQuickFind.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {

		FormStudentQuickFind formStudentQuickFind = (FormStudentQuickFind) target;

		if (formStudentQuickFind.getUserId().isEmpty()
				&& formStudentQuickFind.getLastName().isEmpty()
				&& formStudentQuickFind.getFirstName().isEmpty()) {

			errors.reject("error.empty.fields","Must Have Non-Empty Field");

		}
	}

}
