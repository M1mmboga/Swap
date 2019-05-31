package com.example.swap.datasources.goods.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.swap.datasources.goods.GoodsDataSource;
import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.models.Good;
import com.example.swap.models.GoodsPage;

import retrofit2.Call;

public class GoodsByCategoryDataSourceFactory extends DataSource.Factory<Integer, Good> {

    private MutableLiveData<GoodsDataSource> goodsDataSourceMutableLiveData
            = new MutableLiveData<>();
    private GoodsDataSource goodsDataSource;

    private String category;

    public GoodsByCategoryDataSourceFactory(String category) {
        super();
        this.category = category;
    }

    @NonNull
    @Override
    public DataSource<Integer, Good> create() {
        goodsDataSource = new GoodsDataSource(new ByCategoryGoodsFetcher(this.category));
        goodsDataSourceMutableLiveData.postValue(goodsDataSource);
        return goodsDataSource;
    }

    public MutableLiveData<GoodsDataSource> getGoodsDataSourceMutableLiveData() {
        return goodsDataSourceMutableLiveData;
    }

    class ByCategoryGoodsFetcher extends GoodsFetcher {

        private final String category;

        ByCategoryGoodsFetcher(String category) {
            this.category = category;
        }

        public Call<GoodsPage> fetch(int pageNumber) {
            return goodsDao.fetchGoodsByCategory(pageNumber, this.category);
        }
    }
}
