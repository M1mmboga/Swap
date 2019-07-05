package com.example.swap.views.makeoffer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.swap.data.network.repository.GoodsRepository;
import com.example.swap.models.Good;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectGoodsViewModel extends AndroidViewModel {

    private MutableLiveData<List<ToSwapWithItem>> currentUserGoods;
    private MutableLiveData<NetworkState> networkState = new MutableLiveData<>();

    public SelectGoodsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ToSwapWithItem>> getCurrentUserGoods() {
        int currentUserId = Auth.of(getApplication()).getCurrentUser().getId();
        if (currentUserGoods == null) {
            currentUserGoods = new MutableLiveData<>();
            loadUserGoods(currentUserId);
        }

        return currentUserGoods;
    }

    private void loadUserGoods(int userId) {
        GoodsRepository goodsRepository = new GoodsRepository();
        networkState.setValue(NetworkState.LOADING);
        goodsRepository.getUserGoods(userId, new Callback<List<Good>>() {
            @Override
            public void onResponse(Call<List<Good>> call, Response<List<Good>> response) {
                if(response.isSuccessful()) {
                    networkState.setValue(NetworkState.LOADED);
                    List<ToSwapWithItem> toSwapWithItems = new ArrayList<>();
                    for(Good good : response.body()) {
                        toSwapWithItems.add(new ToSwapWithItem(
                                getApplication(), good.getName(), good.getImageFileName()));
                    }
                    currentUserGoods.setValue(toSwapWithItems);
                } else {
                    networkState.setValue(NetworkState.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<Good>> call, Throwable t) {
                networkState.setValue(NetworkState.error(t.getMessage()));
            }
        });
    }

    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }
}
