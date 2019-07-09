package com.example.swap.views.viewoffers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.GoodDetailsActivity;
import com.example.swap.R;
import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.models.Good;
import com.example.swap.models.User;
import com.example.swap.utils.NetworkState;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import java.util.List;

public class OffersActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CALL_PHONE = 1;

    private ProgressBar progressBar;
    private MaterialButton retryButton;

    private LinearLayout bottomSheetLayout;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private OffersViewModel offersViewModel;

    private FastItemAdapter<OfferItem> fastItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.activity_offers_progressbar);

        retryButton = findViewById(R.id.addon_loading_failed_retry_btn);

        offersViewModel = ViewModelProviders
                .of(this).get(OffersViewModel.class);

        offersViewModel.getNetworkState().observe(
                this, this::handleNetworkState);

        offersViewModel.getAcceptedBidder().observe(
                this, this::handleAcceptedBidder);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.offers_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        fastItemAdapter = new FastItemAdapter<>();
        fastItemAdapter.withSelectable(false);
        recyclerView.setAdapter(fastItemAdapter);
        offersViewModel.getOffers(onClickAcceptListener()).observe(this, this::handleOffers);

        bottomSheetLayout = findViewById(R.id.bottom_sheet_accept_offer);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    private void handleNetworkState(NetworkState networkState) {
        View loadingFailedView = findViewById(R.id.loading_failed_view);

        if (networkState.getStatus() == NetworkState.Status.RUNNING) {
            progressBar.setVisibility(View.VISIBLE);
            loadingFailedView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);

            if (networkState.getStatus() == NetworkState.Status.FAILED) {
                loadingFailedView.setVisibility(View.VISIBLE);
                Button button = loadingFailedView.findViewById(R.id.addon_loading_failed_retry_btn);
                button.setOnClickListener(v -> networkState.getRetryable().retry());
                Log.e("OffersActivity", networkState.getMessage());
            }
        }
    }

    private void handleOffers(List<OfferItem> offers) {
        View noOffersView = findViewById(R.id.addon_offers_no_offers);
        if (offers.size() < 1) {
            noOffersView.setVisibility(View.VISIBLE);
        } else {
            noOffersView.setVisibility(View.GONE);
            fastItemAdapter.add(offers);
        }
    }

    private void handleAcceptedBidder(User user) {

    }

    private OfferItem.OfferItemOnClickListener onClickAcceptListener() {
        return new OfferItem.OfferItemOnClickListener() {
            @Override
            public void onClickAccept(OfferItem offerItem) {
                offersViewModel.setAcceptedBidder(offerItem.getBidder());
                Toast.makeText(OffersActivity.this, offerItem.getBidder().getEmail(), Toast.LENGTH_SHORT).show();
                ((TextView) findViewById(R.id.bottom_sheet_accept_offer_bidder_name))
                        .setText(offerItem.getBidder().getFirstname() + " " + offerItem.getBidder().getLastname());

                String telephoneNumber = "";
                if (offerItem.getBidder().getPhonenumber() != null) {
                    telephoneNumber = offerItem.getBidder().getPhonenumber();
                } else {
                    telephoneNumber = "Phone number N/A";
                }
                ((TextView) findViewById(R.id.bottom_sheet_accept_offer_bidder_phone_number))
                        .setText(telephoneNumber);
                findViewById(R.id.bottom_sheet_accept_offer_call_fab)
                        .setOnClickListener(v -> {
                            if (ContextCompat.checkSelfPermission(
                                    OffersActivity.this, Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED)
                            {
                                ActivityCompat.requestPermissions(
                                        OffersActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        REQUEST_PERMISSION_CALL_PHONE
                                );

                                return;
                            }
                            makeCall(offerItem.getBidder().getPhonenumber());
                        });
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onClickOfferedGoodItem(Good good) {
                Intent toGoodDetails = new Intent(OffersActivity.this, GoodDetailsActivity.class);
                toGoodDetails.putExtra(GoodsListFragment.CHOSEN_GOOD, good);
                startActivity(toGoodDetails);
            }
        };
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==  REQUEST_PERMISSION_CALL_PHONE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(offersViewModel.getAcceptedBidder().getValue().getPhonenumber());
            } else {
                openDialer(offersViewModel.getAcceptedBidder().getValue().getPhonenumber());
            }
        }
    }
}
