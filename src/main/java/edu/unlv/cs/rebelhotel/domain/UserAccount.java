package edu.unlv.cs.rebelhotel.domain;

import java.io.Serializable;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import edu.unlv.cs.rebelhotel.domain.enums.UserGroup;
import edu.unlv.cs.rebelhotel.file.FileStudent;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Configurable("userAccount")
@RooJavaBean
@RooToString
@RooEntity(finders = { "findUserAccountsByUserId", "findUserAccountsByUserIdEquals" })
public class UserAccount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6794686778435168726L;
	private static final Logger LOG = LoggerFactory.getLogger("audit");
	private static final Logger DEBUG_LOG = LoggerFactory.getLogger(UserAccount.class);

    private static final int MAX_PASSWORD_LENGTH = 8;
	
    @NotNull
    @Column(unique = true)
    private String userId;

    @Column(unique = true)
    private String email = "default@mail.com";

    private transient MessageDigestPasswordEncoder passwordEncoder;
    
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private UserGroup userGroup;

    private Boolean enabled = Boolean.TRUE;
    

    public static UserAccount fromFileStudent(FileStudent fileStudent) {
    	UserAccount user = new UserAccount();
    	user.setUserId(fileStudent.getStudentId());
    	user.setPassword(user.generateRandomPassword());
    	user.setEmail(fileStudent.getEmail());
    	user.setUserGroup(UserGroup.ROLE_USER);
    	return user;
    }
    
    public static UserAccount fromStudent(Student student, String email) {
    	UserAccount user = new UserAccount();
    	user.setUserId(student.getUserId());
    	user.setPassword(user.generateRandomPassword());
    	user.setEmail(email);
    	user.setUserGroup(UserGroup.ROLE_USER);
    	return user;
    }
    
    public void setPassword(String password) {
        String encoded = passwordEncoder.encodePassword(password, null);
        this.password = encoded;
    }
    
    public void setPasswordEncoder(MessageDigestPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    public String generateRandomPassword(){
    	final int MAX_PASSWORD_LENGTH = 8;
		final String charset = "12345ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&abcdefghijklmnopqrstuvwxyz67890";
		final String firstcharset = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();

		Integer pos;
		pos = random.nextInt(firstcharset.length());
		sb.append(firstcharset.charAt(pos));
		for (int i = 1; i < MAX_PASSWORD_LENGTH; i++) {
			pos = random.nextInt(charset.length());
        	sb.append(charset.charAt(pos));
		}
		return sb.toString();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserId: ").append(getUserId()).append(", ");
        sb.append("UserGroup: ").append(getUserGroup());
        return sb.toString();
    }

  
	public boolean matchesCurrentPassword (String unencryptedPassword){
    	 String encryptedPassword = passwordEncoder.encodePassword(unencryptedPassword, null);
    	 return encryptedPassword.equals(password);
    }
	public void audit(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasAuthentication = (null != authentication);
		String userName = "";
		if (hasAuthentication) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				userName = ((UserDetails) principal).getUsername();
			} else {
				userName = principal.toString();
			}
		}

		LOG.info("User {} updated student {}.", new Object[]{userName, userId});
	}
    
    @PrePersist
    public void createNewUserAccount() {
    	DEBUG_LOG.debug("Created new user account: " + toString());
    }
    
    @PreUpdate
    public void updateUserAccount() {
    	DEBUG_LOG.debug("Updated user account: " + toString());
    }
}
