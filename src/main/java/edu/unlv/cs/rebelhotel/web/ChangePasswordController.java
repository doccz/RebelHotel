package edu.unlv.cs.rebelhotel.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.form.FormChangePassword;
import edu.unlv.cs.rebelhotel.service.UserInformation;


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
		if (result.hasErrors()){
			model.addAttribute("formChangePassword", change);
			return "change/password";
		}
		
		UserAccount userAccount = userInformation.getUserAccount();
		userAccount.setPassword(change.getNewPassword());
		userAccount.merge();
    	return "change/confirmation";
		
	}

	@Autowired
	public void setUserInformation(UserInformation userInformation) {
		this.userInformation = userInformation;
	}
	
	
}