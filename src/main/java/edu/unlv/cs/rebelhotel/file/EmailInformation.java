package edu.unlv.cs.rebelhotel.file;

import org.springframework.roo.addon.javabean.RooJavaBean;

import edu.unlv.cs.rebelhotel.domain.UserAccount;

@RooJavaBean
public class EmailInformation {
	private UserAccount userAccount;
	private String password;
	private boolean newStudent = Boolean.TRUE;
	
	public EmailInformation (UserAccount userAccount, String password, boolean newStudent) {
		this.userAccount = userAccount;
		this.password = password;
		this.newStudent = newStudent;
	}
}
