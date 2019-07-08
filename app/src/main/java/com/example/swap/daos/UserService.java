package com.example.swap.daos;

import com.example.swap.models.User;
import com.example.swap.models.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @POST("users")
    public Call<UserRegistrationResponse> addUser(@Body User user);

    @GET("myaccount/{id}")
    Call<User> getUser(@Path("id") int userId);
}
