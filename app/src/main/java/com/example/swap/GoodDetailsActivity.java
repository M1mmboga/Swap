package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.swap.fragments.GoodsListFragment;
import com.example.swap.models.Good;
import com.example.swap.utils.Auth;
import com.example.swap.utils.addressconstants.Addresses;
import com.example.swap.views.makeoffer.SelectGoodsToExchangeActivity;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.DefaultSliderView;

import java.text.DecimalFormat;

public class GoodDetailsActivity extends AppCompatActivity {

    public static final String GOOD_ID = "good-id";

    private SliderLayout slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);

        Intent intent = getIntent();
        Good good = (Good) intent.getSerializableExtra(GoodsListFragment.CHOSEN_GOOD);

        Toolbar toolbar = findViewById(R.id.activity_good_details_toolbar);
        setSupportActionBar(toolbar);
        setTitle(good.getName());

        slider = (SliderLayout) findViewById(R.id.good_images_slider);

        slider.addSlider(
                new DefaultSliderView(this)
                    .image(Addresses.IMAGES_HOME + good.getImageFileName())
                    .setProgressBarVisible(true)
        );

        if(good.getSupplementaryImageFileNames() != null) {
            for (String image : good.getSupplementaryImageFileNames()) {
                DefaultSliderView sliderView = new DefaultSliderView(this);
                sliderView
                        .image(Addresses.IMAGES_HOME + image)
                        .setProgressBarVisible(true);
                slider.addSlider(sliderView);
            }
        }

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(7000);

        /*USE DATA-BINDING INSTEAD !!!*/
        ((TextView) findViewById(R.id.good_details_good_name)).setText(good.getName());
        ((TextView) findViewById(R.id.good_details_category)).setText(good.getCategory());
        ((TextView) findViewById(R.id.good_details_good_offerer))
                .setText(good.getOfferer().getFirstname() + " " + good.getOfferer().getLastname());

        String priceApproximation = new DecimalFormat("#,###")
                .format(good.getPriceEstimate());
        ((TextView) findViewById(R.id.good_details_price_approx))
                .setText(getString(R.string.good_details_price_approx, priceApproximation));

        ((TextView) findViewById(R.id.good_details_description))
                .setText(good.getDescription());

        Button makeOfferBtn = findViewById(R.id.good_details_make_offer_button);
        if(Auth.of(getApplication()).getCurrentUser().getId() == good.getOfferer().getId()) {
            makeOfferBtn.setVisibility(View.GONE);
        }
//        ((Button) findViewById(R.id.good_details_make_offer_button))
        makeOfferBtn.setOnClickListener(v -> {
            Intent toGoodDetailsIntent = new Intent(
                    GoodDetailsActivity.this,
                    SelectGoodsToExchangeActivity.class
            );
            toGoodDetailsIntent.putExtra(GOOD_ID, good.getId());
            startActivity(toGoodDetailsIntent);
        });
    }

    @Override
    protected void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }
}
