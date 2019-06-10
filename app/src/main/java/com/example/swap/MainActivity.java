package com.example.swap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.swap.R;
import com.example.swap.daos.GoodsDao;
import com.example.swap.models.Good;
import com.example.swap.models.dtos.GoodDTO;
import com.example.swap.rest.RetrofitFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    ImageView mImageView;
    Button mChooseButton;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText itemName = (EditText) findViewById(R.id.itemNameEdit);
        EditText itemDescriptionText = (EditText) findViewById(R.id.itemDescriptionEdit);
        EditText itemLocationText = (EditText) findViewById(R.id.sellerLocationEdit);
        EditText itemPriceText = (EditText) findViewById(R.id.valuedPriceEdit);

        Spinner spinner = (Spinner) findViewById(R.id.categoriesSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);







        //views
        mImageView = findViewById(R.id.itemImage);
        mChooseButton = findViewById(R.id.selectImageButton);

        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        //permission not granted request it
                        String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                        //POP up for permissions
                        requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else
                        {
                            //permission granted
                            pickImageFromGallery();
                        }
                }

            }
        });

        //submit item details
        Button submitButton = (Button) findViewById(R.id.submitItem);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = spinner.getSelectedItem().toString();
                String nameOfItem = itemName.getText().toString();
                String descriptionOfItem = itemDescriptionText.getText().toString();
                String locationOfItem = itemLocationText.getText().toString();
                String priceOfItem = itemPriceText.getText().toString();

                if (category == null || nameOfItem == null ||
                        descriptionOfItem == null || locationOfItem == null ||
                        priceOfItem == null)
                {
                      Toast.makeText(
                              MainActivity.this,
                              "Please complete item upload information",
                              Toast.LENGTH_SHORT).show();
                }
                else{
                    GoodDTO goodDTO = new GoodDTO(
                            category, nameOfItem,
                            descriptionOfItem, locationOfItem,
                            priceOfItem
                    );
                    Retrofit retrofit = RetrofitFactory.create();
                    GoodsDao goodsDao = retrofit.create(GoodsDao.class);
                    Call<Good> good = goodsDao.addGood(goodDTO);
                }


            }


        });




    }
    private void pickImageFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission granted
                    pickImageFromGallery();
                }
                else
                {
                    //permission denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //what to do to selected image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE )
        {
            //set image to imageview
            mImageView.setImageURI(data.getData());
        }
    }




}
