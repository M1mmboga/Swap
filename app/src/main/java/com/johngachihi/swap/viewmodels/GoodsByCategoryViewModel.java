package com.johngachihi.swap.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.johngachihi.swap.datasources.factories.GoodsDataSourceFactory;
import com.johngachihi.swap.models.Good;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GoodsByCategoryViewModel extends ViewModel {
    private LiveData<PagedList<Good>> goodsPagedList;
    private MutableLiveData<String> categoryLiveData = new MutableLiveData<>();

    public GoodsByCategoryViewModel() {
        GoodsDataSourceFactory goodsDataSourceFactory = new GoodsDataSourceFactory();

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

    public MutableLiveData<String> getCategoryLiveData() {
        return categoryLiveData;
    }

    public LiveData<PagedList<Good>> getGoodsPagedList() {
        return goodsPagedList;
    }

    public void setCategory(String category) {
        if(categoryLiveData.getValue() != null &&
                !categoryLiveData.getValue().equals(category))
        {
            categoryLiveData.setValue(category);
        }
    }
}
