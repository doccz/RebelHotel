package edu.unlv.cs.rebelhotel.validators;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.form.FormChangePassword;
import edu.unlv.cs.rebelhotel.service.UserInformation;
import static org.easymock.EasyMock.*;

/*A TOTAL OF 9 TEST
 3: CONFIRMED IS NULL
 4: iNCORRECT PASSWORD
 5: NEWPASSWORD DOES NOT MATCH CONFIRMED
 6: CURRENT IS BLANK
 7: CONFIRMED IS BLANK
 8: NEWPASSWORD IS BLANK
 9: VALID PROVIDED*/

public class ChangePasswordValidatorTest {

	private ChangePasswordValidator instance;
	private Errors errors;
	private UserInformation userInformation;

	@Before
	public void setUp() {
		userInformation = new UserInformation();
		instance = new ChangePasswordValidator(userInformation);
		errors = createStrictMock(Errors.class);
	}

	// Test if current password is null
	@Test
	public void currentPasswordNullCausesError() {
		FormChangePassword target = new FormChangePassword();
		target.setCurrentPassword(null);

		errors.rejectValue(
				ChangePasswordValidator.FIELD_CURRENT_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_BLANK_CURRENT,
				ChangePasswordValidator.MESSAGE_CURRENT_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if new password is null
	@Test
	public void newPasswordNullCausesError() {
		FormChangePassword target = new FormChangePassword();
		target.setCurrentPassword("test");
		target.setNewPassword(null);

		errors.rejectValue(
				ChangePasswordValidator.FIELD_NEW_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_BLANK_NEW,
				ChangePasswordValidator.MESSAGE_NEW_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if confirm password is null
	@Test
	public void confirmPasswordNullCausesError() {
		FormChangePassword target = new FormChangePassword();
		target.setCurrentPassword("test");
		target.setNewPassword("hello");
		target.setConfirmPassword(null);

		errors.rejectValue(
				ChangePasswordValidator.FIELD_CONFIRM_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_BLANK_CONFIRM,
				ChangePasswordValidator.MESSAGE_YOU_MUST_CONFIRM_YOUR_PASSWORD_WHEN_CHANGING_PASSWORD);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if current password is blank
	@Test
	public void currentPasswordBlankCausesError() {
		FormChangePassword target = new FormChangePassword();
		UserAccount ua = createMock(UserAccount.class);
		userInformation.setUserAccount(ua);
		userInformation.getUserAccount().setPassword("test");
		target.setCurrentPassword("");
		target.setNewPassword("hello");
		target.setConfirmPassword("hello");

		errors.rejectValue(
				ChangePasswordValidator.FIELD_CURRENT_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_BLANK_CURRENT,
				ChangePasswordValidator.MESSAGE_CURRENT_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if new password is blank
	@Test
	public void newPasswordBlankCausesError() {
		FormChangePassword target = new FormChangePassword();
		UserAccount ua = createMock(UserAccount.class);
		userInformation.setUserAccount(ua);
		userInformation.getUserAccount().setPassword("test");
		target.setCurrentPassword("test");
		target.setNewPassword("");
		target.setConfirmPassword("hello");

		errors.rejectValue(
				ChangePasswordValidator.FIELD_NEW_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_BLANK_NEW,
				ChangePasswordValidator.MESSAGE_NEW_PASSWORD_MUST_BE_ENTERED_WHEN_CHANGING_PASSWORD);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if confirm password is blank
	@Test
	public void confirmPasswordBlankCausesError() {
		FormChangePassword target = new FormChangePassword();
		UserAccount ua = createMock(UserAccount.class);
		userInformation.setUserAccount(ua);
		userInformation.getUserAccount().setPassword("test");
		target.setCurrentPassword("test");
		target.setNewPassword("hello");
		target.setConfirmPassword("");

		errors.rejectValue(
				ChangePasswordValidator.FIELD_CONFIRM_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_BLANK_CONFIRM,
				ChangePasswordValidator.MESSAGE_YOU_MUST_CONFIRM_YOUR_PASSWORD_WHEN_CHANGING_PASSWORD);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if confirmed password matches new password
	@Test
	public void confirmPasswordWrongCausesError() {
		FormChangePassword target = new FormChangePassword();
		UserAccount ua = createMock(UserAccount.class);
		userInformation.setUserAccount(ua);
		userInformation.getUserAccount().setPassword("test");
		target.setCurrentPassword("test");
		target.setNewPassword("hello");
		target.setConfirmPassword("nhello");

		errors.rejectValue(
				ChangePasswordValidator.FIELD_CONFIRM_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_WRONG_CONFIRM,
				ChangePasswordValidator.MESSAGE_YOUR_NEW_PASSWORD_AND_CONFIRMED_PASSWORD_DO_NOT_MATCH);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if current password is correct
	@Test
	public void currentPasswordWrongCausesError() {
		FormChangePassword target = new FormChangePassword();
		UserAccount ua = createMock(UserAccount.class);
		userInformation.setUserAccount(ua);
		userInformation.getUserAccount().setPassword("test");
		target.setCurrentPassword("ntest");
		target.setNewPassword("hello");
		target.setConfirmPassword("hello");

		errors.rejectValue(ChangePasswordValidator.FIELD_CURRENT_PASSWORD,
				ChangePasswordValidator.ERROR_CHANGE_PASSWORD_WRONG_CURRENT,
				ChangePasswordValidator.MESSAGE_CURRENT_PASSWORD_IS_INCORRECT);
		expectLastCall();

		replay(errors);

		instance.validate(target, errors);

	}

	// Test if valid entry
	@Test
	public void validEntryNoError() {
		String currentPassword = "test";
		FormChangePassword target = new FormChangePassword();
		UserAccount ua = createMock(UserAccount.class);
		userInformation.setUserAccount(ua);
		target.setCurrentPassword(currentPassword);
		target.setNewPassword("hello");
		target.setConfirmPassword("hello");
		
		expect(ua.matchesCurrentPassword(currentPassword)).andReturn(true);
		
		replay(ua);
		replay(errors);
		
		instance.validate(target, errors);

	}
}
