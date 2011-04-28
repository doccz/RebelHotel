package edu.unlv.cs.rebelhotel.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.email.UserEmailService;


@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {
	
	@Autowired
	UserEmailService userEmailService;
	
	@RequestMapping( "/create")
	public String createforgotPasswordForm(Model model){
		
		return "forgotpassword/create";
	}
	
	
	@RequestMapping( value="/createNewPassword", method = RequestMethod.GET )
	public String createNewPassword(@RequestParam("userId") String userId, Model model){
		
		try{
		UserAccount userAccount = UserAccount.findUserAccountsByUserId(userId).getSingleResult();
		String password = userAccount.generateRandomPassword();
		userAccount.setPassword(password);
		userAccount.merge();

	    userEmailService.sendNewPassword(userAccount, password);
	    Student student = Student.findStudentsByUserAccount(userAccount).getSingleResult();
		model.addAttribute("student",student);
		}
		catch(org.springframework.dao.EmptyResultDataAccessException exception){
			model.addAttribute("userId",userId);
			return "forgotPassword/create";
		}
		
		return "forgotpassword/confirmation";
	}
}
