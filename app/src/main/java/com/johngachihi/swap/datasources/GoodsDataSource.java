package com.johngachihi.swap.datasources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.johngachihi.swap.daos.GoodsDao;
import com.johngachihi.swap.models.Good;
import com.johngachihi.swap.models.GoodsPage;
import com.johngachihi.swap.rest.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoodsDataSource extends PageKeyedDataSource<Integer, Good> {
    private static final int INITIAL_PAGE = 1;

    Retrofit retrofit;

    public GoodsDataSource() {
        super();
        retrofit = RetrofitFactory.create();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Good> callback) {

        GoodsDao goodsDao = retrofit.create(GoodsDao.class);
        Call<GoodsPage> call = goodsDao.fetchGoods(INITIAL_PAGE);
        call.enqueue(new Callback<GoodsPage>() {
            @Override
            public void onResponse(Call<GoodsPage> call, Response<GoodsPage> response) {
                if(response.isSuccessful()) {
                    GoodsPage fetchedGoodsPage = response.body();
                    callback.onResult(
                            fetchedGoodsPage.getGoods(),
                            fetchedGoodsPage.getFirstGoodPosition(),
                            fetchedGoodsPage.getTotal(),
                            fetchedGoodsPage.getPreviousPage(),
                            fetchedGoodsPage.getNextPage()
                    );
                    Log.d("GoodsDS loadInitial", "Fetch successful");
                } else {

                    Log.e("GoodsDS loadInitial", response.message());
                }
            }

            @Override
            public void onFailure(Call<GoodsPage> call, Throwable t) {
                Log.e("GoodsDS loadInitial", "Failed");
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, Good> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<Integer, Good> callback) {
        GoodsDao goodsDao = retrofit.create(GoodsDao.class);
        Call<GoodsPage> call = goodsDao.fetchGoods(params.key);
        call.enqueue(new Callback<GoodsPage>() {
            @Override
            public void onResponse(Call<GoodsPage> call, Response<GoodsPage> response) {
                if(response.isSuccessful()) {
                    GoodsPage fetchedGoodsPage = response.body();
                    callback.onResult(fetchedGoodsPage.getGoods(), fetchedGoodsPage.getNextPage());
                    Log.d("GoodsDS loadAfter", "Fetch successful");
                } else {

                    Log.e("GoodsDS loadAfter", response.message());
                }
            }

            @Override
            public void onFailure(Call<GoodsPage> call, Throwable t) {
                Log.e("GoodsDS loadAfter", "Failed");
                t.printStackTrace();
            }
        });
    }
}
