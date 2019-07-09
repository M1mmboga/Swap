package com.example.swap.views.viewoffers;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.swap.data.network.repository.OffersRepository;
import com.example.swap.models.Good;
import com.example.swap.models.Offer;
import com.example.swap.models.User;
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
    private MutableLiveData<User> acceptedBidder = new MutableLiveData<>();

    public OffersViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<OfferItem>> getOffers(OfferItem.OfferItemOnClickListener offerItemOnClickListener) {
        if(offers == null) {
            offers = new MutableLiveData<>();
            loadOffers(offerItemOnClickListener);
        }
        return offers;
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public void loadOffers(OfferItem.OfferItemOnClickListener offerItemOnClickListener) {
        networkState.setValue(NetworkState.LOADING);
        OffersRepository offersRepository = new OffersRepository();
        int userId = Auth.of(getApplication()).getCurrentUser().getId();
        offersRepository.getUserOffers(userId, new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                if(response.isSuccessful()) {
                    networkState.setValue(NetworkState.LOADED);
                    offers.setValue(convertToOfferItems(response.body(), offerItemOnClickListener));
                } else {
                    networkState.setValue(NetworkState.error(response.message(), () -> loadOffers(offerItemOnClickListener)));
                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {
                networkState.setValue(NetworkState.error(t.getMessage(), () -> loadOffers(offerItemOnClickListener)));
            }
        });
    }

    private List<OfferItem> convertToOfferItems(List<Offer> offers, OfferItem.OfferItemOnClickListener offerItemOnClickListener) {
        List<OfferItem> offerItems = new ArrayList<>();
        for (Offer offer : offers) {
            if(offer.getBidder() != null) {
                OfferItem offerItem = new OfferItem(
                        getApplication(),
                        offer.getWantedGood(),
                        offer.getOfferedGoods(),
                        offer.getBidder()
                );
                offerItem.setOfferItemOnClickListener(offerItemOnClickListener);
                offerItems.add(offerItem);
            }
            String TAG = "items check";
            Log.d(TAG, offer.getWantedGood().getName());
            for(Good good : offer.getOfferedGoods()) {
                Log.d(TAG, "------>" + good.getName());
            }
        }
        return offerItems;
    }

    public LiveData<User> getAcceptedBidder() {
        return acceptedBidder;
    }

    public void setAcceptedBidder(User acceptedBidder) {
        this.acceptedBidder.setValue(acceptedBidder);
    }
}
