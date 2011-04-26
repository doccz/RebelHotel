package edu.unlv.cs.rebelhotel.file;

import java.io.File;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

import edu.unlv.cs.rebelhotel.file.enums.FileUploadStatus;

@RooJavaBean
@RooToString
@RooEntity
public class UploadProgress {
	@Transient
	private transient File file;
	
	private FileUploadStatus fileUploadStatus = FileUploadStatus.PENDING;
	
	private String message = "N/A";
	
	private Boolean successful = Boolean.TRUE;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style="S-")
	private Date startOfExecution;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style="S-")
	private Date endOfExecution;

    public UploadProgress(){}
   
    public UploadProgress(File file){
    	this.file = file;
    }
    
    public File getFile() {
    	return this.file;
    }
    
	public void beginExecution() {
		setStartOfExecution(new Date());
	}

	public void endExecution() {
		setEndOfExecution(new Date());
	}
}
