package com.eduardo.fabs.presenter;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.ImageView;

import com.eduardo.fabs.R;

/**
 * Created by Eduardo on 22/09/2016.
 */

public class MovieActivityMethods {

    public static void setUpNavHeader(NavigationView navigationView, Context context){
        View headerView = navigationView.getHeaderView(0);
        final float scale = context.getResources().getDisplayMetrics().density;
        final int pixelsBig  = (int) (85 * scale + 0.5f);
        final int pixelsSmall  = (int) (55 * scale + 0.5f);
        final ImageView imageView_movies = (ImageView) headerView.findViewById(R.id.imageView_movies);
        final ImageView imageView_anime = (ImageView) headerView.findViewById(R.id.imageView_anime);
        final ImageView imageView_books = (ImageView) headerView.findViewById(R.id.imageView_books);
        final ImageView imageView_series= (ImageView) headerView.findViewById(R.id.imageView_series);
        imageView_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsBig;
                imageView_movies.getLayoutParams().width = pixelsBig;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsSmall;
                imageView_anime.getLayoutParams().width = pixelsSmall;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsSmall;
                imageView_books.getLayoutParams().width = pixelsSmall;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsSmall;
                imageView_series.getLayoutParams().width = pixelsSmall;
                imageView_series.requestLayout();
            }
        });
        imageView_anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsSmall;
                imageView_movies.getLayoutParams().width = pixelsSmall;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsBig;
                imageView_anime.getLayoutParams().width = pixelsBig;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsSmall;
                imageView_books.getLayoutParams().width = pixelsSmall;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsSmall;
                imageView_series.getLayoutParams().width = pixelsSmall;
                imageView_series.requestLayout();
            }
        });
        imageView_books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsSmall;
                imageView_movies.getLayoutParams().width = pixelsSmall;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsSmall;
                imageView_anime.getLayoutParams().width = pixelsSmall;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsBig;
                imageView_books.getLayoutParams().width = pixelsBig;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsSmall;
                imageView_series.getLayoutParams().width = pixelsSmall;
                imageView_series.requestLayout();
            }
        });
        imageView_series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView_movies.getLayoutParams().height = pixelsSmall;
                imageView_movies.getLayoutParams().width = pixelsSmall;
                imageView_movies.requestLayout();
                imageView_anime.getLayoutParams().height = pixelsSmall;
                imageView_anime.getLayoutParams().width = pixelsSmall;
                imageView_anime.requestLayout();
                imageView_books.getLayoutParams().height = pixelsSmall;
                imageView_books.getLayoutParams().width = pixelsSmall;
                imageView_books.requestLayout();
                imageView_series.getLayoutParams().height = pixelsBig;
                imageView_series.getLayoutParams().width = pixelsBig;
                imageView_series.requestLayout();
            }
        });
        // Set Movies as selected
        imageView_movies.getLayoutParams().height = pixelsBig;
        imageView_movies.getLayoutParams().width = pixelsBig;
        imageView_movies.requestLayout();
        imageView_anime.getLayoutParams().height = pixelsSmall;
        imageView_anime.getLayoutParams().width = pixelsSmall;
        imageView_anime.requestLayout();
        imageView_books.getLayoutParams().height = pixelsSmall;
        imageView_books.getLayoutParams().width = pixelsSmall;
        imageView_books.requestLayout();
        imageView_series.getLayoutParams().height = pixelsSmall;
        imageView_series.getLayoutParams().width = pixelsSmall;
        imageView_series.requestLayout();
    }

}
