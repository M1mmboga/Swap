package com.example.swap.views.makeoffer;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;
import com.example.swap.utils.NetworkState;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

public class SelectGoodsToExchangeActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private SelectGoodsViewModel selectGoodsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_goods_to_exchange);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.swap_with));

        progressBar = findViewById(R.id.activity_select_goods_progressbar);

        selectGoodsViewModel = ViewModelProviders
                .of(this).get(SelectGoodsViewModel.class);

        selectGoodsViewModel.getNetworkState().observe(this, this::handleNetworkState);

        RecyclerView recyclerView = findViewById(R.id.select_items_to_exchange_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        FastItemAdapter<ToSwapWithItem> fastAdapter = new FastItemAdapter<>();
        fastAdapter.withSelectable(true);
        fastAdapter.withMultiSelect(true);
        fastAdapter.withSelectOnLongClick(true);
        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
            item.setSelected(!item.isSelected());
            ((MaterialCardView)v).setChecked(item.isSelected());
            Toast.makeText(SelectGoodsToExchangeActivity.this,
                    item.isSelected() ? "Is selected" : "Is not selected",
                    Toast.LENGTH_SHORT).show();
            return false;
        });

        recyclerView.setAdapter(fastAdapter);

        selectGoodsViewModel.getCurrentUserGoods().observe(this, fastAdapter::add);

    }

    private void handleNetworkState(NetworkState networkState) {
        Log.d("Network State", networkState.getStatus().toString());
        if(networkState.getStatus() == NetworkState.Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_items_to_exchange, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
