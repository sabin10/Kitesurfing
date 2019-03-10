package com.example.sabin.kitesurfing;

import android.app.Application;

public class Global extends Application {

    private String accesToken;

    public String getAccesToken() {
        return accesToken;
    }

    public void setAccesToken(String accesToken) {
        this.accesToken = accesToken;
    }
}
