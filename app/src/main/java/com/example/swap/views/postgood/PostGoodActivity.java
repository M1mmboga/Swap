package com.example.swap.views.postgood;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.swap.R;
import com.example.swap.views.postgood.viewmodels.PostGoodViewModel;

public class PostGoodActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG_FORM = "form-fragment-tag";

    private PostItemFormFragment postItemFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_good);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Post Item");

        PostGoodViewModel postGoodViewModel =
                ViewModelProviders.of(this).get(PostGoodViewModel.class);

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            postItemFormFragment = new PostItemFormFragment();
            fragmentTransaction.add(R.id.fragment_container, postItemFormFragment, FRAGMENT_TAG_FORM);
            fragmentTransaction.commit();
        }
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
            fragmentTransaction.replace(R.id.fragment_container, insertImagesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }



    }

}
