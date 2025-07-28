package com.example.JobPortal.Model.DTO;

import com.example.JobPortal.Model.ApplicationStatus;
import com.example.JobPortal.Model.Department;
import com.example.JobPortal.Model.JobType;

import java.time.LocalDate;

public class AppliedJobResponseDTO {

    private Long jobId;
    private String jobTitle;
    private String description;
    private String experience;
    private String location;
    private JobType jobType;
    private Department department;
    private String salary;
    private String companyName;
    private ApplicationStatus status;
    private LocalDate appliedDate;

    public AppliedJobResponseDTO(Long jobId, String jobTitle, String description, String experience, String location, JobType jobType, Department department, String salary, String companyName, ApplicationStatus status, LocalDate appliedDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.description = description;
        this.experience = experience;
        this.location = location;
        this.jobType = jobType;
        this.department = department;
        this.salary = salary;
        this.companyName = companyName;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

}
