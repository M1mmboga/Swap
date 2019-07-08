package com.example.swap.datasources.goods;

import com.example.swap.data.network.retrofit.services.GoodsService;
import com.example.swap.models.GoodsPage;
import com.example.swap.utils.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Retrofit;

public abstract class GoodsFetcher {

    protected Retrofit retrofit;
    protected GoodsService goodsService;

    protected GoodsFetcher() {
        retrofit = RetrofitFactory.create();
        goodsService = retrofit.create(GoodsService.class);
    }

    abstract protected Call<GoodsPage> fetch(int pageNumber);
}
