package com.example.swap.views.postgood.viewmodels;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.swap.data.network.repository.GoodsRepository;
import com.example.swap.models.Good;
import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.utils.fileutils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Callback;

public class PostGoodViewModel extends AndroidViewModel {
    private final static String PROVIDER_AUTHORITY = "com.johngachihi.example.swap.fileprovider";

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
        File file = FileUtils.getFile(getApplication(), fileUri);
        String fileType = getApplication().getApplicationContext()
                .getContentResolver().getType(fileUri);
        if(fileType == null) {
            fileType = getApplication().getApplicationContext()
                    .getContentResolver().getType(
                            FileProvider.getUriForFile(getApplication(), PROVIDER_AUTHORITY, file));
        }
        Log.e("PostGoodViewModel", "fileType: " + Uri.fromFile(file));
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(fileType),
                file
        );

        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private Good prepareGood() {
        Good good = new Good();
        good.setName(itemName.getValue());
        good.setDescription(itemDescription.getValue());
        good.setCategory(itemCategory.getValue());
        if(itemEstimatedPrice.getValue() != null) {
            good.setPriceEstimate(itemEstimatedPrice.getValue());
        }
        good.setLocation(itemLocation.getValue());

        /*final MediaType JSON
                = MediaType.get("application/json; charset=utf-8");

//        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(good));

        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("name", "bleble")
                .addFormDataPart("description", itemCategory.getValue())
                .build();*/

        return good;
    }

    public void submitGood(Callback<Good> callback) {
        GoodsRepository goodsRepository = new GoodsRepository();
        Good good = prepareGood();
        MultipartBody.Part mainImage = prepareFilePart("main_image", itemMainImage.getValue());
        List<MultipartBody.Part> supImages = new ArrayList<>();
        if(itemSupplementaryImages.getValue() != null) {
            for(Uri imageUri : itemSupplementaryImages.getValue()) {
                supImages.add(prepareFilePart("sup_images[]", imageUri));
            }
        }
        User currentUser = Auth.of(getApplication()).getCurrentUser();
        goodsRepository.addGood(good, currentUser.getId(), mainImage, supImages, callback);
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
