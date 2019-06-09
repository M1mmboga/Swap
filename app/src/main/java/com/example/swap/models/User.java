package com.example.swap.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    @SerializedName("email") private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
