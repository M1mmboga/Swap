package com.example.swap.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Good implements Serializable {

    private int id;
    @SerializedName("user_id") private int userId;
    @SerializedName("user") private User user;
    @SerializedName("name") private String name;
    @SerializedName("description") private String description;
    @SerializedName("image_file_name") private String imageFileName;
    @SerializedName("supplementary_good_images") private List<String> supplementaryImageFileNames;
    @SerializedName("price_estimate") private int priceEstimate;
    @SerializedName("category") private String category;
    @SerializedName("location") private String location;
    @SerializedName("created_at") private Date postDate;

    public Good() {
    }

    /*For Good submission*/
//    public Good(User user, String name, String description, ) {
//
//    }

    public Good(int id, User user, String name, String description, String imageFileName, int priceEstimate, String location, Date postDate) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.description = description;
        this.imageFileName = imageFileName;
        this.priceEstimate = priceEstimate;
        this.location = location;
        this.postDate = postDate;
    }

    public Good(int id, int userId, User user, String name, String description, String imageFileName, List<String> supplementaryImageFileNames, int priceEstimate, String category, String location, Date postDate) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.name = name;
        this.description = description;
        this.imageFileName = imageFileName;
        this.supplementaryImageFileNames = supplementaryImageFileNames;
        this.priceEstimate = priceEstimate;
        this.category = category;
        this.location = location;
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

    public int getPriceEstimate() {
        return priceEstimate;
    }

    public void setPriceEstimate(int priceEstimate) {
        this.priceEstimate = priceEstimate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<String> getSupplementaryImageFileNames() {
        return supplementaryImageFileNames;
    }

    public void setSupplementaryImageFileNames(List<String> supplementaryImageFileNames) {
        this.supplementaryImageFileNames = supplementaryImageFileNames;
    }
}
