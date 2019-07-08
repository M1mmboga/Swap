package com.example.swap.data.network.repository;

import com.example.swap.data.network.retrofit.services.OffersService;
import com.example.swap.models.Offer;
import com.example.swap.utils.RetrofitFactory;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class OffersRepository {
    private OffersService offersService;

    public OffersRepository() {
        offersService = RetrofitFactory.create().create(OffersService.class);
    }

    public void makeOffer(int goodOfferedFor,
                          List<Integer> offeredGoods,
                          Callback<Void> callback) {
        Call<Void> call = offersService.makeOffer(goodOfferedFor, offeredGoods);
        call.enqueue(callback);
    }

    public void getUserOffers(int userId, Callback<List<Offer>> callback) {
        Call<List<Offer>> call = offersService.getUserOffers(userId);
        call.enqueue(callback);
    }
}
