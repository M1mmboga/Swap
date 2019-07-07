package com.example.swap.data.network.retrofit.services;

import com.example.swap.models.Offer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OffersService {

    @FormUrlEncoded
    @POST("offers")
    Call<Void> makeOffer(@Field("good_offered_for") int goodOfferedFor,
                         @Field("offered_goods[]")List<Integer> offeredGoods);

    @GET("users/{id}/offers")
    Call<List<Offer>> getUserOffers(@Path("id") int userId);
}
