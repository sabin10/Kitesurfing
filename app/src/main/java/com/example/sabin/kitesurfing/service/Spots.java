package com.example.sabin.kitesurfing.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Spots {

    //@SerializedName("result")
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public class Result {
        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("country")
        private String country;

        @SerializedName("whenToGo")
        private String whenToGo;

        @SerializedName("isFavorite")
        private boolean isFavorite;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getWhenToGo() {
            return whenToGo;
        }

        public void setWhenToGo(String whenToGo) {
            this.whenToGo = whenToGo;
        }

        public boolean isFavorite() {
            return isFavorite;
        }

        public void setFavorite(boolean favorite) {
            isFavorite = favorite;
        }
    }
}
