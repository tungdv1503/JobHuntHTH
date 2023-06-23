package com.jobhunthth.HTH0205.Models;

import java.util.Date;

public class ApplicantsModel {

    private String idJob;
    private String idSeeker;
    private String idApplicants;
    private int state;
    private Date date;

    public ApplicantsModel() {
    }

    public ApplicantsModel(String idJob, String idSeeker, String idApplicants, int state, Date date) {
        this.idJob = idJob;
        this.idSeeker = idSeeker;
        this.idApplicants = idApplicants;
        this.state = state;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }

    public String getIdSeeker() {
        return idSeeker;
    }

    public void setIdSeeker(String idSeeker) {
        this.idSeeker = idSeeker;
    }

    public String getIdApplicants() {
        return idApplicants;
    }

    public void setIdApplicants(String idApplicants) {
        this.idApplicants = idApplicants;
    }
}
