package com.example.swap.data.network.retrofit.services;

import com.example.swap.models.Good;
import com.example.swap.models.GoodsPage;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GoodsService {

    @GET("goods/paged")
    Call<GoodsPage> fetchGoodsByCategory(@Query("page") int page,
                                         @Query("category") String category);

    @GET("users/{userId}/goods")
    Call<List<Good>> fetchGoodsByUserId(@Path("userId") int userId);

    @GET("goods/find")
    Call<GoodsPage> findGoods(@Query("query") String query,
                              @Query("category") String category);

    /*@Multipart
    @POST("goods")
    Call<Good> addGood(@Part("good_details") RequestBody good,
                       @Part MultipartBody.Part mainGoodImage,
                       @Part List<MultipartBody.Part> supplementaryImages);*/

    @Multipart
    @POST("goods")
    Call<Good> addGood(@Part("name") RequestBody goodName,
                       @Part("description") RequestBody goodDescription,
                       @Part("category") RequestBody goodCategory,
                       @Part("price_estimate") RequestBody goodPriceEstimate,
                       @Part("location") RequestBody goodLocation,
                       @Part("user_id") RequestBody user,
                       @Part MultipartBody.Part mainGoodImage,
                       @Part List<MultipartBody.Part> supplementaryImages);
}
