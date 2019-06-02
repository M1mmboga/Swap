package com.example.swap.datasources.goods;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.swap.models.Good;
import com.example.swap.models.GoodsPage;
import com.example.swap.rest.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoodsDataSource extends PageKeyedDataSource<Integer, Good> {
    private static final int INITIAL_PAGE = 1;

    private MutableLiveData<NetworkState> loadInitialNetworkState =
            new MutableLiveData<>();
    private MutableLiveData<NetworkState> loadAfterNetworkState
            = new MutableLiveData<>();

    private GoodsFetcher goodsFetcher;

    public GoodsDataSource(GoodsFetcher goodsFetcher) {
        super();
        this.goodsFetcher = goodsFetcher;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, Good> callback) {

        loadInitialNetworkState.postValue(NetworkState.LOADING);

        Call<GoodsPage> call = goodsFetcher.fetch(INITIAL_PAGE);
        call.enqueue(new Callback<GoodsPage>() {
            @Override
            public void onResponse(Call<GoodsPage> call, Response<GoodsPage> response) {
                if(response.isSuccessful()) {
                    loadInitialNetworkState.postValue(NetworkState.LOADED);

                    GoodsPage fetchedGoodsPage = response.body();
                    callback.onResult(
                            fetchedGoodsPage.getGoods(),
                            fetchedGoodsPage.getFirstGoodPosition(),
                            fetchedGoodsPage.getTotal(),
                            fetchedGoodsPage.getPreviousPage(),
                            fetchedGoodsPage.getNextPage()
                    );
                    Log.d("GoodsDS loadInitial", "Fetch successful");
                    Log.d("GoodsDS loadInitial", response.raw().request().url().toString());
                } else {
                    loadInitialNetworkState.postValue(NetworkState.error(response.message(), () -> {
                        loadInitial(params, callback);
                    }));
                    Log.e("GoodsDS loadInitial", response.message());
                }
            }

            @Override
            public void onFailure(Call<GoodsPage> call, Throwable t) {
                loadInitialNetworkState.postValue(NetworkState.error(t.getMessage(), () -> {
                    loadInitial(params, callback);
                }));
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

        loadAfterNetworkState.postValue(NetworkState.LOADING);

        Call<GoodsPage> call = goodsFetcher.fetch(params.key);
        call.enqueue(new Callback<GoodsPage>() {
            @Override
            public void onResponse(Call<GoodsPage> call, Response<GoodsPage> response) {
                if(response.isSuccessful()) {
                    loadAfterNetworkState.postValue(NetworkState.LOADED);

                    GoodsPage fetchedGoodsPage = response.body();
                    callback.onResult(fetchedGoodsPage.getGoods(), fetchedGoodsPage.getNextPage());
                    Log.d("GoodsDS loadAfter", "Fetch successful");
                } else {

                    loadAfterNetworkState.postValue(NetworkState.error(response.message(), () -> {
                        loadAfter(params, callback);
                    }));
                    Log.e("GoodsDS loadAfter", response.message());
                }
            }

            @Override
            public void onFailure(Call<GoodsPage> call, Throwable t) {
                loadAfterNetworkState.postValue(NetworkState.error(t.getMessage(), () -> {
                    loadAfter(params, callback);
                }));

                Log.e("GoodsDS loadAfter", "Failed");
                t.printStackTrace();
            }
        });
    }

    public MutableLiveData<NetworkState> getLoadInitialNetworkState() {
        return loadInitialNetworkState;
    }

    public MutableLiveData<NetworkState> getLoadAfterNetworkState() {
        return loadAfterNetworkState;
    }
}
