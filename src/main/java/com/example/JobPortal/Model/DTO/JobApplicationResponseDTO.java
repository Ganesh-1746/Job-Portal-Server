package com.example.JobPortal.Model.DTO;

import java.time.LocalDate;

public class JobApplicationResponseDTO {

    private Long jobId;
    private String jobTitle;
    private String companyName;
    private LocalDate appliedDate;

    public JobApplicationResponseDTO(Long jobId, String jobTitle, String companyName, LocalDate appliedDate) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }
}
