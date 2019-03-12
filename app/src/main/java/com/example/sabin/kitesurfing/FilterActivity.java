package com.example.sabin.kitesurfing;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.SpotCountries;

import java.util.List;

public class FilterActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mainToolbar;
    private Button filterBtn;
    private EditText countryView;
    private EditText windView;

    public static final String EXTRA_COUNTRY = "extraCountry";
    public static final String EXTRA_WIND = "extraWind";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //toolbar
        mainToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Filter");
        getSupportActionBar().getThemedContext();
        mainToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        countryView = findViewById(R.id.filter_country);
        windView = findViewById(R.id.filter_wind_probability);
        filterBtn = findViewById(R.id.btn_filter);

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countryView.getText().toString().matches("") && windView.getText().toString().matches("")) {
                    Toast.makeText(FilterActivity.this, "You need to have at least one filter parameter", Toast.LENGTH_LONG).show();
                } else if (countryView.getText().toString().matches("")) {
                    //doar windProbability
                    int windProbability = Integer.parseInt(windView.getText().toString());
                    if (!checkForWindInput(windProbability)) {
                        Toast.makeText(FilterActivity.this, "Wind Probability is not valid. Try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    sendFilterToMain("", windProbability);
                } else if (windView.getText().toString().matches("")) {
                    //doar country
                    checkForCountryInput(countryView, 0);
                } else {
                    //ambele
                    int windProbability = Integer.parseInt(windView.getText().toString());
                    if (!checkForWindInput(windProbability)) {
                        Toast.makeText(FilterActivity.this, "Wind Probability is not valid. Try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                    checkForCountryInput(countryView, windProbability);
                }
            }
        });
    }

    public void checkForCountryInput(EditText countryView, final int windProbability) {
        String token = SpotRecycleAdapter.getAccesToken();

        final String country = countryView.getText().toString();
        GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);
        Call<SpotCountries> callGetCountries = service.getSpotCountries(token);

        callGetCountries.enqueue(new Callback<SpotCountries>() {
            @Override
            public void onResponse(Call<SpotCountries> call, Response<SpotCountries> response) {

                List<String> countries = response.body().getResult();
                boolean countryInputOk = false;

                for (String countryElement : countries) {
                    if (countryElement.trim().toLowerCase().contains(country.trim().toLowerCase())) {
                        countryInputOk = true;
                        break; //nu are rost sa continuam cu loopul
                    }
                }

                if (countryInputOk) {
                    //primul caracter din stringul country trebuie sa fie uppercase
                    if (Character.isUpperCase(country.charAt(0))) {
                        //e bine
                        sendFilterToMain(country, windProbability);
                    } else {
                        // il fac uppercase
                        sendFilterToMain(makeFirstLetterUppercase(country), windProbability);
                    }
                } else {
                    Toast.makeText(FilterActivity.this,
                            "Your input is not valid or this country does not have kite surfing",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SpotCountries> call, Throwable t) {
                Log.i("Error", "callGetCountries");
            }
        });

    }

    public boolean checkForWindInput(int windProbability) {
        if (windProbability < 0 || windProbability > 100) {
            return false;
        }
        return true;
    }

    public void sendFilterToMain(String country, int windProbability) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_COUNTRY, country);
        intent.putExtra(EXTRA_WIND, windProbability);
        startActivity(intent);
    }

    public String makeFirstLetterUppercase(String word) {
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

}
