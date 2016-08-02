package com.eduardo.fabs.models;

/**
 * Created by Eduardo on 02/08/2016.
 */

import android.database.Cursor;

import com.eduardo.fabs.utils.Constants;
import com.eduardo.fabs.utils.UserCategory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovieModel {

    private String poster_path;
    private Boolean adult;
    private String overview;
    private String release_date;
    private List<Integer> genre_ids;
    private String id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private Float popularity;
    private int vote_count;
    private Boolean video;
    private Float vote_average;

    private Boolean userEdited;
    private UserCategory userCategory;
    private Double userRating;

    public MovieModel(String poster_path, Boolean adult, String overview, String release_date, List<Integer> genre_ids, String id, String original_title, String original_language, String title, String backdrop_path, Float popularity, int vote_count, Boolean video, Float vote_average) {
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
        this.userEdited = false;
        this.userCategory = null;
        this.userRating = null;
    }

    public MovieModel(String poster_path, Boolean adult, String overview, String release_date, List<Integer> genre_ids, String id, String original_title, String original_language, String title, String backdrop_path, Float popularity, int vote_count, Boolean video, Float vote_average, UserCategory userCategory, Double userRating) {
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
        this.userEdited = true;
        this.userCategory = userCategory;
        this.userRating = userRating;
    }

    // TODO: Constructor from cursor
    public MovieModel(Cursor cursor) {
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
        this.userEdited = userEdited;
        this.userCategory = userCategory;
        this.userRating = userRating;
    }

    // TODO: Constructor from JSON
    public MovieModel(JSONObject jsonObject) throws JSONException{
        this.poster_path = poster_path;
        this.adult = adult;
        this.overview = overview;
        this.release_date = release_date;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_title = original_title;
        this.original_language = original_language;
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.video = video;
        this.vote_average = vote_average;
        this.userEdited = userEdited;
        this.userCategory = userCategory;
        this.userRating = userRating;
    }

    public Boolean getUserEdited() {
        return userEdited;
    }

    public void setUserEdited(Boolean userEdited) {
        this.userEdited = userEdited;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public UserCategory getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public List<Integer> getGenreIds() {
        return genre_ids;
    }

    public void setGenreIds(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return original_title;
    }

    public void setOriginalTitle(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginalLanguage() {
        return original_language;
    }

    public void setOriginalLanguage(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public void setBackdropPath(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Float getPopularity() {
        return popularity;
    }

    public void setPopularity(Float popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return vote_count;
    }

    public void setVoteCount(int vote_count) {
        this.vote_count = vote_count;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Float getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(Float vote_average) {
        this.vote_average = vote_average;
    }

    public String getImageFullURL() {
        return Constants.MovieConstants.IMAGE_BASE_URL + Constants.MovieConstants.IMAGE_SMALL_SIZE + getPosterPath();
    }

    public String getRating() {
        return getVoteAverage() + "/" + Constants.MovieConstants.RATING_MAX;
    }

    public String getMovieReleaseDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-dd-mm");
        String date = "";
        try {
            Date newDate = format.parse(getRelease_date());
            format = new SimpleDateFormat("MMM dd, yyyy");
            date = format.format(newDate);
        } catch (ParseException e) {

        }
        return date;
    }
}