package com.example.swap;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.utils.NotificationHandler;
import com.example.swap.utils.broadcastReceivers.NotificationBroadcastReceiver;
import com.example.swap.views.authentication.LoginActivity;
import com.example.swap.views.postgood.PostGoodActivity;
import com.example.swap.views.viewoffers.OffersActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

public class TempHomePage extends AppCompatActivity
        implements View.OnClickListener, NotificationHandler {

    private Button booksCategoryBtn;
    private Button clothesCategoryBtn;
    private Button furnitureCategoryBtn;
    private View rootView;

    private GoogleSignInClient mGoogleSignInClient;

    private NotificationBroadcastReceiver notificationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.temp_homepage_toolbar);
        setSupportActionBar(toolbar);

        booksCategoryBtn = (Button) findViewById(R.id.books_category_btn);
        booksCategoryBtn.setOnClickListener(this);
        clothesCategoryBtn = (Button) findViewById(R.id.clothes_category_btn);
        clothesCategoryBtn.setOnClickListener(this);
        furnitureCategoryBtn = (Button) findViewById(R.id.furniture_category_btn);
        furnitureCategoryBtn.setOnClickListener(this);
        rootView = findViewById(R.id.activity_temp_homepage_root);

        User user = Auth.of(getApplication()).getCurrentUser();
        if(user != null) {
//            Toast.makeText(this, "first name: " + user.getFirstname(), Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        setUpNavDrawer(toolbar);
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            Intent i = new Intent(this, ListActivity.class);
            i.putExtra("category", ((Button)v).getText());
            startActivity(i);
        }
    }

    private void setUpNavDrawer(Toolbar toolbar) {
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(accountHeader())
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(2).withName("Post Item").withIcon(R.drawable.ic_add_black_24dp),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Your Offers").withIcon(R.drawable.ic_library_books_black_24dp),
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
                            startActivity(new Intent(TempHomePage.this, LoginActivity.class));
                            finish();
                        } else if(drawerItem.getIdentifier() == 2) {
                            startActivity(new Intent(TempHomePage.this, PostGoodActivity.class));
                        } else if(drawerItem.getIdentifier() == 3) {
                            startActivity(new Intent(TempHomePage.this, OffersActivity.class));
                        } else if(drawerItem.getIdentifier() == 4) {
                            startActivity(new Intent(TempHomePage.this, UserGoodsActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        if(notificationBroadcastReceiver == null) {
            notificationBroadcastReceiver = new NotificationBroadcastReceiver(this);
        }
        IntentFilter intentFilter = new IntentFilter(MyFirebaseMessagingService.NOTIFICATION_BC);
        registerReceiver(notificationBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(notificationBroadcastReceiver != null) {
            unregisterReceiver(notificationBroadcastReceiver);
        }
    }

    @Override
    public void handleNotification() {
        Snackbar.make(rootView, "You have just received an offer!", Snackbar.LENGTH_INDEFINITE)
                .setAction("View", v -> {
                    startActivity(new Intent(TempHomePage.this, OffersActivity.class));
                })
                .show();
    }
}

