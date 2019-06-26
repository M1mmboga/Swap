package com.example.swap.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.RecursiveTask;

public class RegisterActivityViewModel extends ViewModel {

    MutableLiveData<String> firstNameLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> lastNameLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> emailLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> passwordLiveData =
            new MutableLiveData<>();
    MutableLiveData<String> confirmPasswordLiveData =
            new MutableLiveData<>();

    public MutableLiveData<String> getConfirmPasswordLiveData() {
        return confirmPasswordLiveData;
    }

    public MutableLiveData<String> getFirstNameLiveData() {
        return firstNameLiveData;
    }

    public MutableLiveData<String> getLastNameLiveData() {
        return lastNameLiveData;
    }

    public MutableLiveData<String> getEmailLiveData() {
        return emailLiveData;
    }

    public MutableLiveData<String> getPasswordLiveData(){
        return passwordLiveData;
    }
}
