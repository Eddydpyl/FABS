package com.eduardo.fabs.models;

/**
 * Created by Eduardo on 02/08/2016.
 */

import android.database.Cursor;

import com.eduardo.fabs.fetch.FetchMovies;
import com.eduardo.fabs.utils.Constants;
import com.eduardo.fabs.utils.UserCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private Double popularity;
    private int vote_count;
    private Boolean video;
    private Double vote_average;

    private UserCategory userCategory;
    private Double userRating;

    public MovieModel() {}

    public MovieModel(String poster_path, String overview, String release_date, String id, String title, Double popularity, Double vote_average) {
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.id = id;
        this.title = title;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }

    public MovieModel(String poster_path, Boolean adult, String overview, String release_date, List<Integer> genre_ids, String id, String original_title, String original_language, String title, String backdrop_path, Double popularity, int vote_count, Boolean video, Double vote_average) {
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
    }

    public MovieModel(String poster_path, Boolean adult, String overview, String release_date, List<Integer> genre_ids, String id, String original_title, String original_language, String title, String backdrop_path, Double popularity, int vote_count, Boolean video, Double vote_average, UserCategory userCategory, Double userRating) {
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
        this.userCategory = userCategory;
        this.userRating = userRating;
    }

    public MovieModel(JSONObject movieJson) throws JSONException{
        this.id = String.valueOf(movieJson.getInt(FetchMovies.ID));
        this.title = movieJson.getString(FetchMovies.TITLE);
        this.overview = movieJson.getString(FetchMovies.OVERVIEW);
        this.poster_path = movieJson.getString(FetchMovies.POSTER_IMAGE);
        this.release_date = movieJson.getString(FetchMovies.RELEASE_DATE);
        this.popularity = movieJson.getDouble(FetchMovies.POPULARITY);
        this.vote_average = movieJson.getDouble(FetchMovies.VOTE_AVERAGE);
        this.adult = movieJson.getBoolean(FetchMovies.ADULT);
        JSONArray genres = movieJson.getJSONArray(FetchMovies.GENRES);
        List<Integer> genresIDs = new ArrayList<Integer>();
        for(int i = 0; i < genres.length(); i++){
            Integer integer = genres.getJSONObject(i).getInt(FetchMovies.GENRES_ID);
            genresIDs.add(integer);
        }
        this.genre_ids = genresIDs;
        this.original_title = movieJson.getString(FetchMovies.ORIGINAL_TITLE);
        this.original_language = movieJson.getString(FetchMovies.ORIGINAL_LANGUAGE);
        this.backdrop_path = movieJson.getString(FetchMovies.BACKDROP_PATH);
        this.vote_count = movieJson.getInt(FetchMovies.VOTE_COUNT);
        this.video = movieJson.getBoolean(FetchMovies.VIDEO);
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

    public String getReleaseDate() {
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

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
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

    public Double getVoteAverage() {
        return vote_average;
    }

    public void setVoteAverage(Double vote_average) {
        this.vote_average = vote_average;
    }

    public String getImageFullURL() {
        return Constants.TMDBConstants.IMAGE_BASE_URL + Constants.TMDBConstants.IMAGE_SMALL_SIZE + getPosterPath();
    }

    public String getRating() {
        return getVoteAverage() + "/" + Constants.TMDBConstants.RATING_MAX;
    }

    public Date getMovieReleaseDate() throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(getReleaseDate());
        return date;
    }
}