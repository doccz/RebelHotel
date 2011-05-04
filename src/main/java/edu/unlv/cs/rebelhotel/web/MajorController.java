package edu.unlv.cs.rebelhotel.web;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import edu.unlv.cs.rebelhotel.domain.Major;
import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.form.FormMajor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/majors")
@Controller
public class MajorController {
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Major major, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("major", major);
            return "majors/create";
        }
        major.persist();
        return "redirect:/majors/" + encodeUrlPathSegment(major.getId().toString(), request);
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(value = "/{sid}", params = "forstudent", method = RequestMethod.POST)
    public String createForStudent(@PathVariable("sid") Long sid, @Valid FormMajor formMajor, BindingResult result, Model model, HttpServletRequest request) {
		if (result.hasErrors()) {
			model.addAttribute("formMajor", formMajor);
            Student student = Student.findStudent(sid);
            model.addAttribute("sid", sid);
            
            return "majors/createForStudent";
        }
		
		Student student = Student.findStudent(sid);
		Major major = formMajor.getMajor();
		major.setStudent(student);
		major.persist();
		
		student.getMajors().add(major);
		student.merge();
		
		student.refreshMajorHours();
		
        return "redirect:/students/" + encodeUrlPathSegment(student.getId().toString(), request);
    }
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	@RequestMapping(value = "/{sid}", params = "forstudent", method = RequestMethod.GET)
	public String createFormForStudent(@PathVariable("sid") Long sid, Model model) {
		model.addAttribute("formMajor", new FormMajor());
		Student student = Student.findStudent(sid);
		model.addAttribute("sid", student.getId());
		return "majors/createForStudent";
	}
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model model) {
        model.addAttribute("major", new Major());
        return "majors/create";
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(method = RequestMethod.PUT)
    public String update(@Valid Major major, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("major", major);
            return "majors/update";
        }
        major.merge();
        return "redirect:/students/" + encodeUrlPathSegment(major.getStudent().getId().toString(), request);
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("major", Major.findMajor(id));
        return "majors/update";
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        Major major = Major.findMajor(id);
		Student student = major.getStudent();
		major.remove();
        model.addAttribute("page", (page == null) ? "1" : page.toString());
        model.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/students/"+student.getId();
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("major", Major.findMajor(id));
        model.addAttribute("itemId", id);
        return "majors/show";
    }

	@RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model model) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            model.addAttribute("majors", Major.findMajorEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Major.countMajors() / sizeNo;
            model.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            model.addAttribute("majors", Major.findAllMajors());
        }
        return "majors/list";
    }

	@RequestMapping(params = { "find=ByDegreeCodeAndCatalogTerm", "form" }, method = RequestMethod.GET)
    public String findMajorsByDegreeCodeAndCatalogTermForm(Model model) {
        model.addAttribute("terms", Term.findAllTerms());
        return "majors/findMajorsByDegreeCodeAndCatalogTerm";
    }

	@RequestMapping(params = "find=ByDegreeCodeAndCatalogTerm", method = RequestMethod.GET)
    public String findMajorsByDegreeCodeAndCatalogTerm(@RequestParam("degreeCode") String degreeCode, @RequestParam("catalogTerm") Term catalogTerm, Model model) {
        model.addAttribute("majors", Major.findMajorsByDegreeCodeAndCatalogTerm(degreeCode, catalogTerm).getResultList());
        return "majors/list";
    }

	/*@ModelAttribute("students")
    public Collection<Student> populateStudents() {
        return Student.findAllStudents();
    }*/

	/*@ModelAttribute("terms")
    public Collection<Term> populateTerms() {
        return Term.findAllTerms();
    }*/

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
