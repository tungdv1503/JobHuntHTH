package com.jobhunthth.HTH0205.Models;

import java.io.Serializable;
import java.util.Date;

public class JobsAdModel implements Serializable {
    private String id_Company;
    private String title;
    private String job_Description;
    private String type_Job;
    private String job_Profession;
    private Date createAt;

    public JobsAdModel(String id_Company, String title, String job_Description, String type_Job,
                       String job_Profession, Date createAt) {
        this.id_Company = id_Company;
        this.title = title;
        this.job_Description = job_Description;
        this.type_Job = type_Job;
        this.job_Profession = job_Profession;
        this.createAt = createAt;
    }

    public JobsAdModel() {
    }

    public String getJob_Profession() {
        return job_Profession;
    }

    public void setJob_Profession(String job_Profession) {
        this.job_Profession = job_Profession;
    }

    public String getId_Company() {
        return id_Company;
    }

    public void setId_Company(String id_Company) {
        this.id_Company = id_Company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJob_Description() {
        return job_Description;
    }

    public void setJob_Description(String job_Description) {
        this.job_Description = job_Description;
    }

    public String getType_Job() {
        return type_Job;
    }

    public void setType_Job(String type_Job) {
        this.type_Job = type_Job;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
