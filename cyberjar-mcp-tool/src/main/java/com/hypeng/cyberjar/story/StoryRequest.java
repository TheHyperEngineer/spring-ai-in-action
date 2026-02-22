package com.hypeng.cyberjar.story;

public class StoryRequest {
    private String query;

    public StoryRequest() {
    }

    public StoryRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}