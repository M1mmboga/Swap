package com.example.swap.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Offer {
    @SerializedName("id") private int id;
    @SerializedName("good_offered_for") private Good wantedGood;
    @SerializedName("offered_goods") private List<Good> offeredGoods;
    @SerializedName("bidder") private User bidder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Good> getOfferedGoods() {
        return offeredGoods;
    }

    public void setOfferedGoods(List<Good> offeredGoods) {
        this.offeredGoods = offeredGoods;
    }

    public User getBidder() {
        return bidder;
    }

    public void setBidder(User bidder) {
        this.bidder = bidder;
    }

    public Good getWantedGood() {
        return wantedGood;
    }

    public void setWantedGood(Good wantedGood) {
        this.wantedGood = wantedGood;
    }
}
