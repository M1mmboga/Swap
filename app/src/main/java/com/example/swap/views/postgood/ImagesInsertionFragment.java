package com.example.swap.views.postgood;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.example.swap.R;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesInsertionFragment extends Fragment implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate, BSImagePicker.OnMultiImageSelectedListener {

    private final static Integer NUMBER_OF_SUPPLEMENTARY_IMGS = 4;
    private final static String PROVIDER_AUTHORITY = "com.johngachihi.example.swap.fileprovider";


    private Button addMainImageBtn;
    private MaterialButton addSupplementaryImagesBtn;
    private ImageView mainImageIv;
    private FlexboxLayout supplementaryImagesHolder;
    private List<ImageView> supplementaryImageViews = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_good_image_insert, container, false);
        addMainImageBtn = v.findViewById(R.id.add_main_img);
        addSupplementaryImagesBtn = v.findViewById(R.id.add_supplementary_imgs);
        mainImageIv = v.findViewById(R.id.main_image_iv);
        supplementaryImagesHolder = v.findViewById(R.id.supplementary_imgs_holder);

        for(int i = 0; i < NUMBER_OF_SUPPLEMENTARY_IMGS; i++) {
            ImageView imageView = (ImageView) inflater.inflate(
                    R.layout.supplementary_image_imageview,
                    supplementaryImagesHolder,
                    false);
//            imageView.setTag(i);
            supplementaryImageViews.add(imageView);
            supplementaryImagesHolder.addView(imageView);
        }

        setUpSingleImagePickerOn(addMainImageBtn, mainImageIv);
        setUpMultiImagePickerOn(addSupplementaryImagesBtn);
        setUpSingleImagePickerOnClickingSupImageViews(supplementaryImageViews);

        return v;
    }

    private void setUpSingleImagePickerOn(View... views){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker singleImagePicker = new BSImagePicker.Builder(PROVIDER_AUTHORITY)
                        .build();
                singleImagePicker.show(getChildFragmentManager(), "picker");
            }
        };
        for(View v : views) {
            v.setOnClickListener(onClickListener);
        }
    }

    private void setUpSingleImagePickerOnClickingSupImageViews(List<ImageView> supplementaryImageViews) {

        for(int i = 0; i < supplementaryImageViews.size(); i++) {
            final int finalI = i;
            supplementaryImageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BSImagePicker singleImagePicker = new BSImagePicker.Builder(PROVIDER_AUTHORITY)
                            .setTag("" + finalI)
                            .build();
                    singleImagePicker.show(getChildFragmentManager(), "picker");
                }
            });
        }
    }

    private void setUpMultiImagePickerOn(View... views) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker multiImagePicker = new BSImagePicker.Builder("com.johngachihi.ble123.fileprovider")
                        .isMultiSelect()
                        .setMinimumMultiSelectCount(1)
                        .setMaximumMultiSelectCount(NUMBER_OF_SUPPLEMENTARY_IMGS)
                        .build();
                multiImagePicker.show(getChildFragmentManager(), "picker");
            }
        };
        for(View v : views) {
            v.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        if(tag == null) {
            Glide.with(this).load(uri).into(mainImageIv);
        } else {
            Glide.with(this).load(uri).into(supplementaryImageViews.get(Integer.parseInt(tag)));
        }
    }

    @Override
    public void loadImage(File imageFile, ImageView ivImage) {
        Glide.with(getContext()).load(imageFile).into(ivImage);
    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        for (int i = 0; i < uriList.size(); i++) {
            Glide.with(getContext()).load(uriList.get(i)).into(supplementaryImageViews.get(i));
        }
    }
}
