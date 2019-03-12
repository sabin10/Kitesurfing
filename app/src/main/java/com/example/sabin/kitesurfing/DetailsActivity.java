package com.example.sabin.kitesurfing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.SpotDetails;
import com.example.sabin.kitesurfing.service.SpotFavoriteResult;
import com.example.sabin.kitesurfing.service.SpotId;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //widgets
    private androidx.appcompat.widget.Toolbar mainToolbar;
    private TextView countryView;
    private TextView latitudeView;
    private TextView longitudeView;
    private TextView windProbabilityView;
    private TextView whenToGoView;
    private Menu menu;
    private MapView mMapView;

    //vars
    private boolean isFavoriteFromIntent;
    private String token;
    private String spotId;
    private LatLng latLngs;

    //constants
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    //services
    private GetData service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //toolbar
        mainToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().getThemedContext();
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        countryView = findViewById(R.id.details_country);
        latitudeView = findViewById(R.id.details_latitude);
        longitudeView = findViewById(R.id.details_longitude);
        windProbabilityView = findViewById(R.id.details_wind_probability);
        whenToGoView = findViewById(R.id.details_when_to_go);


        Intent intent = getIntent();
        spotId = intent.getStringExtra(SpotRecycleAdapter.ViewHolder.SPOT_ID);
        isFavoriteFromIntent = intent.getBooleanExtra(SpotRecycleAdapter.ViewHolder.IS_FAVORITE, false);
        token = SpotRecycleAdapter.getAccesToken();


        service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<SpotDetails> callGetDetails = service.getSpotDetails(token, new SpotId(spotId));

        callGetDetails.enqueue(new Callback<SpotDetails>() {
            @Override
            public void onResponse(Call<SpotDetails> call, Response<SpotDetails> response) {
                String name = response.body().getResult().getName();
                String country = response.body().getResult().getCountry();
                double latitude = response.body().getResult().getLatitude();
                double longitude = response.body().getResult().getLongitude();
                int windProbability = response.body().getResult().getWindProbability();
                String whenToGo = response.body().getResult().getWhenToGo();
                //boolean isFavorite = response.body().getResult().isFavorite(); //never used

                //adauga in lista lat si long
                LatLng latLng2 = new LatLng(latitude, longitude);
                latLngs = latLng2;

                getSupportActionBar().setTitle(name);
                countryView.setText(country);
                latitudeView.setText(Double.toString(latitude));
                longitudeView.setText(Double.toString(longitude));
                windProbabilityView.setText(Integer.toString(windProbability));
                whenToGoView.setText(whenToGo);

            }

            @Override
            public void onFailure(Call<SpotDetails> call, Throwable t) {
                Log.i("Error", "callGetDetails");
            }
        });

        //map
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = findViewById(R.id.details_map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        this.menu = menu;

        if (isFavoriteFromIntent) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.staronwhite));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.details_menu_favorite) {
            if (isFavoriteFromIntent) {
                // era favorit si trebuie sa devina fals
                isFavoriteFromIntent = false;

                //call remove spot from favorites
                Call<SpotFavoriteResult> callRemoveFromFavorites = service.removeSpotFromFavorites(token, new SpotId(spotId));
                callRemoveFromFavorites.enqueue(new Callback<SpotFavoriteResult>() {
                    @Override
                    public void onResponse(Call<SpotFavoriteResult> call, Response<SpotFavoriteResult> response) {
                        Log.i("RemoveFavorite", "Succes");
                    }

                    @Override
                    public void onFailure(Call<SpotFavoriteResult> call, Throwable t) {
                        Log.i("RemoveFavorite", "Failure");
                    }
                });
            } else {
                //era fals si devine adevarat
                isFavoriteFromIntent = true;

                //call add spot to favorites
                Call<SpotFavoriteResult> callAddToFavorites = service.addSpotToFavorites(token, new SpotId(spotId));
                callAddToFavorites.enqueue(new Callback<SpotFavoriteResult>() {
                    @Override
                    public void onResponse(Call<SpotFavoriteResult> call, Response<SpotFavoriteResult> response) {
                        Log.i("AddFavorite", "Succes");
                    }

                    @Override
                    public void onFailure(Call<SpotFavoriteResult> call, Throwable t) {
                        Log.i("AddFavorite", "Failure");
                    }
                });
            }

            //noua valoare isFavoriteFromIntent
            flipIsFavoriteIcon(isFavoriteFromIntent, item);
        }
        return false;
    }


    public void flipIsFavoriteIcon(boolean newValue, MenuItem item) {
        if (newValue) {
            //cand initial nu e favorit si devine favorit prin apasare
            //newValue = true
            item.setIcon(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.staronwhite));
        } else {
            //cand e favorit si devine nefavorit
            //newValue = false
            item.setIcon(ContextCompat.getDrawable(DetailsActivity.this, R.drawable.staroffwhite));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (map == null || latLngs == null) {
            return;
        }
        double latitude = latLngs.latitude;
        double longitude = latLngs.longitude;
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
