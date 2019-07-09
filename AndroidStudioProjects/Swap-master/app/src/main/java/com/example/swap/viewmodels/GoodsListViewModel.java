package com.example.swap.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.swap.datasources.goods.GoodsDataSource;
import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.datasources.goods.factories.GoodsDataSourceFactory;
import com.example.swap.models.Good;
import com.example.swap.utils.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoodsListViewModel extends ViewModel {
    private LiveData<PagedList<Good>> goodsPagedList;
    private LiveData<NetworkState> loadInitialNetworkState;
    private LiveData<NetworkState> loadAfterNetworkState;

    public GoodsListViewModel(GoodsFetcher goodsFetcher) {
        GoodsDataSourceFactory goodsDataSourceFactory =
                new GoodsDataSourceFactory(goodsFetcher);

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


        loadInitialNetworkState = Transformations.switchMap(
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

    public LiveData<NetworkState> getLoadInitialNetworkState() {
        return loadInitialNetworkState;
    }

    public LiveData<NetworkState> getLoadAfterNetworkState() {
        return loadAfterNetworkState;
    }
}
