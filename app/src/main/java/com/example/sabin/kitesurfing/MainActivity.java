package com.example.sabin.kitesurfing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.BackgroundService;
import com.example.sabin.kitesurfing.service.FilterSpot;
import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.Spots;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView textSample;
    private Button btnSample;
    //public static String accesToken = "";
    public static final String TOKEN_KEY = "tokenKey";



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            
            String accesToken = intent.getStringExtra(TOKEN_KEY);
            textSample.setText("" + accesToken);

            //2. lista
            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
            Call<Spots> callGetAllSpots = service.getAllSpots(accesToken, new FilterSpot());

            callGetAllSpots.enqueue(new Callback<Spots>() {
                @Override
                public void onResponse(Call<Spots> call, Response<Spots> response) {
                    List<Spots.Result> spots = response.body().getResult();
                    for (Spots.Result s : spots) {
                        Log.i("vasile", ""+ s.getName());
                    }

                }

                @Override
                public void onFailure(Call<Spots> call, Throwable t) {

                }
            });


            Log.i("mainSabin", "" + accesToken);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSample = findViewById(R.id.textsample);
        btnSample = findViewById(R.id.btn_sample);

        toBackgroundService();

        //handler pentru interfata GetData
        //GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

        /**
        //1. retrieve token
        Call<User> callGetToken = service.getUser(new Email("sabinhantu@gmail.com"));
        //Execute the request asynchronously
        callGetToken.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                accesToken = response.body().getResult().getToken();
                //Toast.makeText(MainActivity.this, "TOKEN " + accesToken, Toast.LENGTH_SHORT).show();
                RetrofitClient.setToken(accesToken);
                textSample.setText(RetrofitClient.getToken() + " " + response.body().getResult().getEmail());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "CALL FAILURE", Toast.LENGTH_SHORT).show();
            }
        });
         */

        /*
        //2. retrieve list
        Call<Spots> callGetAllSpots = service.getAllSpots(accesToken, new FilterSpot("India"));

        callGetAllSpots.enqueue(new Callback<Spots>() {
            @Override
            public void onResponse(Call<Spots> call, Response<Spots> response) {
                List<Spots.Result> spots = response.body().getResult();

                for (Spots.Result s : spots) {
                    Log.i("vasile", ""+ s.getName());
                }
            }

            @Override
            public void onFailure(Call<Spots> call, Throwable t) {

            }
        });
         */


    }

    private void toBackgroundService() {
        Intent toBackground = new Intent(MainActivity.this, BackgroundService.class);
        startService(toBackground);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(broadcastReceiver, new IntentFilter(BackgroundService.INTENT_SERVICE_MESSAGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(broadcastReceiver);
    }
}
