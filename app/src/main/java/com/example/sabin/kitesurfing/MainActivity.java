package com.example.sabin.kitesurfing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.Email;
import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private TextView textSample;
    private Button btnSample;
    private String accesToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSample = findViewById(R.id.textsample);
        btnSample = findViewById(R.id.btn_sample);


        //Create a handler for the RetrofitInstance interface
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<User> call = service.getUser(new Email("sabinhantu@gmail.com"));

        //Execute the request asynchronously
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //Toast.makeText(MainActivity.this, "Succes Response", Toast.LENGTH_SHORT).show();
                //Log.i("Response",  "Sabin " + response.body().getResult().getToken());
                accesToken = response.body().getResult().getToken();
                Toast.makeText(MainActivity.this, "TOKEN " + accesToken, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MainActivity.this, "CALL FAILURE", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
