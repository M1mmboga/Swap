package com.example.swap.views.viewoffers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.swap.data.network.repository.OffersRepository;
import com.example.swap.models.Offer;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersViewModel extends AndroidViewModel {
    MutableLiveData<List<OfferItem>> offers;
    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    public OffersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<OfferItem>> getOffers() {
        if(offers == null) {
            offers = new MutableLiveData<>();
            loadOffers();
        }
        return offers;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    private void loadOffers() {
        networkState.setValue(NetworkState.LOADING);
        OffersRepository offersRepository = new OffersRepository();
        int userId = Auth.of(getApplication()).getCurrentUser().getId();
        offersRepository.getUserOffers(userId, new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                if(response.isSuccessful()) {
                    networkState.setValue(NetworkState.LOADED);
                    offers.setValue(convertToOfferItems(response.body()));
                } else {
                    networkState.setValue(NetworkState.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {
                networkState.setValue(NetworkState.error(t.getMessage()));
            }
        });
    }

    private List<OfferItem> convertToOfferItems(List<Offer> offers) {
        List<OfferItem> offerItems = new ArrayList<>();
        for (Offer offer : offers) {
            if(offer.getBidder() != null) {
                offerItems.add(new OfferItem(
                        getApplication(),
                        offer.getWantedGood(),
                        offer.getOfferedGoods(),
                        offer.getBidder()
                ));
            }
        }
        return offerItems;
    }
}
