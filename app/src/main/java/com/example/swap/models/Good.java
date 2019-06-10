package com.example.swap.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Good {

    private int id;
    @SerializedName("user_id") private int userId;//
    @SerializedName("user") private User user;//
    @SerializedName("name") private String name;//
    @SerializedName("description") private String description;//
    @SerializedName("image_file_name") private String imageFileName;//
    @SerializedName("price_range_min") private int priceRangeMin;
    @SerializedName("price_range_max") private int priceRangeMax;
    @SerializedName("category") private String category;
    @SerializedName("created_at") private Date postDate;

    public Good() {
    }

    public Good(int id, User user, String name, String description, String imageFileName, int priceRangeMin, int priceRangeMax, Date postDate) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.description = description;
        this.imageFileName = imageFileName;
        this.priceRangeMin = priceRangeMin;
        this.priceRangeMax = priceRangeMax;
        this.postDate = postDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean equals(@Nullable Good good) {
        if(good != null && this.id == good.getId()) {
            return true;
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOfferer() {
        return user;
    }

    public void setOfferer(User user) {
        this.user = user;
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

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public int getPriceRangeMin() {
        return priceRangeMin;
    }

    public void setPriceRangeMin(int priceRangeMin) {
        this.priceRangeMin = priceRangeMin;
    }

    public int getPriceRangeMax() {
        return priceRangeMax;
    }

    public void setPriceRangeMax(int priceRangeMax) {
        this.priceRangeMax = priceRangeMax;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}
