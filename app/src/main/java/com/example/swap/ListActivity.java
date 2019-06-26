package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.swap.datasources.goods.goodfetchers.ByCategoryGoodsFetcher;
import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.rest.NetworkState;
import com.example.swap.viewmodels.GoodsByCategoryViewModel;
import com.example.swap.viewmodels.GoodsFetcherViewModel;
import com.example.swap.viewmodels.GoodsListViewModel;
import com.example.swap.viewmodels.factories.GoodsListViewModelFactory;

public class ListActivity extends AppCompatActivity {
    public static final String CATEGORY_CRITERIA = "Category";

    private GoodsByCategoryViewModel goodsByCategoryViewModel;

    private ProgressBar progressBar;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_list_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setDefaultKeyMode(DEFAULT_KEYS_SEARCH_LOCAL);

        progressBar = (ProgressBar) findViewById(R.id.activity_list_progressbar);

        Intent i = getIntent();
        category = i.getStringExtra("category");

        setTitle(category);

        GoodsFetcherViewModel goodsFetcherViewModel = ViewModelProviders
                .of(this)
                .get(GoodsFetcherViewModel.class);
        goodsFetcherViewModel.getGoodsFetcherLiveData().setValue(new ByCategoryGoodsFetcher(category));

        GoodsListViewModel goodsListViewModel = ViewModelProviders
                .of(this, new GoodsListViewModelFactory(goodsFetcherViewModel.getGoodsFetcherLiveData().getValue()))
                .get(GoodsListViewModel.class);
        goodsListViewModel.getLoadInitialNetworkState()
                .observe(this, this::handleInitialLoading);

        /*goodsByCategoryViewModel = ViewModelProviders
                .of(this, new GoodsByCategoryViewModelFactory(getApplication(), category))
                .get(GoodsByCategoryViewModel.class);
        goodsByCategoryViewModel.getLoadInitialNetworkState()
                .observe(this, this::handleInitialLoading);*/

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        GoodsListFragment goodsListFragment = new GoodsListFragment();
        Bundle fragmentParams = new Bundle();
        fragmentParams.putString(CATEGORY_CRITERIA, category);
        goodsListFragment.setArguments(fragmentParams);
        fragmentTransaction.add(R.id.list_fragment_container, goodsListFragment);
        fragmentTransaction.commit();
    }

    private void handleInitialLoading(NetworkState networkState) {
        Log.d("ListActivity", networkState.getStatus().toString());
        if(networkState.getStatus() == NetworkState.Status.RUNNING) {
            Log.d("ListActivity", "Showing progress indicator");
            progressBar.setVisibility(View.VISIBLE);
        } else {
            Log.d("ListActivity", "Showing progress hiding");
            progressBar.setVisibility(View.INVISIBLE);
        }
        Log.d("ListActivity initLoad", networkState.getStatus().toString());
    }

    @Override
    public boolean onSearchRequested() {
        Bundle searchData = new Bundle();
        Log.d("onSearchRequested", category);
        searchData.putString(CATEGORY_CRITERIA, category);
        startSearch(null, false, searchData, false);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_item).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_menu_item:
                onSearchRequested();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
