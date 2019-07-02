package com.example.swap.data.network.repository;

import com.example.swap.data.network.retrofit.services.AuthService;
import com.example.swap.models.User;
import com.example.swap.utils.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthRepository {
    private AuthService authService;

    public AuthRepository() {
        this.authService = RetrofitFactory.create().create(AuthService.class);
    }

    public void sendIdToken(String idToken, Callback<User> callback) {
        Call<User> call = authService.sendGoogleSignInIdToken(idToken);
        call.enqueue(callback);
    }

    public void doSwapLogin(String email, String password, Callback<User> callback) {
        Call<User> call = authService.doSwapLogin(email, password);
        call.enqueue(callback);
    }
}
