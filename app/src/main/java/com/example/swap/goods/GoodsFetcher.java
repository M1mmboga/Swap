package com.example.swap.goods;

import com.example.swap.daos.GoodsDao;
import com.example.swap.models.GoodsPage;
import com.example.swap.rest.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Retrofit;

public abstract class GoodsFetcher {

    protected Retrofit retrofit;
    protected GoodsDao goodsDao;

    protected GoodsFetcher() {
        retrofit = RetrofitFactory.create();
        goodsDao = retrofit.create(GoodsDao.class);
    }

    abstract protected Call<GoodsPage> fetch(int pageNumber);
}
