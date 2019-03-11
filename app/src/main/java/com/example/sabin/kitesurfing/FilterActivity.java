package com.example.sabin.kitesurfing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mainToolbar;
    private Button filterBtn;
    public static final String EXTRA_COUNTRY = "extraCountry";
    public static final String EXTRA_WIND = "extraWind";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //toolbar Kitesurfing App
        mainToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Filter");
        getSupportActionBar().getThemedContext();
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));


        filterBtn = findViewById(R.id.btn_filter);

        //to do: transmite country si filter catre MainActivity
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFilterToMain();
            }
        });
    }

    public void sendFilterToMain() {
        EditText countryView = findViewById(R.id.filter_country);
        String country = countryView.getText().toString();

        EditText windView = findViewById(R.id.filter_wind_probability);
        int windProbability = Integer.parseInt(windView.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_COUNTRY, country);
        intent.putExtra(EXTRA_WIND, windProbability);

        startActivity(intent);
    }





}
