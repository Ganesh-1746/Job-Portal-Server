package com.example.JobPortal.Model.DTO;

import com.example.JobPortal.Model.ApplicationStatus;

import java.time.LocalDate;

public class JobApplyDTO {

    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private LocalDate appliedDate;

    public JobApplyDTO(Long applicationId, String applicantName, String applicantEmail, String jobTitle, String companyName, ApplicationStatus status, LocalDate appliedDate) {
        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
