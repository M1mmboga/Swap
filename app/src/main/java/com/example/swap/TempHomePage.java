package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.views.authentication.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class TempHomePage extends AppCompatActivity implements View.OnClickListener {

    private Button booksCategoryBtn;
    private Button clothesCategoryBtn;
    private Button furnitureCategoryBtn;

    private GoogleSignInClient mGoogleSignInClient;

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

        User user = Auth.of(getApplication()).getCurrentUser();
        if(user != null) {
            Toast.makeText(this, "first name: " + user.getFirstname(), Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setUpNavDrawer();
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            Intent i = new Intent(this, ListActivity.class);
            i.putExtra("category", ((Button)v).getText());
            startActivity(i);
        }
    }

    private void setUpNavDrawer() {
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .addDrawerItems(
                        new SecondaryDrawerItem().withIdentifier(1).withName("Logout")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if(drawerItem.getIdentifier() == 1) {
                            Auth.of(getApplication()).logout_GoogleSignIn(mGoogleSignInClient, task -> {});
                            Auth.of(getApplication()).logout_Swap();
                            startActivity(new Intent(TempHomePage.this, LoginActivity.class));
                            finish();
                        }
                        return false;
                    }
                })
                .build();
    }
}

