package com.example.swap.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.swap.datasources.goods.GoodsFetcher;

public class GoodsFetcherViewModel extends ViewModel {
    private MutableLiveData<GoodsFetcher> goodsFetcherLiveData =
            new MutableLiveData<>();

    public MutableLiveData<GoodsFetcher> getGoodsFetcherLiveData() {
        return goodsFetcherLiveData;
    }
}
