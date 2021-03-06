package com.eduardo.fabs;

/**
 * Created by Eduardo on 02/08/2016.
 */

public final class Constants {

    public final static String CONTENT_AUTHORITY = "com.eduardo.fabs";
    public static final String ACCOUNT_TYPE = "fabs.eduardo.com";

    public final static class TMDBConstants {
        public static final String BASE_URL = "http://api.themoviedb.org/3/";
        public final static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
        public final static String MOVIE_TAG = "movie";
        public final static String SERIES_TAG = "tv";
        public final static String DISCOVER_TAG = "discover";
        public final static String SEARCH_TAG = "search";
        public final static String QUERY_PARAM = "query=";
        public static final String API_KEY_QUERY_PARAM = "api_key=";
        //TODO: This is a dummy API key that, while functional, will not be used in the release version
        public static final String API_KEY = "5e06959ff0b33747049b1ac2295f4aac";

        public final static String REQUEST_POPULAR = "popular";
        public final static String REQUEST_TOP_RATED = "top_rated";
        public final static String REQUEST_LATEST = "latest";
        public final static String REQUEST_UPCOMING_MOVIES = "upcoming";
        public final static String REQUEST_MOVIES_IN_THEATERS = "now_playing";
        public final static String REQUEST_SERIES_ON_THE_AIR = "on_the_air";
        public final static String REQUEST_SERIES_AIRING_TODAY = "airing_today";
        public final static String REQUEST_VIDEOS = "videos";

        public final static String IMAGE_TINY_SIZE = "w92";
        public final static String IMAGE_VERY_SMALL_SIZE = "w154";
        public final static String IMAGE_SMALL_SIZE = "w185";
        public final static String IMAGE_MEDIUM_SIZE = "w342";
        public final static String IMAGE_BIG_SIZE = "w500";
        public final static String IMAGE_HUGE_SIZE = "w780";
        public final static String IMAGE_ORIGINAL_SIZE = "original";

        public final static String PARAM_PAGE = "page=";
        public final static  String APPEND_TO_RESPONSE = "append_to_response=";

        public final static String RATING_MAX = "10";
    }
}