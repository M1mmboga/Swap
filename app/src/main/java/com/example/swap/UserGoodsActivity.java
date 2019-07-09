package com.example.swap;

import android.content.Intent;
import android.os.Bundle;

import com.example.swap.datasources.goods.goodfetchers.ByCategoryGoodsFetcher;
import com.example.swap.datasources.goods.goodfetchers.UserGoodsFetcher;
import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NetworkState;
import com.example.swap.viewmodels.GoodsFetcherViewModel;
import com.example.swap.viewmodels.GoodsListViewModel;
import com.example.swap.viewmodels.factories.GoodsListViewModelFactory;
import com.example.swap.views.authentication.LoginActivity;
import com.example.swap.views.postgood.PostGoodActivity;
import com.example.swap.views.viewoffers.OffersActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

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
        setUpNavDrawer(toolbar);

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
                            startActivity(new Intent(UserGoodsActivity.this, LoginActivity.class));
                            finish();
                        } else if(drawerItem.getIdentifier() == 2) {
                            startActivity(new Intent(UserGoodsActivity.this, PostGoodActivity.class));
                        } else if(drawerItem.getIdentifier() == 3) {
                            startActivity(new Intent(UserGoodsActivity.this, OffersActivity.class));
                        } else if(drawerItem.getIdentifier() == 4) {
                            startActivity(new Intent(UserGoodsActivity.this, UserGoodsActivity.class));
                        } else if(drawerItem.getIdentifier() == 5) {
                            startActivity(new Intent(UserGoodsActivity.this, TempHomePage.class));
                        } else if(drawerItem.getIdentifier() == 6) {
                            startActivity(new Intent(UserGoodsActivity.this, ProfileActivity.class));
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
