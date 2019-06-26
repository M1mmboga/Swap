package com.example.swap.views.postgood.viewmodels;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PostGoodViewModel extends ViewModel {

    private MutableLiveData<String> itemName = new MutableLiveData<>();
    private MutableLiveData<String> itemDescription = new MutableLiveData<>();
    private MutableLiveData<String> itemCategory = new MutableLiveData<>();
    private MutableLiveData<Integer> itemEstimatedPrice = new MutableLiveData<>();
    private MutableLiveData<String> itemLocation = new MutableLiveData<>();
    private MutableLiveData<Uri> itemMainImage = new MutableLiveData<>();
    private MutableLiveData<List<Uri>>  itemSupplementaryImages = new MutableLiveData<>();

    public MutableLiveData<String> getItemName() {
        return itemName;
    }

    public MutableLiveData<String> getItemDescription() {
        return itemDescription;
    }

    public MutableLiveData<String> getItemCategory() {
        return itemCategory;
    }

    public MutableLiveData<Integer> getItemEstimatedPrice() {
        return itemEstimatedPrice;
    }

    public MutableLiveData<String> getItemLocation() {
        return itemLocation;
    }


    public void setItemNameLiveData(String itemNameValue) {
        itemName.setValue(itemNameValue);
    }

    public void setItemDescriptionLiveData(String itemDescValue) {
        itemDescription.setValue(itemDescValue);
    }

    public void setItemCategoryLiveData(String itemCategoryValue) {
        itemCategory.setValue(itemCategoryValue);
    }

    public void setItemEstimatedPriceLiveData(Integer itemEstimatedPriceValue) {
        itemEstimatedPrice.setValue(itemEstimatedPriceValue);
    }

    public void setItemLocationLiveData(String itemLocationValue) {
        itemLocation.setValue(itemLocationValue);
    }

    public MutableLiveData<Uri> getItemMainImage() {
        return itemMainImage;
    }

    public MutableLiveData<List<Uri>> getItemSupplementaryImages() {
        return itemSupplementaryImages;
    }
}
