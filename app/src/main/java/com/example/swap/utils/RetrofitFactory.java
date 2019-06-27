package com.example.swap.utils;

import com.example.swap.utils.addressconstants.Addresses;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static final String HOME_IP = "192.168.1.141";
    private static final String BASE_URL = "http://" + HOME_IP + "/swapapi/public/index.php/";

    private static Retrofit retrofit;

    public static Retrofit create() {
        if(retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            retrofit = retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(Addresses.API_HOME_URL)
                    .build();
        }
        return retrofit;
    }
}
