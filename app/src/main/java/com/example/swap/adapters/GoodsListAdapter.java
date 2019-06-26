package com.example.swap.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;
import com.example.swap.models.Good;
import com.example.swap.rest.NetworkState;
import com.example.swap.rest.addressconstants.Addresses;
import com.squareup.picasso.Picasso;

//import com.johngachihi.swap.R;

public class GoodsListAdapter extends PagedListAdapter<Good, RecyclerView.ViewHolder> {
    private static final int ITEM_GOOD = 0;
    private static final int ITEM_PLACEHOLDER = 1;

    private Context context;
    private NetworkState networkState;
    private View.OnClickListener goodItemOnClickListener;

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
            return new PlaceholderItemViewHolder(LayoutInflater.from(parent.getContext()), parent, this.networkState);
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
            ((GoodItemViewHolder) holder).goodPriceRangeTxt.setText(
                    this.context.getString(R.string.good_price_range, good.getPriceRangeMin(), good.getPriceRangeMax()));
            ((GoodItemViewHolder) holder).goodCategoryTxt.setText(
                    context.getString(R.string.goods_list_category_label, good.getCategory()));
        } else {
            ((PlaceholderItemViewHolder)holder).itemView1.setOnClickListener(v -> {
                Log.d("load item clicked", "Position: " + position);
            });
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if(hasExtraRow() && position == getItemCount() - 1) {
            return ITEM_PLACEHOLDER;
        } else {
            return ITEM_GOOD;
        }
    }

    @Override
    public int getItemCount() {
        return hasExtraRow() ? super.getItemCount() +  1 : super.getItemCount();
    }

    public void setNetworkState(NetworkState newNetworkState) {
//        Log.d("load setNetworkState", newNetworkState.getStatus().toString());
        if(networkState != null)
            Log.d("load setNetworkState", this.networkState.getStatus().toString() + " to " + newNetworkState.getStatus().toString());

        NetworkState prevNetworkState = this.networkState;
        boolean hadExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean hasExtraRow = hasExtraRow();
        if(hadExtraRow != hasExtraRow) {
            if(hadExtraRow) {
                notifyItemRemoved(super.getItemCount());
                Log.d("load setNS", "Load item removed");
            } else {
                notifyItemInserted(super.getItemCount());
                Log.d("load setNS", "Load item inserted");
            }
        } else if(hasExtraRow && prevNetworkState != newNetworkState){
            notifyItemChanged(getItemCount() - 1);
            Log.d("load setNS", "Load item changed");
            Log.d("load setNS", "Position: " + (getItemCount() - 1));
        }
    }

    public void setGoodItemOnClickListener(View.OnClickListener onClickListener) {
        this.goodItemOnClickListener = onClickListener;
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

            if(goodItemOnClickListener != null) {
                itemView.setTag(this);
                itemView.setOnClickListener(goodItemOnClickListener);
            } else {
                throw new NullPointerException("Must set goodItemOnClickListener.");
            }
        }
    }

    class PlaceholderItemViewHolder extends RecyclerView.ViewHolder {
        public View itemView1;
        public PlaceholderItemViewHolder(LayoutInflater inflater, ViewGroup parent, NetworkState networkState) {
            super(inflater.inflate(R.layout.goods_list_item_loading, parent, false));

            Log.d("LoadingItemViewHolder", "ble " + networkState.getStatus().toString());

            ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.goods_list_item_progressbar);
            Button retryBtn = (Button) itemView.findViewById(R.id.goods_list_item_retry_btn);
            retryBtn.setOnClickListener(v -> {
                networkState.getRetryable().retry();
            });

            itemView1 = itemView;

            if(networkState.getStatus() == NetworkState.Status.FAILED) {
                progressBar.setVisibility(View.GONE);
                retryBtn.setVisibility(View.VISIBLE);
            } else if(networkState.getStatus() == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
                retryBtn.setVisibility(View.GONE);
            }
        }
    }

    private Picasso buildPicasso() {
        Picasso.Builder picassoBuiler = new Picasso.Builder(context);
        Picasso picasso = picassoBuiler.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
//                exception.printStackTrace();
                Log.e("Picasso Load Failed", exception.getMessage());
            }
        }).build();

        return picasso;
    }
}
