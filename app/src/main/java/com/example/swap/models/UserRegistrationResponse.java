package com.example.swap.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRegistrationResponse implements Serializable {

    @SerializedName("error") boolean error;
    @SerializedName("user") User user;

    public UserRegistrationResponse() {
    }

    public UserRegistrationResponse(boolean error, User user) {
        this.error = error;
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
