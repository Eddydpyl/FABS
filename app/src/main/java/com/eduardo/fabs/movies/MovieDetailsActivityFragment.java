package com.eduardo.fabs.movies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.data.FABSContract;
import com.eduardo.fabs.fetch.FetchMovies;
import com.eduardo.fabs.models.MovieModel;
import com.eduardo.fabs.utils.Constants;
import com.eduardo.fabs.utils.UserCategory;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

// TODO: Work on Movie Details UI

public class MovieDetailsActivityFragment extends Fragment {

    private static String ID;
    private static MovieModel movieModel;
    private static Spinner status;
    private static TextView episodes_seen;
    private static Spinner personal_score;

    private final static Uri URI = FABSContract.MY_MOVIES_TABLE.CONTENT_URI;
    private final static String SELECTION = FABSContract.MY_MOVIES_TABLE.TABLE_NAME + "." + FABSContract.MY_MOVIES_TABLE._ID + " = ? ";

    public MovieDetailsActivityFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.movie_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_mymovie) {
            if(status.getSelectedItemPosition()!=0){
                status.setSelection(0);
                episodes_seen.setText("0");
                personal_score.setSelection(0);
                personal_score.setEnabled(false);
                getActivity().getContentResolver().delete(FABSContract.MY_MOVIES_TABLE.CONTENT_URI, SELECTION, new String[] {ID});
            }
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ID = getActivity().getIntent().getStringExtra(getString(R.string.intent_movie_id));
        Cursor cursor = getActivity().getContentResolver().query(URI, MyMoviesActivity.MY_MOVIES_COLUMNS, SELECTION, new String[]{ID}, null);

        try {
            movieModel = new FetchMovies.FetchMovieDetailsTask().execute(ID).get();

            ImageView poster = (ImageView) rootView.findViewById(R.id.poster);
            String imageUrlStr = movieModel.getImageFullURL(Constants.TMDBConstants.IMAGE_MEDIUM_SIZE);
            Picasso.with(getContext()).load(imageUrlStr).into(poster);

            episodes_seen = (TextView) rootView.findViewById(R.id.episodes_seen);

            final Button increase_episodes = (Button) rootView.findViewById(R.id.increase_episodes);
            increase_episodes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    episodes_seen.setText("1");
                    status.setSelection(1);
                    personal_score.setEnabled(true);
                    updateUserDatabase(getActivity().getContentResolver());
                }
            });
            final Button decrease_episodes = (Button) rootView.findViewById(R.id.decrease_episodes);
            decrease_episodes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    episodes_seen.setText("0");
                    if(status.getSelectedItemPosition()==1){
                        status.setSelection(2);
                        personal_score.setEnabled(false);
                        updateUserDatabase(getActivity().getContentResolver());
                    }
                }
            });

            status = (Spinner) rootView.findViewById(R.id.status);
            List<String> list_status = Arrays.asList(getResources().getStringArray(R.array.list_movie_status));
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
            // Set the Listener
            status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(i == 1){
                        episodes_seen.setText("1");
                        increase_episodes.setClickable(true);
                        decrease_episodes.setClickable(true);
                        personal_score.setEnabled(true);
                        updateUserDatabase(getActivity().getContentResolver());
                    } else if(i == 2){
                        episodes_seen.setText("0");
                        increase_episodes.setClickable(true);
                        decrease_episodes.setClickable(true);
                        personal_score.setEnabled(false);
                        updateUserDatabase(getActivity().getContentResolver());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            personal_score = (Spinner) rootView.findViewById(R.id.personal_score);
            List<String> list_score = Arrays.asList(getResources().getStringArray(R.array.list_personal_score));
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
            // Set the Listener
            personal_score.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i != 0){
                        updateUserDatabase(getActivity().getContentResolver());
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            // If the movie exists in the user database, load the pertinent info
            if(cursor.getCount()==1){
                cursor.moveToFirst();
                if(cursor.getString(MyMoviesActivity.COL_MY_MOVIES_USER_CATEGORY).toString().equals(UserCategory.COMPLETED.toString())){
                    status.setSelection(1);
                } else{
                    status.setSelection(2);
                }
                if(status.getSelectedItemPosition()==1){
                    episodes_seen.setText("1");
                } else {
                    episodes_seen.setText("0");
                }
                if((Integer)cursor.getInt(MyMoviesActivity.COL_MY_MOVIES_USER_RATING)!=null){
                    personal_score.setSelection(cursor.getInt(MyMoviesActivity.COL_MY_MOVIES_USER_RATING));
                }
            }

            // Fill in with the rest of the retrieved info
            TextView title = (TextView) rootView.findViewById(R.id.title);
            title.setText(movieModel.getTitle());
            TextView overview = (TextView) rootView.findViewById(R.id.overview);
            overview.setText(movieModel.getOverview());
            TextView releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
            if(movieModel.getReleaseDate().isEmpty()){
                releaseDate.setText("-");
            } else{
                releaseDate.setText(movieModel.getReleaseDate());
            }
            TextView voteAverage = (TextView) rootView.findViewById(R.id.voteAverage);
            voteAverage.setText(movieModel.getRating());
            TextView budget = (TextView) rootView.findViewById(R.id.budget);
            if(movieModel.getBudget()!=0){
                budget.setText(movieModel.getBudget().toString() + "$");
            } else{
                budget.setText("-");
            }
            TextView revenue = (TextView) rootView.findViewById(R.id.revenue);
            if(movieModel.getRevenue()!=0){
                revenue.setText(movieModel.getRevenue().toString() + "$");
            } else{
                revenue.setText("-");
            }
            TextView genres = (TextView) rootView.findViewById(R.id.genres);
            List<String> genreList = movieModel.getGenre_names();
            if(genreList.isEmpty()){
                genres.setText("-");
            } else {
                String genreString = genreList.get(0);
                for(int i = 1; i < genreList.size(); i++){
                    genreString.concat("," + genreList.get(i));
                }
                genres.setText(genreString);
            }

            // Create a list of trailers that launch the youtube app
            LinearLayout trailers = (LinearLayout) rootView.findViewById(R.id.trailers);

            class TrailerAdapter extends ArrayAdapter<String>{

                List<String> trailerTitles;

                public TrailerAdapter(Context context, int resource, List<String> objects) {
                    super(context, resource, objects);
                    trailerTitles = objects;
                }


                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = convertView;
                    if (view == null) {
                        view = inflater.inflate(R.layout.list_item_trailer, parent, false);
                    }
                    ImageView imageView = (ImageView) view.findViewById(R.id.trailer_icon);
                    TextView textView = (TextView) view.findViewById(R.id.trailer_title);
                    textView.setText(trailerTitles.get(position));
                    return view;
                }
            }
            TrailerAdapter trailerAdapter = new TrailerAdapter(getContext(), android.R.layout.simple_list_item_1, movieModel.getVideosName());
            for(int i = 0; i < movieModel.getVideosName().size(); i++){
                final int j = i;
                View view = trailerAdapter.getView(i, null, null);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String trailerID = movieModel.getVideosID().get(j);
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerID));
                        startActivity(i);
                    }
                });
                trailers.addView(view);
            }

            //TODO: Add some reviews

            // Disable score field if user has not chosen a status yet
            if(status.getSelectedItemPosition() == 0){
                personal_score.setEnabled(false);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private static void updateUserDatabase(ContentResolver contentResolver){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FABSContract.MY_MOVIES_TABLE._ID, Integer.valueOf(ID));
        contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_POSTER_IMAGE, movieModel.getPosterPath());
        contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_OVERVIEW, movieModel.getOverview());
        contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_RELEASE_DATE, movieModel.getReleaseDate());
        contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_TITLE, movieModel.getTitle());
        contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_POPULARITY, movieModel.getPopularity());
        contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_VOTE_AVERAGE, movieModel.getVoteAverage());
        if(personal_score.getSelectedItemPosition()!=0){
            contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_USER_RATING, personal_score.getSelectedItemPosition());
        }
        switch (status.getSelectedItemPosition()){
            case 1:
                contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY, UserCategory.COMPLETED.toString());
                break;
            case 2:
                contentValues.put(FABSContract.MY_MOVIES_TABLE.COLUMN_USER_CATEGORY, UserCategory.PLANTOWATCH.toString());
                break;
        }
        if(contentResolver.update(FABSContract.MY_MOVIES_TABLE.CONTENT_URI, contentValues, SELECTION, new String[] {ID}) == 0){
            contentResolver.insert(FABSContract.MY_MOVIES_TABLE.CONTENT_URI, contentValues);
        }
    }
}
