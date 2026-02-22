package com.hypeng.cyberjar.trip;

public class TripPlanResponse {
    private String finalItinerary;
    // Getters, Setters, Constructors
    public TripPlanResponse() {}
    public TripPlanResponse(String finalItinerary) { this.finalItinerary = finalItinerary; }
    public String getFinalItinerary() { return finalItinerary; }
    public void setFinalItinerary(String finalItinerary) { this.finalItinerary = finalItinerary; }
}