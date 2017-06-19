package com.example.vishal.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static android.R.attr.defaultValue;
import static android.R.attr.value;

public class MovieDetailActivity extends AppCompatActivity {

    TextView mOriginalTitleText;
    TextView mReleaseDateTextView;
    TextView mOverviewText;
    ImageView mPosterImageView;
    TextView mVoteAverageTextView;

    private final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        String originalTitleParam = intent.getStringExtra("originalTitle");
        String releaseDateParam = intent.getStringExtra("releaseDate");
        String overviewParam = intent.getStringExtra("overView");
        String posterPathParam = intent.getStringExtra("posterPath");
        double voteAverageParam = intent.getDoubleExtra("voteAverage",defaultValue);


        mOriginalTitleText = (TextView) findViewById(R.id.original_title_text_view);
        mPosterImageView = (ImageView) findViewById(R.id.poster_image_view);
        mReleaseDateTextView = (TextView) findViewById(R.id.release_date_view_value);
        mVoteAverageTextView = (TextView) findViewById(R.id.vote_average_view_value);
        mOverviewText = (TextView) findViewById(R.id.overview_text_view);





        mOriginalTitleText.setText(originalTitleParam);
        Picasso.with(this).load(IMAGE_BASE_URL + posterPathParam).into(mPosterImageView);
        mOverviewText.setText(overviewParam);
        mReleaseDateTextView.setText(releaseDateParam);
        mVoteAverageTextView.setText(String.valueOf(voteAverageParam));



    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
