package com.jobhunthth.HTH0205.Models;

import java.util.List;

public class UserProfileModel {

    private String avatar;
    private String cv;
    private String profession;
    private String minSalary;
    private String maxSalary;
    private String typeSalary;
    private String education;
    private List<String> skill;

    public UserProfileModel() {
    }

    public UserProfileModel(String avatar, String cv, String profession, String minSalary,
                            String maxSalary, String typeSalary, String education, List<String> skill) {
        this.avatar = avatar;
        this.cv = cv;
        this.profession = profession;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.typeSalary = typeSalary;
        this.education = education;
        this.skill = skill;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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

    public String getTypeSalary() {
        return typeSalary;
    }

    public void setTypeSalary(String typeSalary) {
        this.typeSalary = typeSalary;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public List<String> getSkill() {
        return skill;
    }

    public void setSkill(List<String> skill) {
        this.skill = skill;
    }

    public String getSalary(){
        return getMinSalary()+"-"+getMaxSalary()+" "+getTypeSalary()+"/Tháng";
    }
}
