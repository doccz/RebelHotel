// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.file;

import edu.unlv.cs.rebelhotel.domain.UserAccount;
import java.lang.String;

privileged aspect EmailInformation_Roo_JavaBean {
    
    public UserAccount EmailInformation.getUserAccount() {
        return this.userAccount;
    }
    
    public void EmailInformation.setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    
    public String EmailInformation.getPassword() {
        return this.password;
    }
    
    public void EmailInformation.setPassword(String password) {
        this.password = password;
    }
    
    public boolean EmailInformation.isNewStudent() {
        return this.newStudent;
    }
    
    public void EmailInformation.setNewStudent(boolean newStudent) {
        this.newStudent = newStudent;
    }
    
}
