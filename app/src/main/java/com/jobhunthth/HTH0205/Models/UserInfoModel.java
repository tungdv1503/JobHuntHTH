package com.jobhunthth.HTH0205.Models;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserInfoModel {

    private String name;
    private String birth;
    private String phone;
    private String email;
    private String address;
    private String gender;
    private String avatar;

    public UserInfoModel() {
    }

    public UserInfoModel(String name, String birth, String phone, String email, String address, String gender, String avatar) {
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.gender = gender;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAge() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date birthDate = dateFormat.parse(getBirth());

            Calendar currentDate = Calendar.getInstance();

            int currentYear = currentDate.get(Calendar.YEAR);

            Calendar birthCalendar = Calendar.getInstance();
            birthCalendar.setTime(birthDate);
            int birthYear = birthCalendar.get(Calendar.YEAR);

            int age = currentYear - birthYear;

            int currentMonth = currentDate.get(Calendar.MONTH);
            int currentDayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);
            int birthMonth = birthCalendar.get(Calendar.MONTH);
            int birthDayOfMonth = birthCalendar.get(Calendar.DAY_OF_MONTH);
            if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDayOfMonth < birthDayOfMonth)) {
                age--;
            }

            return age;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
