package com.example.sabin.kitesurfing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mainToolbar;

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
    }




}
