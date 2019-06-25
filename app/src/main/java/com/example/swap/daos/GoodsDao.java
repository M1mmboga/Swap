package com.example.swap.daos;

import com.example.swap.models.Good;
import com.example.swap.models.GoodsPage;
import com.example.swap.models.dtos.GoodDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GoodsDao {

    @GET("goods/paged")
    Call<GoodsPage> fetchGoodsByCategory(@Query("page") int page,
                                         @Query("category") String category);

    @GET("goods/find")
    Call<GoodsPage> findGoods(@Query("query") String query,
                              @Query("category") String category);
    @Multipart
    @POST("goods")
    Call<Good> addGood(@Body GoodDTO goodDTO);

}
