package com.example.swap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swap.views.authentication.LoginActivity;


public class ProfileActivity extends AppCompatActivity {

    TextView textView ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textView = (TextView)findViewById(R.id.textviewprofile);

        button = (Button)findViewById(R.id.button) ;

        Intent intent = getIntent();

//        String username = intent.getStringExtra(LoginActivity.UserEmail);

//        textView.setText(username);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);

                startActivity(intent);

                Toast.makeText(ProfileActivity.this, "Log Out Successfully", Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBackPressed() {

        Toast.makeText(ProfileActivity.this, "Please Click on Log Out button .", Toast.LENGTH_LONG).show();

        return;
    }
}

