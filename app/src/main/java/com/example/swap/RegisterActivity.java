package com.example.swap;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.swap.daos.UserService;
import com.example.swap.models.User;
import com.example.swap.models.UserRegistrationResponse;
import com.example.swap.utils.RetrofitFactory;
import com.example.swap.viewmodels.RegisterActivityViewModel;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterActivity extends AppCompatActivity {

    public static final String CURRENT_USER = "current-user";

    Button register ;
    TextView login;
    EditText firstName, lastName, email , password, confPassword ;
    Boolean CheckEditText ;
    String FirstNameHolder,LastNameHolder, EmailHolder, PasswordHolder ;

    RegisterActivityViewModel registerActivityViewModel;

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(
                        email.getText().toString(),
                        password.getText().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString()
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

        if(TextUtils.isEmpty(FirstNameHolder) || TextUtils.isEmpty(LastNameHolder) || TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {

            CheckEditText = false;

        }
        else {

            CheckEditText = true ;
        }

    }

//    public void SendDataToServer(final String firstName, final String lastName, final String email, final String password){
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                String QuickFirstName = firstName ;
//                String QuickLastName = lastName;
//                String QuickEmail = email ;
//                String QuickPassword = password;
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("firstname", QuickFirstName));
//                nameValuePairs.add(new BasicNameValuePair("lastname", QuickLastName));
//                nameValuePairs.add(new BasicNameValuePair("email", QuickEmail));
//                nameValuePairs.add(new BasicNameValuePair("password", QuickPassword));
//
//                try {
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(RegisterURL);
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return "Data Submit Successfully";
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
//
//                Toast.makeText(RegisterActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(firstName, lastName, email, password);
//    }


    public void loginUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        persistInputToViewModel();
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
    }

    private void submitUser(User user){
        Retrofit retrofit = RetrofitFactory.create();
        UserService userService = retrofit.create(UserService.class);
        Call<UserRegistrationResponse> call = userService.addUser(user);
        call.enqueue(new Callback<UserRegistrationResponse>() {
            @Override
            public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                if(response.isSuccessful()) {
                    SharedPreferences sharedPref = getSharedPreferences(
                            getString(R.string.preferences_file), MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(CURRENT_USER, new Gson().toJson(response.body().getUser()));
                    editor.apply();
                    Toast.makeText(
                            RegisterActivity.this,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                    ).show();
                    startActivity(new Intent(RegisterActivity.this, TempHomePage.class));
                } else {
                    Toast.makeText(
                            RegisterActivity.this,
                            response.message(),
                            Toast.LENGTH_LONG
                    ).show();
                    Log.e("bleble", response.raw().request().url().toString());
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
