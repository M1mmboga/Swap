package com.johngachihi.swap.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.johngachihi.swap.R;
import com.johngachihi.swap.models.Good;

public class GoodsListAdapter extends PagedListAdapter<Good, RecyclerView.ViewHolder> {
    private static final int ITEM_GOOD = 0;
    private static final int ITEM_PLACEHOLDER = 1;


    public GoodsListAdapter(@NonNull DiffUtil.ItemCallback<Good> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_GOOD) {
            return new GoodItemViewHolder(LayoutInflater.from(parent.getContext()), parent);
        } else {
            return new PlaceholderItemViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ITEM_GOOD) {
            Good good = getItem(position);
            ((GoodItemViewHolder)holder).goodNameTxt.setText(good.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? ITEM_PLACEHOLDER : ITEM_GOOD;
    }

    class GoodItemViewHolder extends RecyclerView.ViewHolder {
        public TextView goodNameTxt;

        public GoodItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.goods_list_item, parent, false));
            goodNameTxt = (TextView) itemView.findViewById(R.id.good_name_text);
        }
    }

    class PlaceholderItemViewHolder extends RecyclerView.ViewHolder {
        public PlaceholderItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.goods_list_placeholder, parent, false));
        }
    }
}
