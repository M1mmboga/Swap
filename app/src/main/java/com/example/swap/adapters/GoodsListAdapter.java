package com.example.swap.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;
import com.example.swap.models.Good;
import com.example.swap.rest.constants.Addresses;
import com.squareup.picasso.Picasso;

//import com.johngachihi.swap.R;

public class GoodsListAdapter extends PagedListAdapter<Good, RecyclerView.ViewHolder> {
    private static final int ITEM_GOOD = 0;
    private static final int ITEM_PLACEHOLDER = 1;

    private Context context;

    public GoodsListAdapter(@NonNull DiffUtil.ItemCallback<Good> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
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
            ImageView goodImage = ((GoodItemViewHolder)holder).goodImage;
            Picasso.get().load(Addresses.IMAGES_HOME + good.getImageFileName()).into(goodImage);
            ((GoodItemViewHolder)holder).goodNameTxt.setText(good.getName());
            ((GoodItemViewHolder) holder).goodDescriptionTxt.setText(good.getDescription());
            Log.d("Offerer", good.getOfferer().getEmail());
            ((GoodItemViewHolder) holder).goodPriceRangeTxt.setText(
                    this.context.getString(R.string.good_price_range, good.getPriceRangeMin(), good.getPriceRangeMax()));
            ((GoodItemViewHolder) holder).goodCategoryTxt.setText(
                    context.getString(R.string.goods_list_category_label, good.getCategory()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? ITEM_PLACEHOLDER : ITEM_GOOD;
    }

    class GoodItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView goodImage;
        public TextView goodNameTxt;
        public TextView goodDescriptionTxt;
        public TextView goodPriceRangeTxt;
        public TextView goodCategoryTxt;

        public GoodItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.goods_list_item, parent, false));
            goodImage = (ImageView) itemView.findViewById(R.id.good_image);
            goodNameTxt = (TextView) itemView.findViewById(R.id.good_name_text);
            goodDescriptionTxt = (TextView) itemView.findViewById(R.id.good_description);
            goodPriceRangeTxt = (TextView) itemView.findViewById(R.id.good_price_range);
            goodCategoryTxt = (TextView) itemView.findViewById(R.id.temp_good_category);
        }
    }

    class PlaceholderItemViewHolder extends RecyclerView.ViewHolder {
        public PlaceholderItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.goods_list_placeholder, parent, false));
        }
    }
}
