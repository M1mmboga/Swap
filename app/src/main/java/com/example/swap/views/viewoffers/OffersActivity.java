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
import com.example.swap.ProfileActivity;
import com.example.swap.R;
import com.example.swap.TempHomePage;
import com.example.swap.UserGoodsActivity;
import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.models.Good;
import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;
import com.example.swap.views.authentication.LoginActivity;
import com.example.swap.views.postgood.PostGoodActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
        Toolbar toolbar = findViewById(R.id.activity_offers_toolbar);
        setSupportActionBar(toolbar);
        setUpNavDrawer(toolbar);

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

    private void setUpNavDrawer(Toolbar toolbar) {
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader())
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(5).withName("Home").withIcon(R.drawable.ic_home_black_24dp),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Post Item").withIcon(R.drawable.ic_add_black_24dp),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Your Offers").withIcon(R.drawable.ic_library_books_black_24dp),
                        new PrimaryDrawerItem().withIdentifier(6).withName("My Account").withIcon(R.drawable.ic_account_box_black_24dp),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Your posted items").withIcon(R.drawable.ic_library_books_black_24dp),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(1).withName("Logout").withIcon(R.drawable.ic_lock_outline_black_24dp)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem.getIdentifier() == 1) {
                            Auth.of(getApplication()).logout(task -> {});
//                            Auth.of(getApplication()).logout_GoogleSignIn(task -> {});
//                            Auth.of(getApplication()).logout_Swap();
                            startActivity(new Intent(OffersActivity.this, LoginActivity.class));
                            finish();
                        } else if(drawerItem.getIdentifier() == 2) {
                            startActivity(new Intent(OffersActivity.this, PostGoodActivity.class));
                        } else if(drawerItem.getIdentifier() == 3) {
                            startActivity(new Intent(OffersActivity.this, OffersActivity.class));
                        } else if(drawerItem.getIdentifier() == 4) {
                            startActivity(new Intent(OffersActivity.this, UserGoodsActivity.class));
                        } else if(drawerItem.getIdentifier() == 5) {
                            startActivity(new Intent(OffersActivity.this, TempHomePage.class));
                        } else if(drawerItem.getIdentifier() == 6) {
                            startActivity(new Intent(OffersActivity.this, ProfileActivity.class));
                        }
                        return false;
                    }
                })
                .build();
    }

    private AccountHeader accountHeader() {
        User user = Auth.of(getApplication()).getCurrentUser();
        return new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getFirstname() + " " + user.getLastname()).withEmail(user.getEmail())
                )
                .build();
    }
}
