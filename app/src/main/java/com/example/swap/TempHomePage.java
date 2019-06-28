package com.example.swap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swap.models.User;
import com.google.gson.Gson;

public class TempHomePage extends AppCompatActivity implements View.OnClickListener {

    private Button booksCategoryBtn;
    private Button clothesCategoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_homepage);

        booksCategoryBtn = (Button) findViewById(R.id.books_category_btn);
        booksCategoryBtn.setOnClickListener(this);
        clothesCategoryBtn = (Button) findViewById(R.id.clothes_category_btn);
        clothesCategoryBtn.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preferences_file), MODE_PRIVATE);
        String userJson = sharedPreferences.getString(RegisterActivity.CURRENT_USER, null);
        Log.e("User in JSON", userJson);
        Gson gson = new Gson();
        User user = gson.fromJson(userJson, User.class);
        Toast.makeText(
                this,
                "first name: " + user.getFirstname(),
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onClick(View v) {
        if(v instanceof Button) {
            Intent i = new Intent(this, ListActivity.class);
            i.putExtra("category", ((Button)v).getText());
            startActivity(i);
        }
    }
}

