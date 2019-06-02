package com.example.swap.viewmodels.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.swap.viewmodels.SoughtGoodsViewModel;

public class SoughtGoodsViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String searchQuery;
    private String category;

    public SoughtGoodsViewModelFactory(Application application, String searchQuery, String category) {
        this.mApplication = application;
        this.searchQuery = searchQuery;
        this.category = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new SoughtGoodsViewModel(mApplication, searchQuery, category);
    }
}
