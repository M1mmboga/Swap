package com.example.swap.datasources.goods.goodfetchers;

import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.models.GoodsPage;

import retrofit2.Call;

public class UserGoodsFetcher extends GoodsFetcher {
    private int userId;

    public UserGoodsFetcher(int userId) {
        this.userId = userId;
    }

    @Override
    protected Call<GoodsPage> fetch(int pageNumber) {
        return goodsService.fetchGoodsByUserIdPaged(userId, pageNumber);
    }
}
