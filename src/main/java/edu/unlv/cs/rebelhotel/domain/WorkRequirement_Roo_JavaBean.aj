// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import java.lang.Integer;
import java.lang.String;
import java.util.Set;

privileged aspect WorkRequirement_Roo_JavaBean {
    
    public String WorkRequirement.getName() {
        return this.name;
    }
    
    public void WorkRequirement.setName(String name) {
        this.name = name;
    }
    
    public Integer WorkRequirement.getHours() {
        return this.hours;
    }
    
    public void WorkRequirement.setHours(Integer hours) {
        this.hours = hours;
    }
    
    public Student WorkRequirement.getStudent() {
        return this.student;
    }
    
    public void WorkRequirement.setStudent(Student student) {
        this.student = student;
    }
    
    public Set<WorkEffort> WorkRequirement.getWorkEffort() {
        return this.workEffort;
    }
    
    public void WorkRequirement.setWorkEffort(Set<WorkEffort> workEffort) {
        this.workEffort = workEffort;
    }
    
}
