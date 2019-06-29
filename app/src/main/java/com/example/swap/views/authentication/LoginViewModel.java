package com.example.swap.views.authentication;

import androidx.lifecycle.ViewModel;

import com.example.swap.data.network.repository.AuthRepository;
import com.example.swap.models.User;

import retrofit2.Callback;


public class LoginViewModel extends ViewModel {

    private AuthRepository authRepository = new AuthRepository();

    public void sendIdToken(String idToken, Callback<User> callback) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.sendIdToken(idToken, callback);
    }

    public void doSwapLogin(String email, String password, Callback<User> callback) {
        authRepository.doSwapLogin(email, password, callback);
    }
}
