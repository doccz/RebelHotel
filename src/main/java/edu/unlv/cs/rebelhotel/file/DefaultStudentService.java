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
				if(student.exists()){
					student.merge();
					LOG.debug("Updating student: " + student.toString());
				} else {
					student.persist();
					LOG.debug("Creating new student: " + student.toString());
				}
			}
		} catch(Exception e){
			LOG.error("Could not upload student file.", e);
			throw e;
		} finally {
			fileUpload.endExecution();
			fileUpload.persist();
			// LOG message
			LOG.info("File upload ended at: " + fileUpload.getEndOfExecution().toString());
		}
	}
}
