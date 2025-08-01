package com.example.JobPortal.Response;

public class RegisterResponse {

    private String message;
    private String email;
    private String name;
    private String resumeUrl;

    public RegisterResponse(String message, String email, String name, String resumeUrl) {
        this.message = message;
        this.email = email;
        this.name = name;
        this.resumeUrl = resumeUrl;
    }

    public RegisterResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}
