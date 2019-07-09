package com.example.swap.models.dtos;

import com.google.gson.annotations.SerializedName;

public class GoodDTO {

    @SerializedName("category") private String category;
    @SerializedName("name") private String name;
    @SerializedName("description") private String description;
    @SerializedName("location") private String location;
    @SerializedName("price_estimate") private String priceEstimate;

    public GoodDTO(String category, String name, String description, String location, String priceEstimate) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.location = location;
        this.priceEstimate = priceEstimate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPriceEstimate() {
        return priceEstimate;
    }

    public void setPriceEstimate(String priceEstimate) {
        this.priceEstimate = priceEstimate;
    }
}
