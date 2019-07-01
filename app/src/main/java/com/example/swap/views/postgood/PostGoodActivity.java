package com.example.swap.views.postgood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.swap.R;
import com.example.swap.models.Good;
import com.example.swap.utils.Auth;
import com.example.swap.views.authentication.LoginActivity;
import com.example.swap.views.postgood.viewmodels.PostGoodViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostGoodActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG_FORM = "form-fragment-tag";
    private static final String FRAGMENT_TAG_IMAGES_INSERT = "images-insert-fragment-tag";

    private PostItemFormFragment postItemFormFragment;
    private PostGoodViewModel postGoodViewModel;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_good);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Post Item");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        postGoodViewModel = ViewModelProviders
                .of(this).get(PostGoodViewModel.class);

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            postItemFormFragment = new PostItemFormFragment();
            fragmentTransaction.add(R.id.fragment_container, postItemFormFragment, FRAGMENT_TAG_FORM);
            fragmentTransaction.commit();
        }

        setUpNavDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.post_item_form_next) {
            openNextFragment();
        } else if(id == R.id.post_item_images_next) {
            submitGood();
        } else if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openNextFragment() {
        PostItemFormFragment postItemFormFragment = (PostItemFormFragment)
                getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_FORM);
        if(postItemFormFragment.submitForm()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left
            );

            ImagesInsertionFragment insertImagesFragment = new ImagesInsertionFragment();
            fragmentTransaction.replace(R.id.fragment_container, insertImagesFragment, FRAGMENT_TAG_IMAGES_INSERT);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    private void submitGood() {
        ImagesInsertionFragment imagesInsertionFragment = (ImagesInsertionFragment)
                getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_IMAGES_INSERT);
        if(imagesInsertionFragment.isInputValid()) {
            final String TAG = "GOOD-ITEM";
            Log.d(TAG, "Good Name: " + postGoodViewModel.getItemName().getValue());
            Log.d(TAG, "Good Description: " + postGoodViewModel.getItemDescription().getValue());
            Log.d(TAG, "Good Category: " + postGoodViewModel.getItemCategory().getValue());
            Log.d(TAG, "Good Estimated Price: " + postGoodViewModel.getItemEstimatedPrice().getValue());
            Log.d(TAG, "Good Location: " + postGoodViewModel.getItemLocation().getValue());
            Log.d(TAG, "Good Main Image: " + postGoodViewModel.getItemMainImage().getValue());
            if(postGoodViewModel.getItemSupplementaryImages().getValue() != null &&
                    postGoodViewModel.getItemSupplementaryImages().getValue().get(0) != null)
            {
                Log.d(TAG, "Good Sup Image (1)" + postGoodViewModel.getItemSupplementaryImages().getValue().get(0));
            }
            postGoodViewModel.submitGood(new Callback<Good>() {
                @Override
                public void onResponse(Call<Good> call, Response<Good> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PostGoodActivity.this,
                                "Successfully added", Toast.LENGTH_SHORT).show();
                        Log.e("On Post Good", response.message());
                    } else {
                        Toast.makeText(PostGoodActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                        Log.e("On Post Good", response.message());
                        try {
                            Log.e("On post good", response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Good> call, Throwable t) {
                    Toast.makeText(PostGoodActivity.this,
                            t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    Log.e("On Post Good", t.getMessage());
                }
            });
        } else {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
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
                            startActivity(new Intent(PostGoodActivity.this, LoginActivity.class));
                            finish();
                        }
                        return false;
                    }
                })
                .build();
    }

}
