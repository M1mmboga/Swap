package com.example.swap.models;

import com.google.gson.annotations.SerializedName;

public class SimpleRestResponse {
    @SerializedName("message") private String message;
    @SerializedName("code") private int responseCode;

    public SimpleRestResponse() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}