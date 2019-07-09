package com.example.swap.views.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.swap.R;
import com.example.swap.TempHomePage;
import com.example.swap.daos.UserService;
import com.example.swap.models.User;
import com.example.swap.models.UserRegistrationResponse;
import com.example.swap.utils.Auth;
import com.example.swap.utils.RetrofitFactory;
import com.example.swap.utils.formErrorDisplayer.FormErrorDisplayer;
import com.example.swap.viewmodels.RegisterActivityViewModel;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {

    public static final String CURRENT_USER = "current-user";

    public static final String SIGN_IN_METHOD = "sign_in_method";
    public static final int SIGN_IN_REGULAR = 0;
    public static final int SIGN_IN_GOOGLE = 1;

    public static final String UNPROCESSABLE_ENTITY = "Unprocessable Entity";
    public static final int UNPROCESSABLE_ENTITY_CODE = 422;

    Button register ;
    TextView login;
    EditText firstName, lastName, email , password, confPassword, phoneNumber ;
    Boolean CheckEditText ;
    String FirstNameHolder,LastNameHolder, EmailHolder, PasswordHolder, phoneNumberHolder ;

    RegisterActivityViewModel registerActivityViewModel;
    FormErrorDisplayer formErrorDisplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerActivityViewModel = ViewModelProviders
                .of(this).get(RegisterActivityViewModel.class);

        register = (Button)findViewById(R.id.button);
        login = (TextView) findViewById(R.id.loginText);
        firstName = (EditText)findViewById(R.id.first_name);
        firstName.setText(registerActivityViewModel.getFirstNameLiveData().getValue());
        lastName = (EditText)findViewById(R.id.last_name);
        lastName.setText(registerActivityViewModel.getLastNameLiveData().getValue());
        email = (EditText)findViewById(R.id.email);
        email.setText(registerActivityViewModel.getEmailLiveData().getValue());
        password = (EditText)findViewById(R.id.password);
        password.setText(registerActivityViewModel.getPasswordLiveData().getValue());
        confPassword = (EditText)findViewById(R.id.conf_password);
        confPassword.setText(registerActivityViewModel.getConfirmPasswordLiveData().getValue());
        phoneNumber = (EditText) findViewById(R.id.phone);
        phoneNumber.setText(registerActivityViewModel.getPhoneNumberLiveData().getValue());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(
                        email.getText().toString(),
                        password.getText().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        phoneNumber.getText().toString()
                );
                GetCheckEditTextIsEmptyOrNot();
                if(!CheckEditText){
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else if(!checkPasswords(user.getPassword(), confPassword.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                } else {
                    submitUser(user);
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        formErrorDisplayer = new FormErrorDisplayer.Builder()
                .addErrorDisplayer("first_name", (TextView)findViewById(R.id.error_message_first_name))
                .addErrorDisplayer("email", (TextView)findViewById(R.id.error_message_email))
                .addErrorDisplayer("last_name", (TextView)findViewById(R.id.error_message_last_name))
                .addErrorDisplayer("phone_number", (TextView) findViewById(R.id.error_phone_number))
                .build();

    }

    //handle permissions


    private boolean checkPasswords(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }



    public void GetCheckEditTextIsEmptyOrNot(){

        FirstNameHolder = firstName.getText().toString();
        LastNameHolder = lastName.getText().toString();
        EmailHolder = email.getText().toString();
        PasswordHolder = password.getText().toString();
        phoneNumberHolder = phoneNumber.getText().toString();

        if(TextUtils.isEmpty(FirstNameHolder) || TextUtils.isEmpty(LastNameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder) || TextUtils.isEmpty(phoneNumberHolder))
        {
            CheckEditText = false;
        }
        else {
            CheckEditText = true ;
        }

    }

    public void loginUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistInputToViewModel();
        formErrorDisplayer.clearDisplayers();
    }

    private void persistInputToViewModel() {
        registerActivityViewModel.getFirstNameLiveData()
                .setValue(firstName.getText().toString());
        registerActivityViewModel.getLastNameLiveData()
                .setValue(lastName.getText().toString());
        registerActivityViewModel.getEmailLiveData()
                .setValue(email.getText().toString());
        registerActivityViewModel.getPasswordLiveData()
                .setValue(password.getText().toString());
        registerActivityViewModel.getConfirmPasswordLiveData()
                .setValue(confPassword.getText().toString());
        registerActivityViewModel.getPhoneNumberLiveData()
                .setValue(phoneNumber.getText().toString());
    }

    private void submitUser(User user){
        formErrorDisplayer.clearErrors();
        Retrofit retrofit = RetrofitFactory.create();
        UserService userService = retrofit.create(UserService.class);
        Call<UserRegistrationResponse> call = userService.addUser(user);
        call.enqueue(new Callback<UserRegistrationResponse>() {
            @Override
            public void onResponse(Call<UserRegistrationResponse> call,
                                   Response<UserRegistrationResponse> response) {
                if(response.isSuccessful()) {
                    Auth.of(getApplication()).logIn(response.body().getUser(), Auth.AccountType.SWAP_ACCOUNT);
                    startActivity(new Intent(RegisterActivity.this, TempHomePage.class));
                    finish();
                } else {
                    Toast.makeText(
                            RegisterActivity.this,
                            response.message() + " " + response.code(), Toast.LENGTH_LONG).show();

                    if(response.code() == UNPROCESSABLE_ENTITY_CODE) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("Error Response", errorBody);
                            Map<String, List<String>> errorsMap = new Gson().fromJson(errorBody, Map.class);
                            formErrorDisplayer.displayFormErrors(errorsMap);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
                Toast.makeText(
                        RegisterActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
                t.printStackTrace();
            }
        });

    }

}
