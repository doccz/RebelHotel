package edu.unlv.cs.rebelhotel.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.form.FormChangePassword;
import edu.unlv.cs.rebelhotel.service.UserInformation;


@Controller
@RequestMapping("/change")
public class ChangePasswordController {
	
	private final UserInformation userInformation;
	
	@Autowired
	public ChangePasswordController(UserInformation userInformation) {
		this.userInformation = userInformation;
	}
	
	@RequestMapping(params = "password", method = RequestMethod.GET)
	public String showPage(Model model){
		FormChangePassword change = new FormChangePassword();
		model.addAttribute("change",change);
		return "change/password";
	}

	@RequestMapping(params = "password", method = RequestMethod.POST)
	public String changePassword(@Valid FormChangePassword change, BindingResult result, Model model){
		if (result.hasErrors()){
			model.addAttribute("change", change);
			return "change/password";
		}
		
		userInformation.getUserAccount().setPassword(change.getNewPassword());
		
		
		return "";
	}

}