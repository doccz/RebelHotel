package edu.unlv.cs.rebelhotel.web;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.domain.enums.UserGroup;
import edu.unlv.cs.rebelhotel.email.UserEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;


@RequestMapping("/useraccounts")
@Controller
public class UserAccountController {

	@Autowired
	UserEmailService userEmailService;
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid UserAccount userAccount, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            return "useraccounts/create";
        }
        
        // ADD THE CALL TO MAILER
        if(userAccount.getUserGroup().equals(UserGroup.ROLE_STUDENT) )
        {
 
        	String password = userAccount.generateRandomPassword();
        	userAccount.setPassword(password);
        	userAccount.persist();
				userEmailService.sendStudentConfirmation(userAccount, password);

        }
       
        else
        {
        	String password = userAccount.generateRandomPassword();
        	userAccount.setPassword(password);
        	userAccount.persist();
			userEmailService.sendAdminComfirmation(userAccount, password);
        }
         
        return "redirect:/useraccounts/" + encodeUrlPathSegment(userAccount.getId().toString(), request);
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("userAccount", new UserAccount());
        return "useraccounts/create";
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid UserAccount userAccount, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("userAccount", userAccount);
            return "useraccounts/update";
        }
        userAccount.merge();
        return "redirect:/useraccounts/" + encodeUrlPathSegment(userAccount.getId().toString(), request);
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("userAccount", UserAccount.findUserAccount(id));
        return "useraccounts/update";
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        UserAccount.findUserAccount(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/useraccounts?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("useraccount", UserAccount.findUserAccount(id));
        model.addAttribute("itemId", id);
        return "useraccounts/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("useraccounts", UserAccount.findUserAccountEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) UserAccount.countUserAccounts() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("useraccounts", UserAccount.findAllUserAccounts());
        }
        return "useraccounts/list";
    }

	@RequestMapping(params = { "find=ByUserId", "form" }, method = RequestMethod.GET)
    public String findUserAccountsByUserIdForm(Model model) {
        return "useraccounts/findUserAccountsByUserId";
    }

	@RequestMapping(params = "find=ByUserId", method = RequestMethod.GET)
    public String findUserAccountsByUserId(@RequestParam("userId") String userId, Model model) {
        model.addAttribute("useraccounts", UserAccount.findUserAccountsByUserId(userId).getResultList());
        return "useraccounts/list";
    }

	@RequestMapping(params = { "find=ByUserIdEquals", "form" }, method = RequestMethod.GET)
    public String findUserAccountsByUserIdEqualsForm(Model model) {
        return "useraccounts/findUserAccountsByUserIdEquals";
    }

	@RequestMapping(params = "find=ByUserIdEquals", method = RequestMethod.GET)
    public String findUserAccountsByUserIdEquals(@RequestParam("userId") String userId, Model model) {
        model.addAttribute("useraccounts", UserAccount.findUserAccountsByUserIdEquals(userId).getResultList());
        return "useraccounts/list";
    }

	@ModelAttribute("usergroups")
    public Collection<UserGroup> populateUserGroups() {
        return Arrays.asList(UserGroup.class.getEnumConstants());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest request) {
        String enc = request.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
