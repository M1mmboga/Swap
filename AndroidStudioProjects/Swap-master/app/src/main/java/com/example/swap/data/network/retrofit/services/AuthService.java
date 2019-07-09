package com.example.swap.data.network.retrofit.services;

import com.example.swap.models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @FormUrlEncoded
    @POST("auth/google-sign-in")
    Call<User> sendGoogleSignInIdToken(@Field("idToken") String idToken);

//    @FormUrlEncoded
    @POST("auth/swap-sign-in")
    Call<User> doSwapLogin(@Query("email") String email,
                           @Query("password") String password);
}
