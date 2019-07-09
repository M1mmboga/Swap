package com.example.swap.views.authentication;

//import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.swap.R;
import com.example.swap.TempHomePage;
import com.example.swap.models.User;
import com.example.swap.utils.Auth;
import com.example.swap.utils.formErrorDisplayer.FormErrorDisplayer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 12;
    private static final int UNPROCESSABLE_ENTITY_HTTP_CODE = 422;
    Button login ;
    ProgressDialog progressDialog;
    EditText email, password;
    ProgressBar progressBar;

    GoogleSignInClient mGoogleSignInClient;
    LoginViewModel loginViewModel;

    FormErrorDisplayer formErrorDisplayer;

    private Class activityAfterLogin = TempHomePage.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.user_preferences_file), MODE_PRIVATE);
        if(sharedPreferences.getString(RegisterActivity.CURRENT_USER, null) != null) {
            startActivity(new Intent(this, TempHomePage.class));
            finish();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        login = (Button)findViewById(R.id.button);
        login.setOnClickListener(v -> handleSwapSignIn());
        email = (EditText) findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        progressBar = findViewById(R.id.login_google_sign_in_progressbar);

        SignInButton signInButton = findViewById(R.id.google_sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(v -> signIn());

        formErrorDisplayer = new FormErrorDisplayer.Builder()
                .addErrorDisplayer("email", (TextView)findViewById(R.id.error_message_login_email))
                .addErrorDisplayer("password", (TextView)findViewById(R.id.error_message_login_password))
                .addErrorDisplayer("login", (TextView)findViewById(R.id.error_message_login_password))
                .build();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(this::handleGoogleSignIn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            if(resultCode == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleGoogleSignIn(task);
            } else {
                Log.e("LoginActivity", "Result not OK");
            }
        }
    }

    private void handleGoogleSignIn(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
            String idToken = googleSignInAccount.getIdToken();
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            loginViewModel.sendIdToken(googleSignInAccount.getIdToken(), new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()) {
                        Auth.of(getApplication()).logIn(response.body(), Auth.AccountType.GOOGLE_ACCOUNT);
                        startActivity(new Intent(LoginActivity.this, activityAfterLogin));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        Log.e("Login Activity", response.raw().request().url().toString());
                        try {
                            Log.e("Error Body", response.errorBody().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Login OnFailure", t.getMessage());
                    t.printStackTrace();
                }
            });
        } catch (ApiException e) {
            e.printStackTrace();
            Log.e("LoginActivity", "Google sign in failed. Code: " + e.getStatusCode());
        }
    }

    private void handleSwapSignIn() {
        formErrorDisplayer.clearErrors();

        boolean isValid = true;
        if(email.getText().toString().isEmpty()) {
            isValid = false;
            formErrorDisplayer.addError("email", getString(R.string.required_validation_error));
        }
        if(password.getText().toString().isEmpty()) {
            isValid = false;
            formErrorDisplayer.addError("password", getString(R.string.required_validation_error));
        }

        if(!isValid) {
            formErrorDisplayer.display();
            return;
        }
        loginViewModel.doSwapLogin(email.getText().toString(), password.getText().toString(), new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    Auth.of(getApplication()).logIn(response.body(), Auth.AccountType.SWAP_ACCOUNT);
                    startActivity(new Intent(LoginActivity.this, activityAfterLogin));
                    finish();
                } else {
//                    Log.e("Swap Login", response.message());
                    if(response.code() == UNPROCESSABLE_ENTITY_HTTP_CODE) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("Error Response", errorBody);
                            Log.e("Error Message", response.message());
                            Map<String, List<String>> errors = new Gson().fromJson(errorBody, Map.class);
                            formErrorDisplayer.displayFormErrors(errors);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //action listener to register activity
    public void goToRegistration(View view) {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}