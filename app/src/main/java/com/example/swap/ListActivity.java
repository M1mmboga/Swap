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

import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.rest.NetworkState;
import com.example.swap.viewmodels.GoodsByCategoryViewModel;
import com.example.swap.viewmodels.factories.GoodsByCategoryViewModelFactory;

public class ListActivity extends AppCompatActivity {

    private GoodsByCategoryViewModel goodsByCategoryViewModel;

    private ProgressBar progressBar;

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
        String category = i.getStringExtra("category");

        goodsByCategoryViewModel = ViewModelProviders
                .of(this, new GoodsByCategoryViewModelFactory(getApplication(), category))
                .get(GoodsByCategoryViewModel.class);
        goodsByCategoryViewModel.getLoadInitialNetworState()
                .observe(this, this::handleInitialLoading);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        GoodsListFragment goodsListFragment = new GoodsListFragment(
                /*goodsByCategoryViewModel.getGoodsPagedList(),
                goodsByCategoryViewModel.getLoadInitialNetworState(),
                goodsByCategoryViewModel.getLoadAfterNetworkState()*/
        );
        Bundle fragmentParams = new Bundle();
        fragmentParams.putString("Category", category);
        goodsListFragment.setArguments(fragmentParams);
        fragmentTransaction.add(R.id.list_fragment_container, goodsListFragment);
        fragmentTransaction.commit();
    }

    private void handleInitialLoading(NetworkState networkState) {
        if(networkState.getStatus() == NetworkState.Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
        Log.d("ListActivity initLoad", networkState.getStatus().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
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
