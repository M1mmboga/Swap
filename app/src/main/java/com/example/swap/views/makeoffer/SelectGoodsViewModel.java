package com.example.swap.views.makeoffer;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.swap.R;
import com.example.swap.data.network.repository.GoodsRepository;
import com.example.swap.data.network.repository.OffersRepository;
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
    private MutableLiveData<NetworkState> offerState = new MutableLiveData<>();

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

    void loadUserGoods(int userId) {
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
                                getApplication(), good));
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

    public MutableLiveData<NetworkState> getOfferState() {
        return offerState;
    }

    public void makeOffer(int goodWanted){
        List<Integer> selectedItems = getSelectedItems();
        if(selectedItems != null && selectedItems.size() > 0) {
            offerState.setValue(NetworkState.LOADING);
            new OffersRepository().makeOffer(goodWanted, selectedItems, callback());
        } else {
            Toast.makeText(getApplication(),
                    R.string.no_items_selected, Toast.LENGTH_SHORT).show();
        }
    }

    private List<Integer> getSelectedItems() {
        if (currentUserGoods != null &&
                currentUserGoods.getValue() != null &&
                currentUserGoods.getValue().size() > 0)
        {
            List<Integer> selectedItems = new ArrayList<>();
            List<ToSwapWithItem> allItems = currentUserGoods.getValue();
            for (ToSwapWithItem item : allItems) {
                if(item.isSelected()) {
                    selectedItems.add(item.getGood().getId());
                }
            }
            return selectedItems;
        }
        return null;
    }

    private Callback<Void> callback() {
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    offerState.setValue(NetworkState.LOADED);
                } else {
                    offerState.setValue(NetworkState.error(response.message()));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                offerState.setValue(NetworkState.error(t.getMessage()));
            }
        };
    }
}
