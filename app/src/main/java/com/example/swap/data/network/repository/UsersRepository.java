package com.example.swap.data.network.repository;

import android.util.Log;

import com.example.swap.daos.UserService;
import com.example.swap.models.Good;
import com.example.swap.models.SimpleRestResponse;
import com.example.swap.utils.RetrofitFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class UsersRepository {
    private static final String TAG = "UserRepository";

    private UserService userService;

    public UsersRepository() {
        userService = RetrofitFactory.create().create(UserService.class);
    }

    public void fetchGoodsByUserId(int userId,  Callback<List<Good>> callback){
        Call<List<Good>> good = userService.fetchGoodsByUserId(userId);
        good.enqueue(callback);

    }

    public void putFCMInstanceIdForUser(int userId, String fcmInstanceId) {
        Call<SimpleRestResponse> call = userService.putFCMInstanceId(userId, fcmInstanceId);
        call.enqueue( new Callback<SimpleRestResponse>() {
            @Override
            public void onResponse(Call<SimpleRestResponse> call, Response<SimpleRestResponse> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "putFCMInstanceIdForUser" + response.message());
                }
            }

            @Override
            public void onFailure(Call<SimpleRestResponse> call, Throwable t) {
                Log.e(TAG, "putFCMInstanceIdForUser" + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    public void removeFCMInstanceIdForUser(int userId) {
        Call<SimpleRestResponse> call = userService.removeFCMInstanceId(userId);
        call.enqueue(new Callback<SimpleRestResponse>() {
            @Override
            public void onResponse(Call<SimpleRestResponse> call, Response<SimpleRestResponse> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "removeFCMInstanceIdForUser" + response.message());
                }
            }

            @Override
            public void onFailure(Call<SimpleRestResponse> call, Throwable t) {
                Log.e(TAG, "removeFCMInstanceIdForUser" + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
