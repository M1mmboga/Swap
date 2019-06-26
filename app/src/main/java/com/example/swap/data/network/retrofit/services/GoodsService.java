package com.example.swap.data.network.retrofit.services;

import com.example.swap.models.Good;
import com.example.swap.models.GoodsPage;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface GoodsService {

    @GET("goods/paged")
    Call<GoodsPage> fetchGoodsByCategory(@Query("page") int page,
                                         @Query("category") String category);

    @GET("goods/find")
    Call<GoodsPage> findGoods(@Query("query") String query,
                              @Query("category") String category);

    @Multipart
    @POST("goods")
    Call<Void> addGood(@Part("good-details") Good good,
                       @Part MultipartBody.Part mainGoodImage,
                       @Part List<MultipartBody.Part> supplementaryImages);

}
