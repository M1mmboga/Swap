package com.example.swap.views.postgood.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PostGoodViewModel extends ViewModel {

    private MutableLiveData<String> itemName = new MutableLiveData<>();
    private MutableLiveData<String> itemDescription = new MutableLiveData<>();
    private MutableLiveData<String> itemCategory = new MutableLiveData<>();
    private MutableLiveData<String> itemEstimatedPrice = new MutableLiveData<>();

    public MutableLiveData<String> getItemName() {
        return itemName;
    }

    public MutableLiveData<String> getItemDescription() {
        return itemDescription;
    }

    public MutableLiveData<String> getItemCategory() {
        return itemCategory;
    }

    public MutableLiveData<String> getItemEstimatedPrice() {
        return itemEstimatedPrice;
    }

    public void setItemNameLiveData(String itemName) {
        this.itemName.setValue(itemName);
    }

}
