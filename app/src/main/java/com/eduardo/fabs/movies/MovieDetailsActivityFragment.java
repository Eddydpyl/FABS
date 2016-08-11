package com.eduardo.fabs.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.fetch.FetchMovies;
import com.eduardo.fabs.models.MovieModel;
import com.eduardo.fabs.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

// TODO: Work on Movie Details UI

public class MovieDetailsActivityFragment extends Fragment {

    private static String ID;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ID = getActivity().getIntent().getStringExtra(getString(R.string.intent_movie_id));
        try {
            MovieModel movieModel = new FetchMovies.FetchMovieDetailsTask(getContext()).execute(ID).get();

            ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
            String imageUrlStr = movieModel.getImageFullURL(Constants.TMDBConstants.IMAGE_MEDIUM_SIZE);
            Picasso.with(getContext()).load(imageUrlStr).into(poster);

            // TODO: Add interaction with user database

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
