package com.example.swap.daos;

import com.example.swap.models.Good;
import com.example.swap.models.SimpleRestResponse;
import com.example.swap.models.User;
import com.example.swap.models.UserRegistrationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("users")
    Call<UserRegistrationResponse> addUser(@Body User user);

    @GET("users/{userId}/goods")
    Call<List<Good>> fetchGoodsByUserId(@Path("userId") int userId);

    @FormUrlEncoded
    @PUT("users/fcm-instance-id")
    Call<SimpleRestResponse> putFCMInstanceId(@Field("user_id") int userId,
                                              @Field("fcm_instance_id") String fcmInstanceId);

    @DELETE("users/{user_id}/fcm-instance-id")
    Call<SimpleRestResponse> removeFCMInstanceId(@Path("user_id") int userId);
}
