package com.example.swap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.johngachihi.swap.R;
import com.example.swap.adapters.GoodsListAdapter;
import com.example.swap.models.Good;

public class GoodsListFragment extends Fragment {

    LiveData<PagedList<Good>> goodsPagedList;

    public GoodsListFragment(LiveData<PagedList<Good>> goodsPagedList) {
        this.goodsPagedList = goodsPagedList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_goods_list, container, false);

        GoodsListAdapter adapter = new GoodsListAdapter(getDiffCallback());
        goodsPagedList.observe(this, adapter::submitList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView recyclerView = v.findViewById(R.id.goods_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        return v;
    }

    private DiffUtil.ItemCallback<Good> getDiffCallback() {
        return new DiffUtil.ItemCallback<Good>() {
            @Override
            public boolean areItemsTheSame(@NonNull Good oldItem, @NonNull Good newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Good oldItem, @NonNull Good newItem) {
                return oldItem.equals(newItem);
            }
        };
    }
}
