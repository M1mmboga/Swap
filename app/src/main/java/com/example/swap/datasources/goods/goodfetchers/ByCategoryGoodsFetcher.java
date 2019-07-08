package com.example.swap.datasources.goods.goodfetchers;

import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.models.GoodsPage;

import retrofit2.Call;

public class ByCategoryGoodsFetcher extends GoodsFetcher {

    private final String category;

    public ByCategoryGoodsFetcher(String category) {
        this.category = category;
    }

    public Call<GoodsPage> fetch(int pageNumber) {
        return goodsService.fetchGoodsByCategory(pageNumber, this.category);
    }
}