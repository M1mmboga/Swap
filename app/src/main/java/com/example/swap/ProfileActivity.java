package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swap.models.User;
import com.example.swap.utils.Auth;


public class ProfileActivity extends AppCompatActivity {

    TextView myUsername, myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        myUsername = (TextView) findViewById(R.id.username);
        myEmail = (TextView) findViewById(R.id.email);

        User user = Auth.of(getApplication()).getCurrentUser();
        myUsername.setText(user.getFirstname()+' '+user.getLastname());
        myEmail.setText(user.getEmail());
    }




}

