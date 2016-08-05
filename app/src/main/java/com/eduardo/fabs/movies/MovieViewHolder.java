package com.eduardo.fabs.movies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eduardo.fabs.R;

/**
 * Created by Eduardo on 05/08/2016.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;

    public MovieViewHolder(View view) {
        super(view);
        mView = view;
        mIdView = (TextView) view.findViewById(R.id.id);
        mContentView = (TextView) view.findViewById(R.id.content);
    }

    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }
}