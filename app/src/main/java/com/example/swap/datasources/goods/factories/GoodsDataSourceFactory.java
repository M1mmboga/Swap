package com.example.swap.datasources.goods.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.swap.datasources.goods.GoodsDataSource;
import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.models.Good;

public class GoodsDataSourceFactory extends DataSource.Factory<Integer, Good> {

    private MutableLiveData<GoodsDataSource> goodsDataSourceMutableLiveData
            = new MutableLiveData<>();
    private GoodsDataSource goodsDataSource;

    private GoodsFetcher goodsFetcher;

    public GoodsDataSourceFactory(GoodsFetcher goodsFetcher) {
        super();
        this.goodsFetcher = goodsFetcher;
    }

    @NonNull
    @Override
    public DataSource<Integer, Good> create() {
        goodsDataSource = new GoodsDataSource(goodsFetcher);
        goodsDataSourceMutableLiveData.postValue(goodsDataSource);
        return goodsDataSource;
    }

    public MutableLiveData<GoodsDataSource> getGoodsDataSourceMutableLiveData() {
        return goodsDataSourceMutableLiveData;
    }

}
