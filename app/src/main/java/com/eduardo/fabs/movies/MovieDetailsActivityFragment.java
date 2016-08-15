package com.eduardo.fabs.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.fetch.FetchMovies;
import com.eduardo.fabs.models.MovieModel;
import com.eduardo.fabs.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

// TODO: Work on Movie Details UI

public class MovieDetailsActivityFragment extends Fragment {

    private static String ID;
    private static MovieModel movieModel;
    private static Spinner status;
    private static TextView episodes_seen;
    private static Spinner personal_score;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ID = getActivity().getIntent().getStringExtra(getString(R.string.intent_movie_id));
        // TODO: Add interaction with user database
        try {
            movieModel = new FetchMovies.FetchMovieDetailsTask(getContext()).execute(ID).get();

            ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
            String imageUrlStr = movieModel.getImageFullURL(Constants.TMDBConstants.IMAGE_MEDIUM_SIZE);
            Picasso.with(getContext()).load(imageUrlStr).into(poster);

            status = (Spinner) rootView.findViewById(R.id.status);
            List<String> list_status = new ArrayList<String>();
            list_status.add("");
            list_status.add("Completed");
            list_status.add("Plan to Watch");
            // Populate the spinner using a customized ArrayAdapter that hides the first (dummy) entry
            ArrayAdapter<String> adapter_status = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list_status) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent)
                {
                    View v = null;

                    // If this is the initial dummy entry, make it hidden
                    if (position == 0) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;
                    }
                    else {
                        // Pass convertView as null to prevent reuse of special case views
                        v = super.getDropDownView(position, null, parent);
                    }

                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    parent.setVerticalScrollBarEnabled(false);
                    return v;
                }
            };

            // Specify the layout to use when the list of choices appears
            adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            status.setAdapter(adapter_status);

            episodes_seen = (TextView) rootView.findViewById(R.id.episodes_seen);
            TextView episodes_count = (TextView) rootView.findViewById(R.id.episodes_count);
            Button increase_episodes = (Button) rootView.findViewById(R.id.increase_episodes);
            Button decrease_episodes = (Button) rootView.findViewById(R.id.decrease_episodes);

            personal_score = (Spinner) rootView.findViewById(R.id.personal_score);
            List<String> list_score = new ArrayList<String>();
            list_score.add("");
            list_score.add("(10) Masterpiece");
            list_score.add("(9) Great");
            list_score.add("(8) Very Good");
            list_score.add("(7) Good");
            list_score.add("(6) Fine");
            list_score.add("(5) Average");
            list_score.add("(4) Bad");
            list_score.add("(3) Very Bad");
            list_score.add("(2) Horrible");
            list_score.add("(1) Appalling");
            // Populate the spinner using a customized ArrayAdapter that hides the first (dummy) entry
            ArrayAdapter<String> adapter_score = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list_score) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent)
                {
                    View v = null;

                    // If this is the initial dummy entry, make it hidden
                    if (position == 0) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;
                    }
                    else {
                        // Pass convertView as null to prevent reuse of special case views
                        v = super.getDropDownView(position, null, parent);
                    }

                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    parent.setVerticalScrollBarEnabled(false);
                    return v;
                }
            };

            // Specify the layout to use when the list of choices appears
            adapter_score.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            personal_score.setAdapter(adapter_score);

            TextView title = (TextView) rootView.findViewById(R.id.title);
            title.setText(movieModel.getTitle());
            TextView overview = (TextView) rootView.findViewById(R.id.overview);
            overview.setText(movieModel.getOverview());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
