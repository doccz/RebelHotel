package edu.unlv.cs.rebelhotel.web;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.unlv.cs.rebelhotel.file.FileUpload;
import edu.unlv.cs.rebelhotel.file.StudentService;

@RooWebScaffold(path = "fileuploads", formBackingObject = FileUpload.class)
@RequestMapping("/fileuploads")
@Controller
public class FileUploadController {
	private StudentService studentService;

	@Autowired
	public void setStudentService(StudentService studentService) {
		this.studentService = studentService;
	}
	
	@RequestMapping(params = "upload", method = RequestMethod.GET)
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public String uploadForm(Model model) {
		return "fileuploads/upload";
	}

	@RequestMapping(params = "upload", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER')")
	public String uploadFormHandler(@RequestParam("file") MultipartFile multipartFile, Model model) throws IOException {
		if (multipartFile.isEmpty()) {
			return "fileuploads/upload";
		}
		
		File file = File.createTempFile("students",".csv");
		multipartFile.transferTo(file);
		FileUpload fileUpload = new FileUpload(file);
		studentService.upload(fileUpload);
		
		model.addAttribute("fileuploads", FileUpload.findAllFileUploads());
		return "fileuploads/list";
	}
}
