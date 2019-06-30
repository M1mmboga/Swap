package com.example.swap.data.network.repository;

import com.example.swap.data.network.retrofit.services.GoodsService;
import com.example.swap.models.Good;
import com.example.swap.utils.RetrofitFactory;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class GoodsRepository {
    private GoodsService goodsService;

    public GoodsRepository() {
        goodsService = RetrofitFactory.create().create(GoodsService.class);
    }

    public void addGood(RequestBody good,
                        MultipartBody.Part mainImage,
                        List<MultipartBody.Part> supplementaryImages,
                        Callback<Good> callback) {
        Call<Good> call = goodsService.addGood(good, mainImage, supplementaryImages);
        call.enqueue(callback);
    }

}
