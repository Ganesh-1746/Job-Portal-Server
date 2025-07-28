package com.example.JobPortal.Model.DTO;

import com.example.JobPortal.Model.Role;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String mobileNo;
    private String location;
    private Role role;
    private String resumeurl;

    public UserResponseDTO(Long id,String name, String email, String mobileNo, String location, Role role, String resumeurl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobileNo = mobileNo;
        this.location = location;
        this.role = role;
        this.resumeurl = resumeurl;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getResumeurl() {
        return resumeurl;
    }

    public void setResumeurl(String resumeurl) {
        this.resumeurl = resumeurl;
    }
}
