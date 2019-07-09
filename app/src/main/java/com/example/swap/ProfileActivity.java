package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.views.authentication.LoginActivity;
import com.example.swap.views.postgood.PostGoodActivity;
import com.example.swap.views.viewoffers.OffersActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class ProfileActivity extends AppCompatActivity {

    TextView myUsername, myEmail, myPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_profile_toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer(toolbar);

        myUsername = (TextView) findViewById(R.id.username);
        myEmail = (TextView) findViewById(R.id.email);
        myPhone = (TextView) findViewById(R.id.phone);

        User user = Auth.of(getApplication()).getCurrentUser();
        myUsername.setText(user.getFirstname()+' '+user.getLastname());
        myEmail.setText(user.getEmail());
        myPhone.setText(user.getPhonenumber());
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
                            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                            finish();
                        } else if(drawerItem.getIdentifier() == 2) {
                            startActivity(new Intent(ProfileActivity.this, PostGoodActivity.class));
                        } else if(drawerItem.getIdentifier() == 3) {
                            startActivity(new Intent(ProfileActivity.this, OffersActivity.class));
                        } else if(drawerItem.getIdentifier() == 4) {
                            startActivity(new Intent(ProfileActivity.this, UserGoodsActivity.class));
                        } else if(drawerItem.getIdentifier() == 5) {
                            startActivity(new Intent(ProfileActivity.this, TempHomePage.class));
                        } else if(drawerItem.getIdentifier() == 6) {
                            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
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

