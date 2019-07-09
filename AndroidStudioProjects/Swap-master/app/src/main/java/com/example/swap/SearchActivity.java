package com.example.swap;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.swap.datasources.goods.goodfetchers.SoughtGoodsGoodsFetcher;
import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.viewmodels.GoodsFetcherViewModel;

public class SearchActivity extends AppCompatActivity {

    private GoodsFetcherViewModel goodsFetcherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        handleIntent(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            String category = null;
            Log.d("SearchActivity Query", query);

            Bundle searchData = intent.getBundleExtra(SearchManager.APP_DATA);
            if(searchData != null) {
                category = searchData.getString(ListActivity.CATEGORY_CRITERIA);
                Log.d("SearchActivity Category", category);
            }

            goodsFetcherViewModel = ViewModelProviders
                    .of(this)
                    .get(GoodsFetcherViewModel.class);
            goodsFetcherViewModel.getGoodsFetcherLiveData().setValue(new SoughtGoodsGoodsFetcher(query, category));

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            GoodsListFragment goodsListFragment = new GoodsListFragment();
            fragmentTransaction.add(R.id.activity_search_fragment_container, goodsListFragment);
            fragmentTransaction.commit();
        }
    }
}
