package com.example.swap.views.postgood.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.swap.data.network.repository.GoodsRepository;
import com.example.swap.models.Good;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostGoodViewModel extends AndroidViewModel {

    private MutableLiveData<String> itemName = new MutableLiveData<>();
    private MutableLiveData<String> itemDescription = new MutableLiveData<>();
    private MutableLiveData<String> itemCategory = new MutableLiveData<>();
    private MutableLiveData<Integer> itemEstimatedPrice = new MutableLiveData<>();
    private MutableLiveData<String> itemLocation = new MutableLiveData<>();
    private MutableLiveData<Uri> itemMainImage = new MutableLiveData<>();
    private MutableLiveData<List<Uri>>  itemSupplementaryImages = new MutableLiveData<>();

    public PostGoodViewModel(@NonNull Application application) {
        super(application);
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(fileUri.getPath());
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getApplication().getApplicationContext().getContentResolver().getType(fileUri)),
                file
        );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private Good prepareGood(String name, String description,
                             String category, int estimatedPrice,
                             String location)
    {
        return null;
    }

    private void submitItem() {
        GoodsRepository goodsRepository = new GoodsRepository();
//        goodsRepository.addGood(new Good())
    }

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

    public void setItemSupplementaryImagesLiveData(List<Uri> uris) {
        itemSupplementaryImages.setValue(uris);
    }
}
