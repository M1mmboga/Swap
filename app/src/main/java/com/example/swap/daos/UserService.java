package com.example.swap.daos;

import com.example.swap.models.User;
import com.example.swap.models.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("users")
    public Call<UserRegistrationResponse> addUser(@Body User user);
}
