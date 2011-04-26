package edu.unlv.cs.rebelhotel.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.form.FormChangePassword;
import edu.unlv.cs.rebelhotel.form.FormStudentQuery;
import edu.unlv.cs.rebelhotel.service.UserInformation;
import edu.unlv.cs.rebelhotel.web.ChangePasswordController;

@Component
public class ChangePasswordValidator implements Validator {

	static final String MESSAGE_NEW_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD = "New password must be entered when changing password";
	static final String FIELD_NEW_PASSWORD = "newpassword";

	static final String MESSAGE_YOU_MUST_CONFIRM_YOUR_PASSWORD_WHEN_CHANGING_PASSWORD = "You must confirm your password when changing password";
	static final String FIELD_CONFIRM_PASSWORD = "confirmpassword";

	static final String MESSAGE_CURRENT_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD = "Current password must be entered when changing password";
	static final String FIELD_CURRENT_PASSWORD = "currentpassword";
	
	static final String ERROR_CHANGE_PASSWORD_WRONG_CURRENT = "change_password_wrong_current";
	static final String MESSAGE_CURRENT_PASSWORD_IS_INCORRECT = "Current password entered is incorrect";
	
	static final String ERROR_CHANGE_PASSWORD_WRONG_CONFIRM = "change_password_wrong_confirm";
	static final String MESSAGE_YOUR_NEW_PASSWORD_AND_CONFIRMED_PASSWORD_DO_NOT_MATCH = "Your new password and confirmed password do not match";
	
	static final String ERROR_CHANGE_PASSWORD_BLANK_CURRENT = "change_password_blank_current";
	static final String ERROR_CHANGE_PASSWORD_BLANK_NEW = "change_password_blank_new";
	static final String ERROR_CHANGE_PASSWORD_BLANK_CONFIRM = "change_password_blank_confirm";
	
	private final UserInformation userInformation;
	
	@Autowired
	public ChangePasswordValidator(UserInformation userInformation){
		this.userInformation = userInformation;
	}

	public boolean supports(Class clazz) {
		return FormChangePassword.class.isAssignableFrom(clazz);
	}


	public void validate(Object target, Errors errors) {

		FormChangePassword form = (FormChangePassword) target;
		UserAccount userAccount = userInformation.getUserAccount();
		
		
		if (!StringUtils.hasText(form.getCurrentPassword())) {
			errors.rejectValue(FIELD_CURRENT_PASSWORD,
					ERROR_CHANGE_PASSWORD_BLANK_CURRENT,
					MESSAGE_CURRENT_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD);
			return;
		}
		
		if (!StringUtils.hasText(form.getNewPassword())) {
			errors.rejectValue(FIELD_NEW_PASSWORD,
					ERROR_CHANGE_PASSWORD_BLANK_NEW,
					MESSAGE_NEW_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD);
			return;
		}

		if (!StringUtils.hasText(form.getConfirmPassword())) {
			errors.rejectValue(FIELD_CONFIRM_PASSWORD,
					ERROR_CHANGE_PASSWORD_BLANK_CONFIRM,
					MESSAGE_YOU_MUST_CONFIRM_YOUR_PASSWORD_WHEN_CHANGING_PASSWORD);
			return;
		}
		
		if (!form.getNewPassword().equals(form.getConfirmPassword())) {
			errors.rejectValue(FIELD_CONFIRM_PASSWORD,
					ERROR_CHANGE_PASSWORD_WRONG_CONFIRM,
					MESSAGE_YOUR_NEW_PASSWORD_AND_CONFIRMED_PASSWORD_DO_NOT_MATCH);
			return;
		}
		
		if (!userAccount.matchesCurrentPassword(form.getCurrentPassword())) {
			errors.rejectValue(FIELD_CURRENT_PASSWORD,
					ERROR_CHANGE_PASSWORD_WRONG_CURRENT,
					MESSAGE_CURRENT_PASSWORD_IS_INCORRECT);
			return;
		}
		
	}
}