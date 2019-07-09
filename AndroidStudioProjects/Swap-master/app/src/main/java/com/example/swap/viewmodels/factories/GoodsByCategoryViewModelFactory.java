package com.example.swap.viewmodels.factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.swap.viewmodels.GoodsByCategoryViewModel;

public class GoodsByCategoryViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String category;

    public GoodsByCategoryViewModelFactory(Application application, String category) {
        this.mApplication = application;
        this.category = category;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T)new GoodsByCategoryViewModel(mApplication, category);
    }
}
