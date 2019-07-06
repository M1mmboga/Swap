package com.example.swap.data.network.retrofit.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OffersService {

    @FormUrlEncoded
    @POST("offers")
    Call<Void> makeOffer(@Field("good_offered_for") int goodOfferedFor,
                         @Field("offered_goods[]")List<Integer> offeredGoods);
}
