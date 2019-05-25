package com.johngachihi.swap.daos;

import com.johngachihi.swap.models.GoodsPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoodsDao {

    @GET("goods/paged")
    Call<GoodsPage> fetchGoodsByCategory(@Query("page") int page,
                                         @Query("category") String category);
}
