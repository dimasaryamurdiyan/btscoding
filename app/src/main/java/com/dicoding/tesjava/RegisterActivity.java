package com.dicoding.tesjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dicoding.tesjava.API.APIInterface;
import com.dicoding.tesjava.Model.RegisterUser;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://18.139.50.74:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private TextInputLayout mUsername, mEmail, mPassword;
    private String usernameInput, emailInput, passwordInput;
    private Button signUp;
    APIInterface apiInterface = retrofit.create(APIInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.Email);

        signUp = findViewById(R.id.button_register);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("response","klik1");
                register();
            }
        });
    }

    private void register() {
        Log.d("response","klik2");
        final JSONObject jsonObject = new JSONObject();

        this.usernameInput = mUsername.getEditText().getText().toString();
        this.passwordInput = mPassword.getEditText().getText().toString();
        this.emailInput = mEmail.getEditText().getText().toString().trim();
        try{
            jsonObject.put("email", emailInput);
            jsonObject.put("password", passwordInput);
            jsonObject.put("username", usernameInput);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        Call<RegisterUser> call= apiInterface.registration(
                jsonObject
        );
        call.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                Log.d("response","klik3");
                if (response.body()!=null){
                    SharedPreferences sharedPreferences =
                            getSharedPreferences("UserData",MODE_PRIVATE);
                    if(sharedPreferences.getString("username",null)!=null){
                        sharedPreferences.edit()
                                .clear()
                                .commit();
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putString("username",usernameInput);
                        editor.putString("email",emailInput);

                        editor.commit();
                    }else {
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putString("username",usernameInput);
                        editor.putString("email",emailInput);

                        editor.commit();
                    }
                    Toast.makeText(RegisterActivity.this,"Akun sudah terdaftar.",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(RegisterActivity.this,"Upps Something Wrong " + response.body().getStatusCode(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                Log.d("response","klik4");
                String message = t.getMessage();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(RegisterActivity.this,"Connection time out  " + message,Toast.LENGTH_LONG ).show();
                }else if (t instanceof IOException){
                    Toast.makeText(RegisterActivity.this,"Time Out  " + message,Toast.LENGTH_LONG ).show();
                }else {
                    //Call was cancelled by user
                    if(call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                        Log.d("response","call cancelled");
                    }
                    else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                        Log.d("response","network error:: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }
}
