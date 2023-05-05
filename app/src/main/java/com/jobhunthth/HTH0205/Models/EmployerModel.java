package com.jobhunthth.HTH0205.Models;

public class EmployerModel {
    private String id;
    private String email;
    private String name;
    private String phone_number;
    private String address;

    public EmployerModel(String id, String email, String name, String phone_number, String address) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.address = address;
    }

    public EmployerModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
