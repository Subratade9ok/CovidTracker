package com.subrata.covidtracker.api;

import java.util.Map;

public class CountryData {
    private String updated;
    private String country;
    private String cases;
    private String todayCases;
    private String deaths;
    private String todayDeaths;
    private String recovered;
    private String todayRecovered;
    private String active;
    private String tests;
    private String critical;
    private Map<String, String> countryInfo;
    private String population;
    private String continent;

    public CountryData(String updated, String country, String cases, String todayCases, String deaths, String todayDeaths, String recovered, String todayRecovered, String active, String tests, String critical, Map<String, String> countryInfo, String population, String continent) {
        this.updated = updated;
        this.country = country;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.todayRecovered = todayRecovered;
        this.active = active;
        this.tests = tests;
        this.critical = critical;
        this.countryInfo = countryInfo;
        this.population = population;
        this.continent = continent;
    }

    public String getUpdated() {
        return updated;
    }

    public String getCountry() {
        return country;
    }

    public String getCases() {
        return cases;
    }

    public String getTodayCases() {
        return todayCases;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getTodayDeaths() {
        return todayDeaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getTodayRecovered() {
        return todayRecovered;
    }

    public String getActive() {
        return active;
    }

    public String getTests() {
        return tests;
    }

    public String getCritical() {
        return critical;
    }

    public Map<String, String> getCountryInfo() {
        return countryInfo;
    }

    public String getPopulation() {
        return population;
    }

    public String getContinent() {
        return continent;
    }
}
