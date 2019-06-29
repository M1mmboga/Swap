package com.example.swap.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.swap.R;
import com.example.swap.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.gson.Gson;

public class Auth {
    private static final String CURRENT_USER = "current-user";
    private static final String ACCOUNT_TYPE = "account-type";

    private Application application;
    private static SharedPreferences sharedPreferences;

    public enum AccountType {
        SWAP_ACCOUNT,
        GOOGLE_ACCOUNT
    }

    public Auth(Application application) {
        this.application = application;
    }

    public static Auth of(Application app) {
        return new Auth(app);
    }

    public void logIn(User user, AccountType accountType) {
        putToSharedPreferences(CURRENT_USER, new Gson().toJson(user));
        putToSharedPreferences(ACCOUNT_TYPE, accountType.toString());

        Log.e("Auth", getSharedPrefs().getString(CURRENT_USER, null));
    }

    private void putToSharedPreferences(String key, String value) {
        SharedPreferences sharedPref = getSharedPrefs();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void logout_Swap() {
        clearUserFromSharedPrefs();
    }

    public void logout_GoogleSignIn(GoogleSignInClient client,
                                    OnCompleteListener<Void> onCompleteListener) {
        client.signOut().addOnCompleteListener(onCompleteListener);
    }

    private void clearUserFromSharedPrefs() {
        SharedPreferences sharedPrefs = getSharedPrefs();
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.clear().apply();
    }

    public User getCurrentUser() {
        String userJson = (String)getFromSharedPrefs(CURRENT_USER.toString(), null);
        return new Gson().fromJson(userJson, User.class);
    }

    public String getAccountType() {
        return (String)getFromSharedPrefs(ACCOUNT_TYPE.toString(), null);
    }

    private Object getFromSharedPrefs(String key, String defaultValue) {
        SharedPreferences sharedPrefs = getSharedPrefs();
        return sharedPrefs.getString(key, defaultValue);
    }

    private SharedPreferences getSharedPrefs() {
        if (sharedPreferences == null) {
            sharedPreferences = getContext().getSharedPreferences(
                    getContext().getString(R.string.user_preferences_file), Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    private Context getContext() {
        return application.getApplicationContext();
    }
}
