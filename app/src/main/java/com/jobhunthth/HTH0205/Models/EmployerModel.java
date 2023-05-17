package com.jobhunthth.HTH0205.Models;

import android.net.Uri;

public class EmployerModel {
    private String email;
    private String avatar;
    private String name;
    private String phone_number;
    private String address;

    public EmployerModel(String email, String avatar, String name, String phone_number, String address) {
        this.email = email;
        this.avatar = avatar;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
    }

    public EmployerModel() {
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
