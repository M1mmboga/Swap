package com.example.swap.views.postgood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;
import com.example.swap.views.postgood.viewmodels.PostGoodViewModel;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;

public class PostItemFormFragment extends Fragment {

    private static final int FORM_ELEMENT_NAME = 1;
    private static final int FORM_ELEMENT_DESCRIPTION = 2;
    private static final int FORM_ELEMENT_CATEGORY = 3;
    private static final int FORM_ELEMENT_PRICE_ESTIMATE = 4;
    private static final int FORM_ELEMENT_LOCATION = 5;

    private PostGoodViewModel postGoodViewModel;
    private FormBuilder formBuilder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        postGoodViewModel = ViewModelProviders
                .of(getActivity()).get(PostGoodViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_item_form, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.post_item_form_recyclerview);
        setUpForm(recyclerView);

        return view;
    }

    private void setUpForm(RecyclerView recyclerView) {
        formBuilder = new FormBuilder(getActivity(), recyclerView);
        FormHeader itemNameFormHeader = FormHeader.createInstance(getString(R.string.item_name_and_description));
        FormElementTextSingleLine nameElement = FormElementTextSingleLine.createInstance()
                .setTag(FORM_ELEMENT_NAME)
                .setRequired(true)
                .setValue(postGoodViewModel.getItemName().getValue())
                .setTitle(getString(R.string.item_name))
                .setHint(getString(R.string.item_name_element_hint));
        FormElementTextMultiLine descriptionElement = FormElementTextMultiLine.createInstance()
                .setTag(FORM_ELEMENT_DESCRIPTION)
                .setRequired(true)
                .setValue(postGoodViewModel.getItemDescription().getValue())
                .setTitle("Item Description")
                .setHint("Insert item's description");

        FormHeader itemCategoryHeader = FormHeader.createInstance("Item Category");
        List<String> categories = new ArrayList<>();
        categories.add("Books");
        categories.add("Electronics");
        categories.add("Furniture");
        categories.add("Men Clothing");
        categories.add("Women Clothing");
        FormElementPickerSingle categoryPicker = FormElementPickerSingle.createInstance()
                .setTag(FORM_ELEMENT_CATEGORY)
                .setRequired(true)
                .setValue(postGoodViewModel.getItemCategory().getValue())
                .setTitle("Select Item Category")
                .setOptions(categories)
                .setPickerTitle("Select item's category");

        FormHeader misellaneousSectionHeader = FormHeader.createInstance("Other Details");
        FormElementTextNumber priceEstimateElement = FormElementTextNumber.createInstance()
                .setTag(FORM_ELEMENT_PRICE_ESTIMATE)
                .setRequired(true)
                .setValue(postGoodViewModel.getItemEstimatedPrice().getValue() == null ? "" : "" + postGoodViewModel.getItemEstimatedPrice().getValue())
                .setTitle("Estimated Price")
                .setHint("eg 5000");

        FormElementTextMultiLine locationElement = FormElementTextMultiLine.createInstance()
                .setTag(FORM_ELEMENT_LOCATION)
                .setValue(postGoodViewModel.getItemLocation().getValue())
                .setTitle(("Location"));

        List<BaseFormElement> formElements = new ArrayList<>();
        formElements.add(itemNameFormHeader);
        formElements.add(nameElement);
        formElements.add(descriptionElement);
        formElements.add(itemCategoryHeader);
        formElements.add(categoryPicker);
        formElements.add(misellaneousSectionHeader);
        formElements.add(priceEstimateElement);
        formElements.add(locationElement);

        formBuilder.addFormElements(formElements);
    }

    boolean submitForm() {
        if(formBuilder.isValidForm()) {
            postGoodViewModel.setItemNameLiveData(formBuilder.getFormElement(FORM_ELEMENT_NAME).getValue());
            postGoodViewModel.setItemDescriptionLiveData(formBuilder.getFormElement(FORM_ELEMENT_DESCRIPTION).getValue());
            postGoodViewModel.setItemCategoryLiveData(formBuilder.getFormElement(FORM_ELEMENT_CATEGORY).getValue());
            postGoodViewModel.setItemEstimatedPriceLiveData(Integer.parseInt(formBuilder.getFormElement(FORM_ELEMENT_PRICE_ESTIMATE).getValue()));
            postGoodViewModel.setItemLocationLiveData(formBuilder.getFormElement(FORM_ELEMENT_LOCATION).getValue());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post_item_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == android.R.id.home) {
//            getActivity().onBackPressed();
//            return true;
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
        return super.onOptionsItemSelected(item);
    }
}

