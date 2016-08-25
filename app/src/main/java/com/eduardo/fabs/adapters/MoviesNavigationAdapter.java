package com.eduardo.fabs.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eduardo.fabs.R;

/**
 * Created by Eduardo on 19/08/2016.
 */
public class MoviesNavigationAdapter extends ArrayAdapter<String> {

    public MoviesNavigationAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String string = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null || convertView.findViewById(R.id.textView)==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.nav_item, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iconImage);
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(string);
        // Use position to determine if parent, child or divider
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        switch (position){
            case 0:{
                imageView.setImageResource(R.drawable.ic_mymovies);
                params.setMargins(0, 5, 0, 0);
                textView.setLayoutParams(params);
                textView.setTypeface(null, Typeface.BOLD);
                break;
            } case 3:{
                imageView.setImageResource(R.drawable.ic_movies);
                params.setMargins(0, 5, 0, 0);
                textView.setLayoutParams(params);
                textView.setTypeface(null, Typeface.BOLD);
                convertView.setOnClickListener(null);
                break;
            } case 8:{
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.nav_divider, parent, false);
                convertView.setOnClickListener(null);
                break;
            } case 9:{
                imageView.setImageResource(R.drawable.ic_settings);
                params.setMargins(0, 5, 0, 0);
                textView.setLayoutParams(params);
                textView.setTypeface(null, Typeface.BOLD);
                break;
            } default:{
                imageView.setVisibility(View.GONE);
                params.setMargins(60, 0, 0, 0);
                textView.setLayoutParams(params);
                break;
            }
        }
        return convertView;
    }
}
