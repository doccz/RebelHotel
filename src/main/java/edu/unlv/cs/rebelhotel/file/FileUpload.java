package edu.unlv.cs.rebelhotel.file;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import edu.unlv.cs.rebelhotel.file.enums.FileUploadStatus;

@Configurable
@Entity
public class FileUpload {
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

    public FileUpload(){}
   
    public FileUpload(File file){
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

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("File: ").append(getFile()).append(", ");
        sb.append("FileUploadStatus: ").append(getFileUploadStatus()).append(", ");
        sb.append("Message: ").append(getMessage()).append(", ");
        sb.append("Successful: ").append(getSuccessful()).append(", ");
        sb.append("StartOfExecution: ").append(getStartOfExecution()).append(", ");
        sb.append("EndOfExecution: ").append(getEndOfExecution());
        return sb.toString();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FileUpload attached = FileUpload.findFileUpload(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public FileUpload merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FileUpload merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new FileUpload().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countFileUploads() {
        return entityManager().createQuery("select count(o) from FileUpload o", Long.class).getSingleResult();
    }

	public static List<FileUpload> findAllFileUploads() {
        return entityManager().createQuery("select o from FileUpload o", FileUpload.class).getResultList();
    }

	public static FileUpload findFileUpload(Long id) {
        if (id == null) return null;
        return entityManager().find(FileUpload.class, id);
    }

	public static List<FileUpload> findFileUploadEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from FileUpload o", FileUpload.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public FileUploadStatus getFileUploadStatus() {
        return this.fileUploadStatus;
    }

	public void setFileUploadStatus(FileUploadStatus fileUploadStatus) {
        this.fileUploadStatus = fileUploadStatus;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public Boolean getSuccessful() {
        return this.successful;
    }

	public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

	public Date getStartOfExecution() {
        return this.startOfExecution;
    }

	public void setStartOfExecution(Date startOfExecution) {
        this.startOfExecution = startOfExecution;
    }

	public Date getEndOfExecution() {
        return this.endOfExecution;
    }

	public void setEndOfExecution(Date endOfExecution) {
        this.endOfExecution = endOfExecution;
    }
}
