package edu.unlv.cs.rebelhotel.form;

// A form backing class for use in the work requirement creation form (which lists work templates)
public class FormChangePassword {
	
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	
	public String getNewPassword(){
		return newPassword;
	}
	
	public String getConfirmPassword(){
		return confirmPassword;
	}
	

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}