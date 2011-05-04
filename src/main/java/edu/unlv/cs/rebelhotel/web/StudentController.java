package edu.unlv.cs.rebelhotel.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.joda.time.format.DateTimeFormat;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;

import edu.unlv.cs.rebelhotel.domain.enums.Semester;
import edu.unlv.cs.rebelhotel.domain.enums.UserGroup;
import edu.unlv.cs.rebelhotel.domain.enums.Validation;
import edu.unlv.cs.rebelhotel.domain.enums.Verification;
import edu.unlv.cs.rebelhotel.email.UserEmailService;
import edu.unlv.cs.rebelhotel.form.FormStudent;
import edu.unlv.cs.rebelhotel.form.FormStudentMajor;
import edu.unlv.cs.rebelhotel.form.FormStudentQuery;
import edu.unlv.cs.rebelhotel.form.FormStudentQuickFind;
import edu.unlv.cs.rebelhotel.service.StudentQueryService;
import edu.unlv.cs.rebelhotel.service.StudentQuickFindService;
import edu.unlv.cs.rebelhotel.service.UserInformation;
import edu.unlv.cs.rebelhotel.service.WorkEffortQueryService;
import edu.unlv.cs.rebelhotel.validators.StudentQueryValidator;
import edu.unlv.cs.rebelhotel.validators.StudentQuickFindValidator;
import edu.unlv.cs.rebelhotel.validators.WorkEffortQueryValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@SessionAttributes("studentList")
@RequestMapping("/students")
@Controller
public class StudentController {
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UserInformation userInformation;
	
	@Autowired
	StudentQueryValidator studentQueryValidator;
	
	@Autowired
	StudentQueryService studentQueryService;
	
	@Autowired
	UserEmailService userEmailService;
	

	@Autowired
    StudentQuickFindService studentQuickFindService;
	

	@Autowired
	StudentQuickFindValidator studentQuickFindValidator;

	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	void setStudentQueryValidator(StudentQueryValidator studentQueryValidator) {
		this.studentQueryValidator = studentQueryValidator;
	}
	
	void setStudentQueryService(StudentQueryService studentQueryService) {
		this.studentQueryService = studentQueryService;
	}
	
	void setStudentQuickFindService(StudentQuickFindService studentQuickFindService) {
		this.studentQuickFindService = studentQuickFindService;
	}
	void setStudentQuickFindValidator(StudentQuickFindValidator studentQuickFindValidator) {
		this.studentQuickFindValidator = studentQuickFindValidator;
	}
	
	void addDateTimeFormatPatterns(Model model) {
        model.addAttribute("student_lastmodified_date_format", DateTimeFormat.patternForStyle("SF", LocaleContextHolder.getLocale()));
    }
	
