package com.example.sabin.kitesurfing;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.SpotDetails;
import com.example.sabin.kitesurfing.service.SpotId;

public class DetailsActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mainToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //toolbar Kitesurfing App
        mainToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        //getSupportActionBar().setTitle("Filter");
        getSupportActionBar().getThemedContext();
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        Intent intent = getIntent();
        String spotId = intent.getStringExtra(SpotRecycleAdapter.ViewHolder.SPOT_ID);
        String token = SpotRecycleAdapter.getAccesToken();

        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<SpotDetails> callGetDetails = service.getSpotDetails(token, new SpotId(spotId));

        callGetDetails.enqueue(new Callback<SpotDetails>() {
            @Override
            public void onResponse(Call<SpotDetails> call, Response<SpotDetails> response) {
                String name = response.body().getResult().getName();
                Toast.makeText(DetailsActivity.this, "" + name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SpotDetails> call, Throwable t) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);

        //Drawable drawable = menu.getItem(0).getIcon();
        //drawable.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        return true;
    }
}
