package com.example.swap;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.viewmodels.GoodsByCategoryViewModel;
import com.example.swap.viewmodels.factories.GoodsByCategoryViewModelFactory;

public class ListActivity extends AppCompatActivity {

    private GoodsByCategoryViewModel goodsByCategoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_list_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        String category = i.getStringExtra("category");

        goodsByCategoryViewModel = ViewModelProviders
                .of(this, new GoodsByCategoryViewModelFactory(getApplication(), category))
                .get(GoodsByCategoryViewModel.class);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        GoodsListFragment goodsListFragment =
                new GoodsListFragment(goodsByCategoryViewModel.getGoodsPagedList());
        fragmentTransaction.add(R.id.list_fragment_container, goodsListFragment);
        fragmentTransaction.commit();
    }
}
