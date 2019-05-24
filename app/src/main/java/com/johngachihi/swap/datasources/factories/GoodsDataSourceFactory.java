package com.johngachihi.swap.datasources.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.johngachihi.swap.datasources.GoodsDataSource;
import com.johngachihi.swap.models.Good;

public class GoodsDataSourceFactory extends DataSource.Factory<Integer, Good> {

    private MutableLiveData<GoodsDataSource> goodsDataSourceMutableLiveData
            = new MutableLiveData<>();
    private GoodsDataSource goodsDataSource;

    @NonNull
    @Override
    public DataSource<Integer, Good> create() {
        goodsDataSource = new GoodsDataSource();
        goodsDataSourceMutableLiveData.postValue(goodsDataSource);
        return goodsDataSource;
    }

    public MutableLiveData<GoodsDataSource> getGoodsDataSourceMutableLiveData() {
        return goodsDataSourceMutableLiveData;
    }
}
