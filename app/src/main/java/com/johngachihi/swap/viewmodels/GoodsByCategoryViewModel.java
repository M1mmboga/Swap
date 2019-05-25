package com.johngachihi.swap.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.johngachihi.swap.datasources.goods.factories.GoodsByCategoryDataSourceFactory;
import com.johngachihi.swap.models.Good;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoodsByCategoryViewModel extends ViewModel {
    private LiveData<PagedList<Good>> goodsPagedList;

    public GoodsByCategoryViewModel(Application application, String category) {
        GoodsByCategoryDataSourceFactory goodsDataSourceFactory =
                new GoodsByCategoryDataSourceFactory(category);

        Executor executor = Executors.newFixedThreadPool(4);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(30)
                .setEnablePlaceholders(true)
                .setPageSize(20)
                .setPrefetchDistance(20)
                .build();

        goodsPagedList = (new LivePagedListBuilder<>(goodsDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Good>> getGoodsPagedList() {
        return goodsPagedList;
    }

}
