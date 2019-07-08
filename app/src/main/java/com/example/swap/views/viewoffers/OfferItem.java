package com.example.swap.views.viewoffers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.swap.R;
import com.example.swap.models.Good;
import com.example.swap.models.User;
import com.example.swap.utils.addressconstants.Addresses;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.text.DecimalFormat;
import java.util.List;

public class OfferItem extends AbstractItem {
    private Good goodWanted;
    private List<Good> offeredGoods;
    private User bidder;

    private Context context;

    public OfferItem(Context context, Good goodWanted, List<Good> offeredGoods, User bidder) {
        this.goodWanted = goodWanted;
        this.offeredGoods = offeredGoods;
        this.bidder = bidder;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder getViewHolder(View v) {
        return new OfferViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.item_offer;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_offer;
    }

    public Good getGoodWanted() {
        return goodWanted;
    }

    public List<Good> getOfferedGoods() {
        return offeredGoods;
    }

    public User getBidder() {
        return bidder;
    }

    class OfferViewHolder extends FastAdapter.ViewHolder<OfferItem> {

        private TextView bidderNameTxt;
        private ImageView wantedGoodImage;
        private TextView wantedGoodNameTxt;
        private LinearLayout offeredGoodsContainer;

        public OfferViewHolder(View itemView) {
            super(itemView);
            bidderNameTxt = itemView.findViewById(R.id.item_offer_bidder);
            wantedGoodImage = itemView.findViewById(R.id.item_offer_wanted_good_image);
            wantedGoodNameTxt = itemView.findViewById(R.id.item_offer_wanted_good_name);
            offeredGoodsContainer = itemView.findViewById(R.id.item_offer_offered_goods_container);
        }

        @Override
        public void bindView(OfferItem item, List<Object> payloads) {
            bidderNameTxt.setText(item.context.getString(
                    R.string.label_from,
                    item.bidder.getFirstname() + " " + item.bidder.getLastname()) );

            String imageFileName = Addresses.IMAGES_HOME + item.goodWanted.getImageFileName();
            Glide.with(item.context)
                    .load(imageFileName)
                    .centerCrop()
                    .into(wantedGoodImage);

            wantedGoodNameTxt.setText(item.goodWanted.getName());

            addOfferedGoodsToLinearLayout(item.offeredGoods, offeredGoodsContainer);
        }

        private void addOfferedGoodsToLinearLayout(List<Good> offeredGoods, LinearLayout linearLayout) {
            for(Good good : offeredGoods) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.item_offered_good,
                        linearLayout, false);
                ImageView wantedGoodImageView = ((ImageView) v.findViewById(R.id.item_offered_good_image));
                Glide.with(context)
                        .load(Addresses.IMAGES_HOME + good.getImageFileName())
                        .centerCrop()
                        .into(wantedGoodImageView);
                ((TextView) v.findViewById(R.id.item_offered_good_name)).setText(good.getName());
                String priceEstimate = new DecimalFormat("#,###")
                        .format(good.getPriceEstimate());
                ((TextView) v.findViewById(R.id.item_offered_good_price_estimate))
                        .setText(context.getString(R.string.label_price_estimate, priceEstimate));

                linearLayout.addView(v);
            }
        }

        @Override
        public void unbindView(OfferItem item) {

        }
    }
}
