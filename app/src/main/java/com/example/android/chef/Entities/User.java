package com.example.android.chef.Entities;

import java.io.Serializable;

/**
 * Created by jenil on 04-03-2018.
 */

public class User implements Serializable {
    private String username;
    private String password;
    private String type;
    private String catagory;

    public User(String username, String password, String type, String catagory) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.catagory = catagory;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }
}
