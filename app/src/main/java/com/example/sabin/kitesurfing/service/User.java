package com.example.sabin.kitesurfing.service;

import com.google.gson.annotations.SerializedName;

public class User {

    public class Result {
        @SerializedName("token")
        private String token;

        @SerializedName("email")
        private String email;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    private Result result;

    public Result getResult() {
        return result;
    }
}
