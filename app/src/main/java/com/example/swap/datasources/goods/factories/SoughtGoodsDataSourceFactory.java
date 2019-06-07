package com.example.swap.datasources.goods.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.swap.datasources.goods.GoodsDataSource;
import com.example.swap.datasources.goods.GoodsFetcher;
import com.example.swap.models.Good;
import com.example.swap.models.GoodsPage;

import retrofit2.Call;

public class SoughtGoodsDataSourceFactory extends DataSource.Factory<Integer, Good> {
    private MutableLiveData<GoodsDataSource> goodsDataSourceMutableLiveData
            = new MutableLiveData<>();
    private GoodsDataSource goodsDataSource;

    private String searchQuery;
    private String category;

    public SoughtGoodsDataSourceFactory(String searchQuery, String category) {
        super();
        this.searchQuery = searchQuery;
        this.category = category;
    }

    @NonNull
    @Override
    public DataSource<Integer, Good> create() {
        goodsDataSource = new GoodsDataSource(new SoughtGoodsFetcher(this.searchQuery, this.category));
        goodsDataSourceMutableLiveData.postValue(goodsDataSource);
        return goodsDataSource;
    }

    public MutableLiveData<GoodsDataSource> getGoodsDataSourceMutableLiveData() {
        return goodsDataSourceMutableLiveData;
    }

    class SoughtGoodsFetcher extends GoodsFetcher {
        private String searchQuery;
        private  String category;

        public SoughtGoodsFetcher(String searchQuery, String category) {
            this.searchQuery = searchQuery;
            this.category = category;
        }

        @Override
        protected Call<GoodsPage> fetch(int pageNumber) {
            return goodsDao.findGoods(this.searchQuery, this.category);
        }
    }
}
