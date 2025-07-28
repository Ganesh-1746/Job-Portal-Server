package com.example.JobPortal.Model.DTO;

import java.util.List;

public class CompanyRequestDTO {

    private String name;
    private String description;
    private List<String> jobs;

    public CompanyRequestDTO(String name, String description, List<String> jobs) {
        this.name = name;
        this.description = description;
        this.jobs = jobs;
    }

    public CompanyRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getJobs() {
        return jobs;
    }

    public void setJobs(List<String> jobs) {
        this.jobs = jobs;
    }
}
