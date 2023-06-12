package com.jobhunthth.HTH0205.Models;

public class CompanyInfoModel {
    private String companyAvatar;
    private String companyName;
    private String companyScale;
    private String companyIndustry;
    private String companyPhone;
    private String companyWebsite;
    private String companyAddress;
    private String companyDescription;
    private String accountId;

    public CompanyInfoModel() {
    }

    public CompanyInfoModel(String companyAvatar, String companyName, String companyScale, String companyIndustry, String companyPhone,
                            String companyWebsite, String companyAddress, String companyDescription, String accountId) {
        this.companyAvatar = companyAvatar;
        this.companyName = companyName;
        this.companyScale = companyScale;
        this.companyIndustry = companyIndustry;
        this.companyPhone = companyPhone;
        this.companyWebsite = companyWebsite;
        this.companyAddress = companyAddress;
        this.companyDescription = companyDescription;
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyAvatar() {
        return companyAvatar;
    }

    public void setCompanyAvatar(String companyAvatar) {
        this.companyAvatar = companyAvatar;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyScale() {
        return companyScale;
    }

    public void setCompanyScale(String companyScale) {
        this.companyScale = companyScale;
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }
}
