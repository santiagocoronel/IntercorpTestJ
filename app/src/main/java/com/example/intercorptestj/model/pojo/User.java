package com.example.intercorptestj.model.pojo;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class User {

    private String uuid;
    private String firstName;
    private String lastName;
    private Integer age;
    private Date birthdate;

    public User() {
    }

    public User(String uuid, String firstName, String lastName, Integer age, Date birthdate) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.birthdate = birthdate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Integer calculateAge() {
        Log.w(
                "User.calculateAge",
                "**WARNING the age is calculating by the birthdate, the result depends from the device date."
        );

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(birthdate);

        Integer age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}