	void addQueryDateTimeFormatPatterns(Model model) {
        model.addAttribute("student_query_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
        model.addAttribute("workEffortDuration_startdate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
        model.addAttribute("workEffortDuration_enddate_date_format", DateTimeFormat.patternForStyle("S-", LocaleContextHolder.getLocale()));
    }
	
	@ModelAttribute("query_semesters")
    public Collection<Semester> populateQuerySemesters() {
        return Arrays.asList(Semester.class.getEnumConstants());
    }
	
	@ModelAttribute("validations")
    public Collection<Validation> populateValidations() {
        return Arrays.asList(Validation.class.getEnumConstants());
    }
	
	@ModelAttribute("verifications")
    public Collection<Verification> populateVerifications() {
        return Arrays.asList(Verification.class.getEnumConstants());
    }
	
	public int getNumProperties(FormStudentQuery formStudentQuery) {
		int num = 1;
		if (formStudentQuery.getShowUserId()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowEmail()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowFirstName()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowMiddleName()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowLastName()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowAdmitTerm()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowGradTerm()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowCodeOfConductSigned()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowLastModified()) {
			num = num + 1;
		}
		if (formStudentQuery.getShowUserAccount()) {
			num = num + 1;
		}
		return num;
	}
	
	public String buildPropertiesString(FormStudentQuery formStudentQuery) {
		String properties = "id";
		if (formStudentQuery.getShowUserId()) {
			properties += ",userId";
		}
		if (formStudentQuery.getShowEmail()) {
			properties += ",email";
		}
		if (formStudentQuery.getShowFirstName()) {
			properties += ",firstName";
		}
		if (formStudentQuery.getShowMiddleName()) {
			properties += ",middleName";
		}
		if (formStudentQuery.getShowLastName()) {
			properties += ",lastName";
		}
		if (formStudentQuery.getShowAdmitTerm()) {
			properties += ",admitTerm";
		}
		if (formStudentQuery.getShowGradTerm()) {
			properties += ",gradTerm";
		}
		if (formStudentQuery.getShowCodeOfConductSigned()) {
			properties += ",codeOfConductSigned";
		}
		if (formStudentQuery.getShowLastModified()) {
			properties += ",lastModified";
		}
		if (formStudentQuery.getShowUserAccount()) {
			properties += ",userAccount";
		}
		return properties;
	}
	
	public String buildLabelsString(FormStudentQuery formStudentQuery) {
		String properties = messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_id", null, LocaleContextHolder.getLocale());
		if (formStudentQuery.getShowUserId()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_userid", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowEmail()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_email", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowFirstName()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_firstname", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowMiddleName()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_middlename", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowLastName()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_lastname", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowAdmitTerm()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_admitterm", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowGradTerm()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_gradterm", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowCodeOfConductSigned()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_codeofconductsigned", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowLastModified()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_lastmodified", null, LocaleContextHolder.getLocale());
		}
		if (formStudentQuery.getShowUserAccount()) {
			properties += "," + messageSource.getMessage("label_edu_unlv_cs_rebelhotel_domain_student_useraccount", null, LocaleContextHolder.getLocale());
		}
		return properties;
	}
	
	public String buildMaxLengthsString(FormStudentQuery formStudentQuery) {
		// these will determine how many characters the table.jspx will display per data column; table.jspx defaults to 10, so this does too
		String properties = "10";
		if (formStudentQuery.getShowUserId()) {
			properties += ",10";
		}
		if (formStudentQuery.getShowEmail()) {
			properties += ",32";
		}
		if (formStudentQuery.getShowFirstName()) {
			properties += ",64";
		}
		if (formStudentQuery.getShowMiddleName()) {
			properties += ",64";
		}
		if (formStudentQuery.getShowLastName()) {
			properties += ",64";
		}
		if (formStudentQuery.getShowAdmitTerm()) {
			properties += ",11";
		}
		if (formStudentQuery.getShowGradTerm()) {
			properties += ",11";
		}
		if (formStudentQuery.getShowCodeOfConductSigned()) {
			properties += ",10";
		}
		if (formStudentQuery.getShowLastModified()) {
			properties += ",32";
		}
		if (formStudentQuery.getShowUserAccount()) {
			properties += ",16";
		}
		return properties;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(value="/quickFind", method = RequestMethod.POST)
    public String quickFind(FormStudentQuickFind form,BindingResult result, Model model, HttpServletRequest request) throws Exception {
		
		studentQuickFindValidator.validate(form, result);
		
		if(result.hasErrors()){
			FormStudentQuickFind formStudentQuickFind = new FormStudentQuickFind();
			model.addAttribute("formStudentQuickFind",formStudentQuickFind);
			return "students/quickFindForm";
		}
		
		// Perform the query for jobs
		List<Student> studentList = studentQuickFindService.findStudents(form);
		//the list of jobs will be added as a session attribute 
		model.addAttribute("studentList", studentList);
		return "redirect:/students/quickfind?page=1&size=10";
	}
	

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(value = "/quickfind", method = RequestMethod.GET)
	public String quickFindList(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			@ModelAttribute("studentList") List<WorkEffort> studentList,
			BindingResult result, Model model, HttpServletRequest request) {

		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			int pageNo = page == null ? 1 : page.intValue();
			int from = sizeNo * pageNo < studentList.size() ? sizeNo
					* pageNo : studentList.size();
			int to = (pageNo - 1) * sizeNo;
			model.addAttribute("students", studentList.subList(to, from));
			float nrOfPages = (float) studentList.size() / sizeNo;
			model.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			model.addAttribute("students",studentList);
		}

		return "students/quickFindList";
	}

	
	
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        addDateTimeFormatPatterns(model);
        model.addAttribute("student", Student.findStudent(id));
        model.addAttribute("itemId", id);
        model.addAttribute("progressList", Student.findStudent(id).calculateProgress());
        return "students/show";
    }
	
	@RequestMapping(value = "/listquery", method = RequestMethod.GET)
	public String queryList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @Valid FormStudentQuery form, BindingResult result, Model model) throws IOException, Exception {
		studentQueryValidator.validate(form, result); // rather than assigning the validator to the student controller (like with the work effort controller), it should only apply to this method
		
		if (result.hasErrors()) {
			model.addAttribute("formStudentQuery", form);
			addQueryDateTimeFormatPatterns(model);
			return "students/query";
		}
		
		String sorting = null;
		for (int i = 0; i < getNumProperties(form)*2; i++) {
			if (WebUtils.hasSubmitParameter(request, "sorting" + i + ".x")) {
				sorting = "" + i;
				model.addAttribute("sorting", sorting);
			}
		}
		
		List<Object> queryResult;
		int sizeNo = size == null ? 10 : size.intValue();
		if ((page != null || size != null) && !form.getOutputCsv()) {
            queryResult = studentQueryService.queryStudents(form, sorting, (page.intValue() - 1) * sizeNo, sizeNo);
		}
		else {
			queryResult = studentQueryService.queryStudents(form, sorting);
		}
		Long resultCount = (Long) queryResult.get(0);
		List<Student> students = (List<Student>) queryResult.get(1);
		if ((page != null || size != null) && !form.getOutputCsv()) {
            float nrOfPages = (float) resultCount / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
		}
		
		if (!form.getOutputCsv() && !WebUtils.hasSubmitParameter(request, "downloadExcel")) {
			String properties = buildPropertiesString(form);
			String labels = buildLabelsString(form);
			String maxLengths = buildMaxLengthsString(form);
			
			/* Code to remove "page" and "size" parameters from the query string
			since otherwise the pagination that uses queryString would add those two parameters to the URL
			each time a different page size is selected */
			String queryString = request.getQueryString();
			String queryStringArray[] = queryString.split("&");
			for (int i = 0; i < queryStringArray.length; i++) {
				String check;
				if ((check = queryStringArray[i].split("=")[0]).equals("size") || check.equals("page")) {
					queryStringArray[i] = "";
				}
			}
			queryString = "";
			for (int i = 0; i < queryStringArray.length; i++) {
				if (queryStringArray[i] != "") {
					if (!queryString.equals("")) {
						queryString += "&amp;";
					}
					queryString += queryStringArray[i];
				}
			}
			
			model.addAttribute("sortId", sorting);
			model.addAttribute("queryString", queryString);
			model.addAttribute("counted", resultCount);
			model.addAttribute("formStudentQuery", form);
			model.addAttribute("students", students);
			model.addAttribute("tempColumnProperties", properties);
			model.addAttribute("tempColumnLabels", labels);
			model.addAttribute("tempColumnMaxLengths", maxLengths);
			return "students/queryList";
		}
		else {
			// code to generate CSV is called from here;
			// then the contents are placed into the temp file below before sent to the user as a download
			ServletContext servletContext = request.getSession().getServletContext();
			String filename = "student query.csv"; // users will by default receive this file as the name to save as
			String contents = studentQueryService.generateCsv(form, students, messageSource); // the contents of the file in a string; alternatively use a byte array
			
			int filesize = (int) contents.length();
			
			if (filesize > 0 ) {
				response.setBufferSize(filesize);
				response.setContentType("text/cvs");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("pragma", "no-cache");
				response.setDateHeader("Expires", 0);
				response.setContentLength(filesize);
				
				FileCopyUtils.copy(contents.getBytes(), response.getOutputStream());
				
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}
			else {
				// TODO make this more robust
				response.setContentType("text/html");
				PrintWriter writer = response.getWriter();
				writer.println("<html>");
				writer.println("FAIL");
				writer.println("</html>");
			}
			
			return null;
		}
	}
	
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String query(Model model) {
		FormStudentQuery form = new FormStudentQuery();
		form.setLastModifiedStart(new Date());
		form.setLastModifiedEnd(new Date());
		model.addAttribute("formStudentQuery", form);
		addQueryDateTimeFormatPatterns(model);
		return "students/query";
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid FormStudent formStudent, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("formStudent", formStudent);
            addDateTimeFormatPatterns(model);
            return "students/create";
        }
        
