package com.example.swap;

import android.os.Bundle;

import com.example.swap.datasources.goods.goodfetchers.ByCategoryGoodsFetcher;
import com.example.swap.datasources.goods.goodfetchers.UserGoodsFetcher;
import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;
import com.example.swap.viewmodels.GoodsFetcherViewModel;
import com.example.swap.viewmodels.GoodsListViewModel;
import com.example.swap.viewmodels.factories.GoodsListViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class UserGoodsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_goods);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int userId = Auth.of(getApplication()).getCurrentUser().getId();
        Log.e("THE USER ID", "" + userId);

        GoodsFetcherViewModel goodsFetcherViewModel = ViewModelProviders
                .of(this)
                .get(GoodsFetcherViewModel.class);
        goodsFetcherViewModel.getGoodsFetcherLiveData().setValue(new UserGoodsFetcher(userId));

        GoodsListViewModel goodsListViewModel = ViewModelProviders
                .of(this, new GoodsListViewModelFactory(goodsFetcherViewModel.getGoodsFetcherLiveData().getValue()))
                .get(GoodsListViewModel.class);
        goodsListViewModel.getLoadInitialNetworkState()
                .observe(this, this::handleInitialLoading);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        GoodsListFragment goodsListFragment = new GoodsListFragment();
        Bundle fragmentParams = new Bundle();
//        fragmentParams.putString(CATEGORY_CRITERIA, category);
        goodsListFragment.setArguments(fragmentParams);
        fragmentTransaction.add(R.id.user_goods_list_container, goodsListFragment);
        fragmentTransaction.commit();
    }

    private void handleInitialLoading(NetworkState networkState) {
        if(networkState.getStatus() == NetworkState.Status.RUNNING) {
            showToast("Running");
        } else {
            if(networkState.getStatus() == NetworkState.Status.SUCCESS) {
                showToast("Finished");
            } else {
                showToast("Failed");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
