package com.example.swap.views.postgood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swap.R;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;

public class PostItemFormFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

//        getActivity().setTitle("Post Item");
        View view = inflater.inflate(R.layout.fragment_post_item_form, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.post_item_form_recyclerview);
        setUpForm(recyclerView);

        return view;
    }

    private void setUpForm(RecyclerView recyclerView) {
        FormBuilder formBuilder = new FormBuilder(getActivity(), recyclerView);
        FormHeader itemNameFormHeader = FormHeader.createInstance("Item name and description");
        FormElementTextSingleLine nameElement = FormElementTextSingleLine.createInstance()
                .setTitle("Item Name")
                .setHint("Insert item name");
        FormElementTextMultiLine descriptionElement = FormElementTextMultiLine.createInstance()
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
                .setTitle("Select Item Category")
                .setOptions(categories)
                .setPickerTitle("Select item's category");

        FormHeader priceEstimateHeader = FormHeader.createInstance("Item Estimated Price");
        FormElementTextNumber priceEstimateElement = FormElementTextNumber.createInstance()
                .setTitle("Estimated Price")
                .setHint("eg 5000");

        FormHeader locationHeader = FormHeader.createInstance("Item Location");
        FormElementTextMultiLine locationElement = FormElementTextMultiLine.createInstance()
                .setTitle(("Location"));

        List formElements = new ArrayList();
        formElements.add(itemNameFormHeader);
        formElements.add(nameElement);
        formElements.add(descriptionElement);
        formElements.add(itemCategoryHeader);
        formElements.add(categoryPicker);
        formElements.add(priceEstimateHeader);
        formElements.add(priceEstimateElement);
        formElements.add(locationHeader);
        formElements.add(locationElement);


        formBuilder.addFormElements(formElements);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_post_item_form, menu);
    }
}

