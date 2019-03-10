package com.example.sabin.kitesurfing.service;

public class FilterSpot {
    final String country;
    final int windProbability;

    //fara filtru
    //afiseaza toate posibilitatile din lume
    public FilterSpot() {
        this.country = "";
        this.windProbability = 0;
    }

    //filtru doar la tara
    //afiseaza totul dintr o anumita tara
    public FilterSpot(String country) {
        this.country = country;
        this.windProbability = 0;
    }

    //filtru doar la windProbability
    //afiseaza toate locurile din lume cu windProbability intre [windProbability, 100]
    public  FilterSpot(int windProbability) {
        this.windProbability = windProbability;
        this.country = "";
    }

    //filtru si pentru tara si pentru windProbability
    //afiseaza locurile doar dintr o tara anume cu un windProbability anume
    public FilterSpot(String country, int windProbability) {
        this.country = country;
        this.windProbability = windProbability;
    }


}
