package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private final ArrayList<Movie> movies;

    public MovieListViewAdapter(ArrayList<Movie> movies, Context context) {
        super(context, R.layout.row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row, parent, false);

        Movie movie = movies.get(position);

        TextView titleView = view.findViewById(R.id.title);
        TextView subtitle1View = view.findViewById(R.id.subtitle1);
        TextView subtitle2View = view.findViewById(R.id.subtitle2);

        titleView.setText(movie.getName());
        // need to cast the year to a string to set the label
        subtitle1View.setText(movie.getRating() + " " +movie.getDirector() + " " + movie.getYear() + "");
        subtitle2View.setText(movie.getStars() + "");


        return view;
    }

}