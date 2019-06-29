package com.example.swap.utils.formErrorDisplayer;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormErrorDisplayer {
    private Map<String, TextView> errorDisplayers = new HashMap<>();
    private Map<String, List<String>> errors = new HashMap<>();

    public void clearDisplayers() {
        errorDisplayers.clear();
    }

    public void clearErrors() {
        for(Map.Entry<String, TextView> entry : errorDisplayers.entrySet()) {
            TextView textView = entry.getValue();
            textView.setText("");
            textView.setVisibility(View.GONE);
        }
        errors.clear();
    }

    public void displayFormErrors(Map<String, List<String>> errors) {
        display(errors);
    }

    public void display() {
        Log.e("Errors to display", this.errors.toString());
        display(this.errors);
    }

    public void addError(String displayerName, String error) {
        if(this.errors.containsKey(displayerName)) {
            this.errors.get(displayerName).add(error);
        } else {
            this.errors.put(displayerName, new ArrayList<String>(Arrays.asList(error)));
        }
    }

    private void display(Map<String, List<String>> errors) {
        for(Map.Entry<String, List<String>> entry : errors.entrySet()) {
            if(errorDisplayers.containsKey(entry.getKey())) {
                TextView errorDisplayerTv = errorDisplayers.get(entry.getKey());
                List<String> errorsMessages = entry.getValue();
                displayFieldErrors(errorDisplayerTv, errorsMessages);
            }
        }
    }

    private void displayFieldErrors(TextView field, List<String> errors) {
        for(String error : errors) {
            setErrors(field, error);
            field.setVisibility(View.VISIBLE);
        }
    }

    private void setErrors(TextView field, String error) {
        if(!field.getText().toString().isEmpty()) {
            field.append("\n" + error);
        } else {
            field.setText(error);
        }
    }

    public static class Builder {
        private FormErrorDisplayer formErrorDisplayer =
                new FormErrorDisplayer();

        public Builder addErrorDisplayer(String displayerName, TextView displayer) {
            formErrorDisplayer.errorDisplayers.put(displayerName, displayer);
            return this;
        }

        public FormErrorDisplayer build() {
            return formErrorDisplayer;
        }
    }

}