        // the order of this is pretty strict
        Student student = new Student();
        student.setUserId(formStudent.getUserId());
        
        // attempts to find a user account with matching user id first; this may not be desirable
        // likely will want an error message if the found user account is assigned to a different user for some reason
        UserAccount userAccount;
        String password;
    	
        try {
        	userAccount = UserAccount.findUserAccountsByUserId(student.getUserId()).getSingleResult();
        	userAccount.setEmail(formStudent.getEmail()); // updates email
        	password = userAccount.generateRandomPassword();
        	userAccount.setPassword(password);
        	userAccount.merge();
        }
        catch (org.springframework.dao.EmptyResultDataAccessException exception) {
        	userAccount = UserAccount.fromStudent(student, formStudent.getEmail());
            userAccount.setUserGroup(UserGroup.ROLE_STUDENT);
            userAccount.setEnabled(true);
            password = userAccount.generateRandomPassword();
        	userAccount.setPassword(password);
            userAccount.persist();
        }
        
        student.setUserAccount(userAccount);
        student.copyFromFormStudent(formStudent);

        
        //Send Email to student's account with generated password
    	
    	student.persist();
		userEmailService.sendStudentConfirmation(userAccount, password);

        return "redirect:/students/" + encodeUrlPathSegment(student.getId().toString(), request);
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("formStudent", new FormStudent());
        addDateTimeFormatPatterns(model);
        return "students/create";
    }
    
    /**********************************************
     * Remove later; added as a temporary means of adding majors to students
     **********************************************/
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", params = "major", method = RequestMethod.GET)
    public String createMajor(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("formStudentMajor", FormStudentMajor.createFromStudent(Student.findStudent(id)));
    	return "students/updateMajor";
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(params = "major", method = RequestMethod.PUT)
    public String updateMajor(@Valid FormStudentMajor fsm, BindingResult result, Model model, HttpServletRequest request) {
    	if (result.hasErrors()) {
    		model.addAttribute("formStudentMajor", fsm);
    		return "students/updateMajor";
    	}
    	Student student = Student.findStudent(fsm.getId());
    	student.setMajors(fsm.getMajors());
    	student.merge();
    	return "redirect:/students/" + encodeUrlPathSegment(student.getId().toString(), request);
    }
    /************************************************
     ************************************************/
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid FormStudent formStudent, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("formStudent", formStudent);
            addDateTimeFormatPatterns(model);
            return "students/update";
        }
        Student student = Student.findStudent(formStudent.getId());
        student.copyFromFormStudent(formStudent);
        student.merge();
        return "redirect:/students/" + encodeUrlPathSegment(student.getId().toString(), request);
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Student.findStudent(id).remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/students?page=" + ((page == null) ? "1" : page.toString()) + "&size=" + ((size == null) ? "10" : size.toString());
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("formStudent", FormStudent.createFromStudent(Student.findStudent(id)));
        addDateTimeFormatPatterns(model);
        return "students/update";
    }

	@RequestMapping(params = "myprogress", method = RequestMethod.GET)
	public String myProgress(Model model) {	
	
		Student student = Student.findStudent(userInformation.getStudent().getId());
		
		model.addAttribute("student", student);
    	model.addAttribute("majorsList", student.getMajors());
    	model.addAttribute("progressList", student.calculateProgress());
		
		return "students/myprogress";
	}
    
    @RequestMapping(params = { "find=ByUserIdEquals", "form" }, method = RequestMethod.GET)
    public String findStudentsByUserIdEqualsForm(Model model) {
        return "students/findStudentsByUserIdEquals";
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

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("students", Student.findStudentEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Student.countStudents() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("students", Student.findAllStudents());
        }
        addDateTimeFormatPatterns(model);
        return "students/list";
    }
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(value = "quickFindByUserId", method = RequestMethod.GET)
    public String DefaultFindStudentByUserId(@RequestParam(value= "userId", required= false) String userId, Model model, HttpServletRequest request) {
    	Student student;
    
    	if( userId.isEmpty())
    		return "index";
    	
    	try{
        student =  Student.findStudentsByUserIdEquals(userId).getSingleResult();
		model.addAttribute("student", student);
        model.addAttribute("itemId", student.getId());
    	}
 
    	catch(org.springframework.dao.EmptyResultDataAccessException exception ){
    		return "students/show";
    	}
    	catch(NoResultException e){
    		return "students/show";
    	}
    	
        return "students/show";
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping("quickFindCreate")
    public String quickFindCreateForm(Model model, HttpServletRequest request) {
		
		FormStudentQuickFind formStudentQuickFind = new FormStudentQuickFind();
		model.addAttribute("formStudentQuickFind",formStudentQuickFind);
		return "students/quickFindForm";
	}
	
	
	/*@ModelAttribute("majors")
    public Collection<Major> populateMajors() {
        return Major.findAllMajors();
    }*/

	@ModelAttribute("terms")
    public Collection<Term> populateTerms() {
        return Term.findAllTerms();
    }

	/*@ModelAttribute("useraccounts")
    public Collection<UserAccount> populateUserAccounts() {
        return UserAccount.findAllUserAccounts();
    }*/

	@ModelAttribute("workefforts")
    public Collection<WorkEffort> populateWorkEfforts() {
        return WorkEffort.findAllWorkEfforts();
    }
}
