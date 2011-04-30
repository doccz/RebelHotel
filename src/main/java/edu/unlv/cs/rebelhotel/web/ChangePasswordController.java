package edu.unlv.cs.rebelhotel.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.form.FormChangePassword;
import edu.unlv.cs.rebelhotel.service.UserInformation;
import edu.unlv.cs.rebelhotel.validators.ChangePasswordValidator;


@Controller
@RequestMapping("/change")
public class ChangePasswordController {

	private UserInformation userInformation;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showPage(Model model){
		FormChangePassword change = new FormChangePassword();
		model.addAttribute("formChangePassword",change);
		return "change/password";
	}

	@RequestMapping(method = RequestMethod.PUT)
	public String changePassword(@Valid FormChangePassword change, BindingResult result, Model model, HttpServletRequest request){
		// not sure if concurrency matters here; creates a new validator with the user information parameter for each request just in case
		new ChangePasswordValidator(userInformation).validate(change, result);
		if (result.hasErrors()){
			model.addAttribute("formChangePassword", change);
			return "change/password";
		}
		
		// avoid attempting to merge the account stored in the session scoped object
		UserAccount userAccount = UserAccount.findUserAccount(userInformation.getUserAccount().getId());
		userAccount.setPassword(change.getNewPassword());
		userAccount.merge();
		
		// update the session scoped copy with the new password (updating the above one does not automatically do this)
		userInformation.getUserAccount().setPassword(change.getNewPassword());
    	return "change/confirmation";
		
	}

	@Autowired
	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}
	
	
}
