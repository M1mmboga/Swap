package com.example.swap.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.swap.datasources.goods.GoodsDataSource;
import com.example.swap.datasources.goods.factories.GoodsByCategoryDataSourceFactory;
import com.example.swap.models.Good;
import com.example.swap.rest.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoodsByCategoryViewModel extends ViewModel {
    private LiveData<PagedList<Good>> goodsPagedList;
    private LiveData<NetworkState> loadInitialNetworState;
    private LiveData<NetworkState> loadAfterNetworkState;

    public GoodsByCategoryViewModel(Application application, String category) {

        GoodsByCategoryDataSourceFactory goodsDataSourceFactory =
                new GoodsByCategoryDataSourceFactory(category);

        Executor executor = Executors.newFixedThreadPool(4);

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(30)
                .setEnablePlaceholders(false)
                .setPageSize(20)
                .setPrefetchDistance(20)
                .build();

        goodsPagedList = (new LivePagedListBuilder<>(goodsDataSourceFactory, config))
                .setFetchExecutor(executor)
                .build();


        loadInitialNetworState = Transformations.switchMap(
                goodsDataSourceFactory.getGoodsDataSourceMutableLiveData(),
                GoodsDataSource::getLoadInitialNetworkState
        );

        loadAfterNetworkState = Transformations.switchMap(
                goodsDataSourceFactory.getGoodsDataSourceMutableLiveData(),
                GoodsDataSource::getLoadAfterNetworkState
        );

    }

    public LiveData<PagedList<Good>> getGoodsPagedList() {
        return goodsPagedList;
    }

    public LiveData<NetworkState> getLoadInitialNetworState() {
        return loadInitialNetworState;
    }

    public LiveData<NetworkState> getLoadAfterNetworkState() {
        return loadAfterNetworkState;
    }
}
