package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

