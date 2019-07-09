package com.example.swap.views.makeoffer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.GoodDetailsActivity;
import com.example.swap.R;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;
import com.example.swap.views.postgood.PostGoodActivity;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

public class SelectGoodsToExchangeActivity extends AppCompatActivity {
    private static final int POST_GOOD_CODE = 1;

    private ProgressBar progressBar;
    private FastItemAdapter<ToSwapWithItem> fastAdapter;

    private SelectGoodsViewModel selectGoodsViewModel;
    private int goodWanted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_goods_to_exchange);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.swap_with));

        Intent intent = getIntent();
        goodWanted = intent.getIntExtra(GoodDetailsActivity.GOOD_ID, -1);

        progressBar = findViewById(R.id.activity_select_goods_progressbar);

        selectGoodsViewModel = ViewModelProviders
                .of(this).get(SelectGoodsViewModel.class);

        selectGoodsViewModel.getNetworkState().observe(this, this::handleNetworkState);
        selectGoodsViewModel.getOfferState().observe(this, this::handleOfferState);

        RecyclerView recyclerView = findViewById(R.id.select_items_to_exchange_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        fastAdapter = new FastItemAdapter<>();
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener((v, adapter, item, position) -> {
            item.setSelected(!item.isSelected());
            ((MaterialCardView)v).setChecked(item.isSelected());
            return false;
        });

        recyclerView.setAdapter(fastAdapter);

        selectGoodsViewModel.getCurrentUserGoods()
                .observe(this, this::handleCurrentUserGoods);

        (findViewById(R.id.select_items_to_exchange_post_good_btn)).setOnClickListener(v -> {
            startActivityForResult(
                    new Intent(this, PostGoodActivity.class), POST_GOOD_CODE);
        });
    }

    private void handleNetworkState(NetworkState networkState) {
        Log.d("Network State", networkState.getStatus().toString());
        if(networkState.getStatus() == NetworkState.Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void handleOfferState(NetworkState networkState) {
        if(networkState == NetworkState.LOADING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            if(networkState.getStatus() == NetworkState.Status.FAILED){
                showToast(networkState.getMessage());
            } else {
                showToast("Offer made");
            }
        }
    }

    private void handleCurrentUserGoods(List<ToSwapWithItem> items) {
        View v = findViewById(R.id.select_items_to_exchange_no_items_layout);

        if(items.size() < 1) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
            fastAdapter.add(items);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_items_to_exchange, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.select_items_to_exchange_make_offer) {
            selectGoodsViewModel.makeOffer(goodWanted);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == POST_GOOD_CODE) {
            selectGoodsViewModel.loadUserGoods(
                    Auth.of(getApplication()).getCurrentUser().getId()
            );
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
