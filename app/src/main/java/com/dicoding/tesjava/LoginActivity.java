package com.dicoding.tesjava;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dicoding.tesjava.API.APIInterface;
import com.dicoding.tesjava.Model.Login;
import com.dicoding.tesjava.Model.RegisterUser;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class LoginActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://18.139.50.74:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private TextInputLayout mUsername, mPassword;
    private String usernameInput, passwordInput, token;
    private Button signIn;
    APIInterface apiInterface = retrofit.create(APIInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsername = findViewById(R.id.usernameLogin);
        mPassword = findViewById(R.id.passwordLogin);

        signIn = findViewById(R.id.button_login);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        final JSONObject jsonObject = new JSONObject();

        this.usernameInput = mUsername.getEditText().getText().toString();
        this.passwordInput = mPassword.getEditText().getText().toString();
        try{
            jsonObject.put("password", passwordInput);
            jsonObject.put("username", usernameInput);
        }catch (JSONException e) {
            e.printStackTrace();
        }

        Call<Login> call= apiInterface.login(
                jsonObject
        );
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.body()!=null){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    token = response.body().getData().getToken();
                    editor.putString("username",usernameInput);
                    editor.putString("token",token);
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d("response","klik4");
                String message = t.getMessage();
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(LoginActivity.this,"Connection time out  " + message,Toast.LENGTH_LONG ).show();
                }else if (t instanceof IOException){
                    Toast.makeText(LoginActivity.this,"Time Out  " + message,Toast.LENGTH_LONG ).show();
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
