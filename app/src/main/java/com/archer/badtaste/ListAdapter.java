package com.archer.badtaste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by archer on 2017-12-19.
 */

public class ListAdapter extends BaseAdapter {

    private List<Movie> movies;
    Context context;
    LayoutInflater layoutInflater;
    MainActivity mainActivity;

    public ListAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
        mainActivity = (MainActivity) context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        layoutInflater = LayoutInflater.from(this.context);

        final Movie movie = movies.get(i);
        final int position = i;
        view = layoutInflater.inflate(R.layout.movie_row_item, null);
        Picasso.with(context).load("https://image.tmdb.org/t/p/w154" + movie.getPoster()).into((ImageView)view.findViewById(R.id.posterView));
        ((TextView)view.findViewById(R.id.idView)).setText(String.valueOf(movie.getId()));
        ((TextView)view.findViewById(R.id.titleView)).setText(movie.getTitle());
        ((TextView)view.findViewById(R.id.genresView)).setText(movie.getGenres());
        ((RatingBar)view.findViewById(R.id.ratingBar)).setRating((float) movie.getPredicted());
        ((RatingBar)view.findViewById(R.id.ratingBar)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mainActivity.setCurrentItem(position);
                Toast.makeText(context, "Rating: " + v + " MOVIEID: " + movie.getId(), Toast.LENGTH_SHORT).show();
                mainActivity.contribute(v, movie.getId());
            }
        });
        return view;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
