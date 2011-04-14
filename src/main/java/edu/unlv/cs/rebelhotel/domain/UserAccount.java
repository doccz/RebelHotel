package edu.unlv.cs.rebelhotel.domain;

import java.util.Random;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
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
@RooEntity(finders = { "findUserAccountsByUserId" })
public class UserAccount {

    @NotNull
    @Column(unique = true)
    private String userId;

    private transient MessageDigestPasswordEncoder passwordEncoder;

    @NotNull
    private String password;

    @NotNull
    private String email;
    
    @Enumerated(EnumType.STRING)
    private UserGroup userGroup;

    private Boolean enabled = Boolean.TRUE;

   /* public UserAccount(FileStudent fileStudent) {
    	this.userId = fileStudent.getStudentId();
    	setPasswordEncoder(new MessageDigestPasswordEncoder("SHA-256"));
     	setPassword(generateRandomPassword());
    	this.email = fileStudent.getEmail();
    	this.userGroup = UserGroup.ROLE_USER;
    }*/
    
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
    
}
