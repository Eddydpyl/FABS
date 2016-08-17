package com.eduardo.fabs.models;

/**
 * Created by Eduardo on 02/08/2016.
 */

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
    private List<String> genre_names;
    private String id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private Double popularity;
    private Integer vote_count;
    private Boolean video;
    private Double vote_average;
    private Integer budget;
    private Integer revenue;
    private Integer runtime;
    private List<String> videosID;
    private List<String> videosName;

    private UserCategory userCategory;
    private Double userRating;

    public MovieModel(String id, String poster_path, String overview, String release_date, String title, Double popularity, Double vote_average) {
        this.id = id;
        this.poster_path = poster_path;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.popularity = popularity;
        this.vote_average = vote_average;
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
        this.genre_ids = new ArrayList<Integer>();
        this.genre_names = new ArrayList<String>();
        this.videosName = new ArrayList<String>();
        this.videosID = new ArrayList<String>();
        try{
            this.budget = movieJson.getInt(FetchMovies.BUDGET);
            this.revenue = movieJson.getInt(FetchMovies.REVENUE);
            this.runtime = movieJson.getInt(FetchMovies.RUNTIME);
            JSONArray genres = movieJson.getJSONArray(FetchMovies.GENRESA);
            for(int i = 0; i < genres.length(); i++){
                Integer genreID = genres.getJSONObject(i).getInt(FetchMovies.GENRESA_ID);
                String genreNAME = genres.getJSONObject(i).getString(FetchMovies.GENRESA_NAME);
                this.genre_ids.add(genreID);
                this.genre_names.add(genreNAME);
            }
            JSONArray videosArray = movieJson.getJSONObject(FetchMovies.VIDEOS).getJSONArray(FetchMovies.RESULTS);
            for(int i = 0; i < videosArray.length(); i++){
                String videoID = videosArray.getJSONObject(i).getString(FetchMovies.VIDEOS_ID);
                String videoNAME = videosArray.getJSONObject(i).getString(FetchMovies.VIDEOS_NAME);
                String videoTYPE = videosArray.getJSONObject(i).getString(FetchMovies.VIDEOS_TYPE);
                if(videoTYPE.equals("Trailer")){
                    this.videosID.add(videoID);
                    this.videosName.add(videoNAME);
                }
            }
        } catch (JSONException exception){
            JSONArray genres = movieJson.getJSONArray(FetchMovies.GENRESB);
            for(int i = 0; i < genres.length(); i++){
                Integer integer = (Integer) genres.get(i);
                this.genre_ids.add(integer);
            }
        }
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

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public List<String> getGenre_names() {
        return genre_names;
    }

    public void setGenre_names(List<String> genre_names) {
        this.genre_names = genre_names;
    }

    public List<String> getVideosName() {
        return videosName;
    }

    public void setVideosName(List<String> videosName) {
        this.videosName = videosName;
    }

    public List<String> getVideosID() {
        return videosID;
    }

    public void setVideosID(List<String> videosID) {
        this.videosID = videosID;
    }

    public String getImageFullURL(String size) {
        return Constants.TMDBConstants.IMAGE_BASE_URL + size + getPosterPath();
    }

    public String getRating() {
        return getVoteAverage() + "/" + Constants.TMDBConstants.RATING_MAX;
    }

    public Date getMovieReleaseDate() throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = inputFormat.parse(getReleaseDate());
        return date;
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "original_title='" + original_title + '\'' +
                '}';
    }
}