package edu.unlv.cs.rebelhotel.domain;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

@Configurable("userAccount")
@RooJavaBean
@RooToString
@RooEntity(finders = { "findUserAccountsByUserId", "findUserAccountsByUserIdEquals" })
public class UserAccount {
	
	private static final Logger LOG = LoggerFactory.getLogger("audit");

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
    
    private static final int MAX_PASSWORD_LENGTH = 8;
    
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserId: ").append(getUserId()).append(", ");
        sb.append("UserGroup: ").append(getUserGroup());
        return sb.toString();
    }
    
    public String generateRandomPassword(){
		String charset = "12345ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%&abcdefghijklmnopqrstuvwxyz67890";
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		
		Integer pos;
		for (int i = 0; i < MAX_PASSWORD_LENGTH; i++) {
			pos = random.nextInt(charset.length());
        	sb.append(charset.charAt(pos));
		}
		
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
}
