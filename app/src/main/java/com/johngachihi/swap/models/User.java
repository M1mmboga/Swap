package com.johngachihi.swap.models;

import com.google.gson.annotations.SerializedName;

public class User {

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
