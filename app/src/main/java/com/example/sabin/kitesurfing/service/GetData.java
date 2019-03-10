package com.example.sabin.kitesurfing.service;

import com.example.sabin.kitesurfing.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetData {

    //get token(user)
    @Headers("Content-Type: application/json")
    @POST("api-user-get")
    Call<User> getUser(@Body Email email);


    //get all spots
    @Headers("Content-Type: application/json")
    @POST("api-spot-get-all")
    Call<Spots> getAllSpots(
            @Header("token") String token,
            @Body FilterSpot filterSpot
    );



}

