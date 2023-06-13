package com.jobhunthth.HTH0205.Models;

import java.io.Serializable;
import java.util.Date;

public class JobsAdModel implements Serializable {
    private String title;
    private String number;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String avatar;
    private String address;
    private String gender;
    private String minAge;

    public JobsAdModel(String avatar) {
        this.avatar = avatar;
    }

    private String maxAge;
    private String typeOfSalary;
    private String minSalary;
    private String maxSalary;
    private String desc;
    private Date currentTime;
    private String role;
    private String idPutJob;
    private String area;
    private String profession;
    private String typeOfWork;
    private String jobId;
    private String exDate;
    private String education;
    private int view;

    public JobsAdModel() {
    }



    public JobsAdModel(String title, String number, String address, String gender, String minAge,
                       String maxAge, String typeOfSalary, String minSalary, String maxSalary,
                       String desc, Date currentTime, String role, String idPutJob, String area,
                       String profession, String typeOfWork, String jobId, String exDate, String education,
                       int view) {
        this.title = title;
        this.number = number;
        this.address = address;
        this.gender = gender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.typeOfSalary = typeOfSalary;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.desc = desc;
        this.currentTime = currentTime;
        this.role = role;
        this.idPutJob = idPutJob;
        this.area = area;
        this.profession = profession;
        this.typeOfWork = typeOfWork;
        this.jobId = jobId;
        this.exDate = exDate;
        this.education = education;
        this.view = view;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public String getJobId() {
        return jobId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExDate() {
        return exDate;
    }

    public void setExDate(String exDate) {
        this.exDate = exDate;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getTypeOfWork() {
        return typeOfWork;
    }

    public void setTypeOfWork(String typeOfWork) {
        this.typeOfWork = typeOfWork;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIdPutJob() {
        return idPutJob;
    }

    public void setIdPutJob(String idPutJob) {
        this.idPutJob = idPutJob;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getTypeOfSalary() {
        return typeOfSalary;
    }

    public void setTypeOfSalary(String typeOfSalary) {
        this.typeOfSalary = typeOfSalary;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(String minSalary) {
        this.minSalary = minSalary;
    }

    public String getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(String maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
