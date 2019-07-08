package com.example.swap.views.viewoffers;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;
import com.example.swap.utils.NetworkState;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

public class OffersActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    private FastItemAdapter<OfferItem> fastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.activity_offers_progressbar);

        OffersViewModel offersViewModel = ViewModelProviders
                .of(this).get(OffersViewModel.class);

        offersViewModel.getNetworkState().observe(
                this, this::handleNetworkState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.offers_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        fastItemAdapter = new FastItemAdapter<>();
        recyclerView.setAdapter(fastItemAdapter);
        offersViewModel.getOffers().observe(this, this::handleOffers);

    }

    private void handleNetworkState(NetworkState networkState) {
        View loadingFailedView = findViewById(R.id.loading_failed_view);

        if(networkState.getStatus() == NetworkState.Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
            loadingFailedView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);

            if(networkState.getStatus() == NetworkState.Status.FAILED) {
                loadingFailedView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void handleOffers(List<OfferItem> offers) {
        fastItemAdapter.add(offers);
    }

}
