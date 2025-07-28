package com.example.JobPortal.Model.DTO;

public class UserUpdateDTO {
    private String name;
    private String mobileNo;
    private String location;
    private String resumeurl;

    public UserUpdateDTO(String name, String mobileNo, String location,  String resumeurl) {
        this.name = name;
        this.mobileNo = mobileNo;
        this.location = location;
        this.resumeurl = resumeurl;
    }

    public UserUpdateDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getResumeurl() {
        return resumeurl;
    }

    public void setResumeurl(String resumeurl) {
        this.resumeurl = resumeurl;
    }
}
