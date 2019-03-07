package com.example.sabin.kitesurfing.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetData {

    @Headers("Content-Type: application/json")
    @POST("api-user-get")
    Call<User> getUser(@Body Email email);


}

