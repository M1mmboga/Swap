package com.example.swap;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.swap.fragments.GoodsListFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        handleIntent(intent);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        GoodsListFragment goodsListFragment = new GoodsListFragment();
//        Bundle fragmentParams = new Bundle();
//        fragmentParams.putString(CATEGORY_CRITERIA, category);
//        goodsListFragment.setArguments(fragmentParams);
        fragmentTransaction.add(R.id.list_fragment_container, goodsListFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchActivity Query", query);

            Bundle searchData = intent.getBundleExtra(SearchManager.APP_DATA);
            if(searchData != null) {
                String category = searchData.getString(ListActivity.CATEGORY_CRITERIA);
                Log.d("SearchActivity Category", category);
            }
        }
    }
}
