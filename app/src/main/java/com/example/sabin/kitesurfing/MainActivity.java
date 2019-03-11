package com.example.sabin.kitesurfing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.BackgroundService;
import com.example.sabin.kitesurfing.service.FilterSpot;
import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.Spots;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private androidx.appcompat.widget.Toolbar mainToolbar;
    public static final String TOKEN_KEY = "tokenKey";

    //lista
    RecyclerView spotsListView;
    SpotRecycleAdapter spotRecycleAdapter;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {

            final String accesToken = intent.getStringExtra(TOKEN_KEY);
            String country = intent.getStringExtra(FilterActivity.EXTRA_COUNTRY);
            int windProbability = intent.getIntExtra(FilterActivity.EXTRA_WIND, 0);

            //Log.i("onReceive", "" + country + windProbability);

            //2. lista
            GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
            Call<Spots> callGetAllSpots = service.getAllSpots(accesToken, new FilterSpot(country, windProbability));

            callGetAllSpots.enqueue(new Callback<Spots>() {
                @Override
                public void onResponse(Call<Spots> call, Response<Spots> response) {

                    List<Spots.Result> spots = response.body().getResult();
                    spotRecycleAdapter = new SpotRecycleAdapter(spots, accesToken); //tokenul este trimis in adapter
                    spotsListView.setLayoutManager(new LinearLayoutManager(context));
                    spotsListView.setAdapter(spotRecycleAdapter);

                }

                @Override
                public void onFailure(Call<Spots> call, Throwable t) {
                    Log.i("Lista", "Eroare endpointul 2");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar Kitesurfing App
        mainToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Kitesurfing App");
        getSupportActionBar().getThemedContext();
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        //initializare lista
        spotsListView = findViewById(R.id.spots_list);

        Intent intent = getIntent();
        String country = intent.getStringExtra(FilterActivity.EXTRA_COUNTRY);
        int windProbability = intent.getIntExtra(FilterActivity.EXTRA_WIND, 0);

        //verific daca activitatea a fost pornita by default sau din intentul de la filter
        //trimit country si windProbability in backgroundService ca sa le pot accesa in broadcastReceiver
        if (country == null) {
            //activitatea a pornit by default
            toBackgroundService("", 0);
        } else {
            //activitatea a pornit din FilterActivity
            toBackgroundService(country, windProbability);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        Drawable drawable = menu.getItem(0).getIcon();
        drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_filter) {
            toFilterActivity();
        }
        return false;
    }

    private void toFilterActivity() {
        startActivity(new Intent(MainActivity.this, FilterActivity.class));
    }

    private void toBackgroundService(String country, int windProbability) {
        Intent toBackground = new Intent(MainActivity.this, BackgroundService.class);
        toBackground.putExtra(FilterActivity.EXTRA_COUNTRY, country);
        toBackground.putExtra(FilterActivity.EXTRA_WIND, windProbability);
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
