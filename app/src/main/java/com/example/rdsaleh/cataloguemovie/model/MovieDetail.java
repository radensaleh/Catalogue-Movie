package com.example.rdsaleh.cataloguemovie.model;

public class MovieDetail {

    private String id;
    private String title;
    private String poster_path;
    private String overview;
    private String original_language;
    private String release_date;
    private String tagline;
    private String vote_average;
    private int budget;
    private int runtime;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTagline() {
        return tagline;
    }

    public String getVote_average() {
        return vote_average;
    }

    public int getBudget() {
        return budget;
    }

    public int getRuntime() {
        return runtime;
    }
}
