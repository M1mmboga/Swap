package com.johngachihi.swap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TestViewModel extends ViewModel {
    private MutableLiveData<String> testText;

    public TestViewModel() {
        testText = new MutableLiveData<>();
    }

    public MutableLiveData<String> getTestText() {
        return testText;
    }

    public void setTestText(String text) {
        testText.setValue(text);
    }
}
