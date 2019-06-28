package com.example.swap.data.network.repository;

import com.example.swap.data.network.retrofit.services.GoodsService;
import com.example.swap.models.Good;
import com.example.swap.utils.RetrofitFactory;

import java.util.List;

import okhttp3.MultipartBody;

public class GoodsRepository {
    private GoodsService goodsService;

    public GoodsRepository() {
        goodsService = RetrofitFactory.create().create(GoodsService.class);
    }

    public Good addGood(Good good, MultipartBody.Part mainImage, List<MultipartBody.Part> supplementaryImages) {
//        Call<Good> call = goodsService.addGood(good, mainImage, supplementaryImages)
        return null;
    }

}
