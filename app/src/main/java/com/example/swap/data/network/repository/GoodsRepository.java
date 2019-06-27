package com.example.swap.data.network.repository;

import android.content.Context;
import android.net.Uri;

import com.example.swap.data.network.retrofit.services.GoodsService;
import com.example.swap.models.Good;
import com.example.swap.utils.RetrofitFactory;

import java.io.File;
import java.net.ContentHandler;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class GoodsRepository {
    private GoodsService goodsService;

    public GoodsRepository() {
        goodsService = RetrofitFactory.create().create(GoodsService.class);
    }

    public Good addGood(Good good, File mainImage, List<File> supplementaryImages) {
        Call<Good> call = goodsService.addGood(good, mainImage, supplementaryImages)
    }

}
