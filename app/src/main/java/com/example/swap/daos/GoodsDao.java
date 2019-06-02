package com.example.swap.daos;

import com.example.swap.models.GoodsPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodsDao {

    @GET("goods/paged")
    Call<GoodsPage> fetchGoodsByCategory(@Query("page") int page,
                                         @Query("category") String category);

    @GET("goods/find")
    Call<GoodsPage> findGoods(@Query("query") String query,
                              @Query("category") String category);

}
