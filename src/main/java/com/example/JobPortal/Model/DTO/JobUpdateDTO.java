package com.example.JobPortal.Model.DTO;

import com.example.JobPortal.Model.Department;
import com.example.JobPortal.Model.JobType;

import java.time.LocalDate;

public class JobUpdateDTO {

    private String title;
    private String description;
    private String experience;
    private String location;
    private JobType jobType;
    private Department department;
    private String salary;
    private LocalDate postedDate;
    private Long companyId;

    public String getTitle() {
        return title;
    }

    public JobUpdateDTO(String title, String description, String experience, String location, JobType jobType, Department department,String salary, LocalDate postedDate,Long companyId) {
        this.title = title;
        this.description = description;
        this.experience = experience;
        this.location = location;
        this.jobType = jobType;
        this.department = department;
        this.salary = salary;
        this.postedDate = postedDate;
        this.companyId = companyId;
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

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary){
        this.salary = salary;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setComapanyId(Long comapanyId) {
        this.companyId = comapanyId;
    }
}
