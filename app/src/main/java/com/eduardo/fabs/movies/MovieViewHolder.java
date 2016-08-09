package com.eduardo.fabs.movies;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Eduardo on 05/08/2016.
 */

//TODO: Work on Movie List UI

public class MovieViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mTitle;
    public final TextView mOverview;
    public final ImageView mPoster;

    public MovieViewHolder(View view) {
        super(view);
        mView = view;
        mTitle = (TextView) view.findViewById(R.id.title);
        mOverview = (TextView) view.findViewById(R.id.overview);
        mPoster = (ImageView) view.findViewById(R.id.poster);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mTitle.getText() + "'";
    }

    public static void populateMovieModel(Context context, MovieViewHolder movieViewHolder, Cursor cursor) throws IOException {
        String imageUrlStr = Constants.TMDBConstants.IMAGE_BASE_URL + Constants.TMDBConstants.IMAGE_SMALL_SIZE + cursor.getString(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_POSTER_IMAGE);
        Picasso.with(context).load(imageUrlStr).into(movieViewHolder.mPoster);
        movieViewHolder.mTitle.setText(cursor.getString(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_TITLE));
        movieViewHolder.mOverview.setText(cursor.getString(DiscoverMoviesActivity.COL_DISCOVER_MOVIES_OVERVIEW));
    }
}