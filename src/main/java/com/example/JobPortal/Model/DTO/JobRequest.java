package com.example.JobPortal.Model.DTO;

import com.example.JobPortal.Model.Department;
import com.example.JobPortal.Model.JobType;

import java.time.LocalDate;

public class JobRequest {

    private String title;
    private String description;
    private String experience;
    private String location;
    private JobType jobType;
    private Department department;
    private LocalDate postedDate;
    private String salary;
    private Long companyId;

    public JobRequest(String title, String description,String experience, String location, JobType jobType, Department department,LocalDate postedDate,String salary, Long companyId) {
        this.title = title;
        this.description = description;
        this.experience = experience;
        this.location = location;
        this.jobType = jobType;
        this.department = department;
        this.postedDate = postedDate;
        this.salary = salary;
        this.companyId = companyId;
    }

    public JobRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary){
        this.salary = salary;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

}
