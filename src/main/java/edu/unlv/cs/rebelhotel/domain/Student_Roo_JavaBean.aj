// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.Term;
import edu.unlv.cs.rebelhotel.domain.UserAccount;
import edu.unlv.cs.rebelhotel.domain.ViewProgress;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.domain.WorkRequirement;
import java.lang.String;
import java.util.Set;

privileged aspect Student_Roo_JavaBean {
    
    public String Student.getUserId() {
        return this.userId;
    }
    
    public void Student.setUserId(String userId) {
        this.userId = userId;
    }
    
    public String Student.getEmail() {
        return this.email;
    }
    
    public void Student.setEmail(String email) {
        this.email = email;
    }
    
    public String Student.getFirstName() {
        return this.firstName;
    }
    
    public void Student.setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String Student.getMiddleName() {
        return this.middleName;
    }
    
    public void Student.setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public String Student.getLastName() {
        return this.lastName;
    }
    
    public void Student.setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public Set<WorkRequirement> Student.getWorkRequirements() {
        return this.workRequirements;
    }
    
    public void Student.setWorkRequirements(Set<WorkRequirement> workRequirements) {
        this.workRequirements = workRequirements;
    }
    
    public String Student.getMajor1() {
        return this.major1;
    }
    
    public void Student.setMajor1(String major1) {
        this.major1 = major1;
    }
    
    public String Student.getMajor2() {
        return this.major2;
    }
    
    public void Student.setMajor2(String major2) {
        this.major2 = major2;
    }
    
    public Term Student.getAdmitTerm() {
        return this.admitTerm;
    }
    
    public void Student.setAdmitTerm(Term admitTerm) {
        this.admitTerm = admitTerm;
    }
    
    public Term Student.getGradTerm() {
        return this.gradTerm;
    }
    
    public void Student.setGradTerm(Term gradTerm) {
        this.gradTerm = gradTerm;
    }
    
    public Set<WorkEffort> Student.getWorkEffort() {
        return this.workEffort;
    }
    
    public void Student.setWorkEffort(Set<WorkEffort> workEffort) {
        this.workEffort = workEffort;
    }
    
    public Set<ViewProgress> Student.getMilestone() {
        return this.milestone;
    }
    
    public void Student.setMilestone(Set<ViewProgress> milestone) {
        this.milestone = milestone;
    }
    
    public UserAccount Student.getUserAccount() {
        return this.userAccount;
    }
    
    public void Student.setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    
}
