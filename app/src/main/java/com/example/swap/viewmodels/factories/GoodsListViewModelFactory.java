package com.example.swap.viewmodels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.viewmodels.GoodsListViewModel;

public class GoodsListViewModelFactory implements ViewModelProvider.Factory {

    private GoodsFetcher goodsFetcher;

    public GoodsListViewModelFactory(GoodsFetcher goodsFetcher) {
        this.goodsFetcher = goodsFetcher;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new GoodsListViewModel(this.goodsFetcher);
    }
}
