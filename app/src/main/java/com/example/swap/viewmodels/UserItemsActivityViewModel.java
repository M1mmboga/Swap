package com.example.swap.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swap.data.network.repository.UsersRepository;
import com.example.swap.models.Good;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserItemsActivityViewModel extends AndroidViewModel {
    MutableLiveData<List<Good>> goods;
    MutableLiveData<NetworkState> networkState =
            new MutableLiveData<>();

    public UserItemsActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Good>> getGoods(){
        if (goods == null){
            goods = new MutableLiveData<>();
            loadGoods();
        }

        return goods;
    }

    public void loadGoods(){
        int userId = Auth.of(getApplication()).getCurrentUser().getId();
        UsersRepository usersRepository = new UsersRepository();
        usersRepository.fetchGoodsByUserId(userId, new Callback<List<Good>>() {
            @Override
            public void onResponse(Call<List<Good>> call, Response<List<Good>> response) {

                if (response.isSuccessful()){
                    networkState.setValue(NetworkState.LOADED);
                    goods.setValue(response.body());
                }else{
                    networkState.setValue(NetworkState.error(response.message(), () -> loadGoods()));
                }

            }

            @Override
            public void onFailure(Call<List<Good>> call, Throwable t) {

            }
        });
    }
}
