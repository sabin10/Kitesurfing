package com.example.sabin.kitesurfing.service;

public class SpotDetails {

    private Result result;

    public Result getResult() {
        return result;
    }

    public class Result {
        private String id;
        private String name;
        private double longitude;
        private double latitude;
        private int windProbability;
        private String country;
        private String whenToGo;
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

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public int getWindProbability() {
            return windProbability;
        }

        public void setWindProbability(int windProbability) {
            this.windProbability = windProbability;
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
