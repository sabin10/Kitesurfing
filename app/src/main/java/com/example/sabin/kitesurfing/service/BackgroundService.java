package com.example.sabin.kitesurfing.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sabin.kitesurfing.FilterActivity;
import com.example.sabin.kitesurfing.Global;
import com.example.sabin.kitesurfing.MainActivity;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundService extends IntentService {

    public static final String INTENT_SERVICE_MESSAGE = "IntentServiceMessage";
    public static final String EMAIL_STRING = "sabinhantu@gmail.com";

    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {

        final String country = intent.getStringExtra(FilterActivity.EXTRA_COUNTRY);
        final int windProbability = intent.getIntExtra(FilterActivity.EXTRA_WIND, 0);

        GetData service = RetrofitClient.getRetrofitInstance()
                .create(GetData.class);
        Call<User> callGetToken = service.getUser(new Email(EMAIL_STRING));

        //asynchron
        callGetToken.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String accesToken = response.body().getResult().getToken();
                sendTokenToActivity(accesToken, country, windProbability);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void sendTokenToActivity(String tokenValue, String country, int windProbability) {
        Intent intent = new Intent(INTENT_SERVICE_MESSAGE);
        intent.putExtra(MainActivity.TOKEN_KEY, tokenValue);
        intent.putExtra(FilterActivity.EXTRA_COUNTRY, country);
        intent.putExtra(FilterActivity.EXTRA_WIND, windProbability);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);

    }

}
