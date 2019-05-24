package com.johngachihi.swap.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoodsPage {

    @SerializedName("current_page") private int currentPage;
    @SerializedName("data") private List<Good> goods;
    @SerializedName("next_page_url") private String nextPageUrl;
    @SerializedName("prev_page_url") private String prevPageUrl;
    @SerializedName("per_page") private int perPage;
    @SerializedName("total") private int total;

//    private int firstGoodPosition;
//    private int nextPage;
//    private int previousPage;

    public GoodsPage() {
    }

    public GoodsPage(int currentPage, List<Good> goods, String nextPageUrl, String prevPageUrl, int perPage, int total) {
        this.currentPage = currentPage;
        this.goods = goods;
        this.nextPageUrl = nextPageUrl;
        this.prevPageUrl = prevPageUrl;
        this.perPage = perPage;
        this.total = total;
    }

    public int getFirstGoodPosition() {
        return perPage * (currentPage - 1);
    }

    public Integer getNextPage() {
        int nextPage = currentPage + 1;
        if(currentPage < total) {
            return nextPage;
        }
        return null;
    }

    public Integer getPreviousPage() {
        int prevPage = currentPage - 1;
        return prevPage < 1 ? null : prevPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage > total) {
            throw new IndexOutOfBoundsException("Current page set is greater than total pages");
        }
        this.currentPage = currentPage;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
