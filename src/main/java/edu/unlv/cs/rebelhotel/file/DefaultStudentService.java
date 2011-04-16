package edu.unlv.cs.rebelhotel.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import edu.unlv.cs.rebelhotel.file.FileStudent;
import edu.unlv.cs.rebelhotel.file.StudentMapper;
import edu.unlv.cs.rebelhotel.file.enums.FileUploadStatus;
import edu.unlv.cs.rebelhotel.domain.Student;


// As with this class...
// anything that is a singleton must be thread safe, because there will be
// multiple threads accessing it simultaneously
// - fields should be defined "final"


@Service
public class DefaultStudentService implements StudentService{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultStudentService.class);
	private Parser parser;
	private Lexer lexer;
	private StudentMapper studentMapper;
	
	@Autowired
	public DefaultStudentService(Parser parser, Lexer lexer, StudentMapper studentMapper){
		this.parser = parser;
		this.lexer = lexer;
		this.studentMapper = studentMapper;
	}
	
	@Async
	@Transactional
	public void upload(FileUpload fileUpload) {
		fileUpload.beginExecution();
		LOG.info("File upload began at: " + fileUpload.getStartOfExecution().toString());
		try {
			List<List<String>> contents = Collections.emptyList();
			contents = lexer.tokenize(new FileReader(fileUpload.getFile()));
			
			Set<FileStudent> fileStudents = parser.parse(contents);
			
			for (FileStudent each : fileStudents) {
				Student student = studentMapper.findOrReplace(each);
				student.setUserId(each.getStudentId());
				student.upsert();
			}
		} catch(Exception e){
			LOG.error("Could not upload student file.", e);
			fileUpload.setMessage("Upload FAILED." + e.getMessage());
			fileUpload.setSuccessful(false);
			throw new FileUploadException("Could not upload student file.",e);
		} finally {
			fileUpload.endExecution();
			if (fileUpload.getSuccessful()) {
				fileUpload.setMessage(FileUploadStatus.SUCCESSFUL.toString());
			}
			fileUpload.persist();
			LOG.info("File upload ended at: " + fileUpload.getEndOfExecution().toString());
		}
	}
}
