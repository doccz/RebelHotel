package edu.unlv.cs.rebelhotel.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	public void upload(FileUpload fileUpload) {
		StopWatch watch = new StopWatch();
		watch.start();
		fileUpload.beginExecution();
		fileUpload.persist();
		LOG.error("File upload began at: " + fileUpload.getStartOfExecution().toString());
		try {
			fileToStudents(fileUpload);
		} catch(Exception e){
			StringBuilder sb = new StringBuilder();
			sb.append(e.getMessage());
			fileUpload.setMessage(sb.toString());
			fileUpload.setFileUploadStatus(FileUploadStatus.FAILED);
			fileUpload.setSuccessful(false);
			LOG.error("Could not upload student file.", e);
			throw new FileUploadException("Could not upload student file.",e);
		} finally {
			fileUpload.endExecution();
			if (fileUpload.getSuccessful()) {
				fileUpload.setFileUploadStatus(FileUploadStatus.SUCCESSFUL);
			}
			fileUpload.merge();
			LOG.error("File upload ended at: " + fileUpload.getEndOfExecution().toString());
			watch.stop();
			
			StringBuilder sb = new StringBuilder();
			sb.append("Seconds: ").append(watch.getTotalTimeSeconds()).append("\n").
			append("Short summary: ").append(watch.shortSummary());
			LOG.error("Took this long... " + sb.toString());
		}
	}

	@Transactional
	private void fileToStudents(FileUpload fileUpload) throws IOException, FileNotFoundException {
		List<List<String>> contents = Collections.emptyList();
		contents = lexer.tokenize(new FileReader(fileUpload.getFile()));
		
		Set<FileStudent> fileStudents = parser.parse(contents);
		
		for (FileStudent each : fileStudents) {
			Student student = studentMapper.findOrReplace(each);
			student.setUserId(each.getStudentId());
			student.upsert();
		}
	}
}
