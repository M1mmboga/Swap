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

    public void addGood(Good good,
                        int userId,
                        MultipartBody.Part mainImage,
                        List<MultipartBody.Part> supplementaryImages,
                        Callback<Good> callback) {
        Call<Good> call = goodsService.addGood(
                preparePartFromString(good.getName()),
                preparePartFromString(good.getDescription()),
                preparePartFromString(good.getCategory()),
                preparePartFromString(Integer.toString(good.getPriceEstimate())),
                preparePartFromString(good.getLocation()),
                preparePartFromString(Integer.toString(userId)),
                mainImage, supplementaryImages
        );
        call.enqueue(callback);
    }

    private RequestBody preparePartFromString(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

}
