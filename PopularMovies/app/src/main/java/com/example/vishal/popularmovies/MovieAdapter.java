package com.example.vishal.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 09-06-2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;



    public MovieAdapter(Activity context, List<Movie> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
        mContext = context;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";

        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.movie_poster,parent,false);
        }

        Movie currentMovie = getItem(position);

        ImageView mImageView = (ImageView) listItemView.findViewById(R.id.movie_poster_view);
        if(currentMovie != null) {

            Picasso.with(mContext).load(IMAGE_BASE_URL + currentMovie.posterPath).into(mImageView);
        }
        return listItemView;
    }

}
