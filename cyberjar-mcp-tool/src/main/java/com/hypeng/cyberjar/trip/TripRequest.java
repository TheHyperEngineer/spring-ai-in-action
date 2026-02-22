package com.hypeng.cyberjar.trip;

public class TripRequest {
    private String query;
    // Getters, Setters, Constructors
    public TripRequest() {}
    public TripRequest(String query) { this.query = query; }
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
}