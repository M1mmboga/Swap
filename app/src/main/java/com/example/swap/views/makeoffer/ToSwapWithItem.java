package com.example.swap.views.makeoffer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swap.R;
import com.example.swap.models.Good;
import com.example.swap.utils.addressconstants.Addresses;
import com.google.android.material.card.MaterialCardView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public class ToSwapWithItem extends AbstractItem {
//    private String goodName;
//    private String imageFileName;
    private Good good;
    private boolean isSelected;
    Context context;

//    public ToSwapWithItem() {
//    }

    public ToSwapWithItem(Context context, Good good) {
//        this.goodName = goodName;
//        this.imageFileName = imageFileName;
        this.context = context;
        this.good = good;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder getViewHolder(View v) {
        return new MyViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.items_to_swap_with_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.good_to_swap_with_list_item;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    class MyViewHolder extends FastAdapter.ViewHolder<ToSwapWithItem> {
        MaterialCardView cardView;
        ImageView imageView;
        TextView goodName;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.items_to_swap_with_item);
            imageView = itemView.findViewById(R.id.item_to_swap_with_list_item_image);
            goodName = itemView.findViewById(R.id.item_to_swap_with_list_item_name);
        }

        @Override
        public void bindView(ToSwapWithItem item, List<Object> payloads) {
            String imageFileName = Addresses.IMAGES_HOME + item.getGood().getImageFileName();
            Glide.with(context).load(imageFileName).into(imageView);
            goodName.setText(item.getGood().getName());
            cardView.setChecked(item.isSelected);
        }

        @Override
        public void unbindView(ToSwapWithItem item) {
            goodName.setText(null);
        }
    }
}
