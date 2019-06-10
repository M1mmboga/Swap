package com.example.swap.datasources.goods.goodfetchers;

import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.models.GoodsPage;

import retrofit2.Call;

public class SoughtGoodsGoodsFetcher extends GoodsFetcher {
    private String searchQuery;
    private  String category;

    public SoughtGoodsGoodsFetcher(String searchQuery, String category) {
        this.searchQuery = searchQuery;
        this.category = category;
    }

    @Override
    protected Call<GoodsPage> fetch(int pageNumber) {
        return goodsDao.findGoods(this.searchQuery, this.category);
    }
}
