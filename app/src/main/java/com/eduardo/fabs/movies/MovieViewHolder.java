package com.eduardo.fabs.movies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.models.MovieModel;
import com.eduardo.fabs.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Eduardo on 05/08/2016.
 */

public class MovieViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

    private final static Uri URI = FABSContract.MY_MOVIES_TABLE.CONTENT_URI;
    private final static String SELECTION = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE._ID + " = ? ";

    public final View mView;
    public final CardView cardView;
    public final TextView ID;
    public final TextView mTitle;
    public final TextView mOverview;
    public final ImageView mPoster;
    public final RatingBar mRatingBar;

    public MovieViewHolder(View view) {
        super(view);
        mView = view;
        cardView = (CardView) view.findViewById(R.id.card_view);
        ID = (TextView) view.findViewById(R.id.ID);
        mTitle = (TextView) view.findViewById(R.id.title);
        mOverview = (TextView) view.findViewById(R.id.overview);
        mPoster = (ImageView) view.findViewById(R.id.poster);
        mRatingBar = (RatingBar) view.findViewById(R.id.rating);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTitle.getText() + "'";
    }

    public static void populateMovieModel(Context context, MovieViewHolder movieViewHolder, Cursor cursor) throws IOException {
        String imageUrlStr = Constants.TMDBConstants.IMAGE_BASE_URL + Constants.TMDBConstants.IMAGE_SMALL_SIZE + cursor.getString(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_POSTER_IMAGE);
        Picasso.with(context).load(imageUrlStr).into(movieViewHolder.mPoster);
        Integer _id = cursor.getInt(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_ID);
        movieViewHolder.ID.setText(_id.toString());
        movieViewHolder.mTitle.setText(cursor.getString(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_TITLE));
        movieViewHolder.mOverview.setText(cursor.getString(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_OVERVIEW));
        Cursor myCursor = context.getContentResolver().query(URI, MyMoviesActivity.MY_MOVIES_COLUMNS, SELECTION, new String[]{_id.toString()}, null);
        // If the movie exists in the user database, load the pertinent info
        if(myCursor.getCount()==1){
            myCursor.moveToFirst();
            if((Integer)myCursor.getInt(MyMoviesActivity.COL_MY_MOVIES_USER_RATING)!=null){
                Float rating = Float.valueOf(myCursor.getInt(MyMoviesActivity.COL_MY_MOVIES_USER_RATING))/2;
                movieViewHolder.mRatingBar.setRating(rating);
            }
        } else{
            movieViewHolder.mRatingBar.setRating(0);
        } myCursor.close();
    }

    public static void populateMovieModel(Context context, MovieViewHolder movieViewHolder, MovieModel movieModel) throws IOException {
        String imageUrlStr = movieModel.getImageFullURL(Constants.TMDBConstants.IMAGE_SMALL_SIZE);
        Picasso.with(context).load(imageUrlStr).into(movieViewHolder.mPoster);
        String _id = movieModel.getId();
        movieViewHolder.ID.setText(_id);
        movieViewHolder.mTitle.setText(movieModel.getTitle());
        movieViewHolder.mOverview.setText(movieModel.getOverview());
        Cursor myCursor = context.getContentResolver().query(URI, MyMoviesActivity.MY_MOVIES_COLUMNS, SELECTION, new String[]{_id.toString()}, null);
        // If the movie exists in the user database, load the pertinent info
        if(myCursor.getCount()==1){
            myCursor.moveToFirst();
            if((Integer)myCursor.getInt(MyMoviesActivity.COL_MY_MOVIES_USER_RATING)!=null){
                Float rating = Float.valueOf(myCursor.getInt(MyMoviesActivity.COL_MY_MOVIES_USER_RATING))/2;
                movieViewHolder.mRatingBar.setRating(rating);
            }
        } else{
            movieViewHolder.mRatingBar.setRating(0);
        } myCursor.close();
    }
}