package com.dicoding.tesjava.API;

import com.dicoding.tesjava.Model.Login;
import com.dicoding.tesjava.Model.RegisterUser;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {
    @FormUrlEncoded
    @POST("register")
    Call<RegisterUser> registration(
            @Field("requestDto") JSONObject requestDto
    );
    @FormUrlEncoded
    @POST("login")
    Call<Login> login(
            @Field("loginDto") JSONObject loginDto
    );
}
