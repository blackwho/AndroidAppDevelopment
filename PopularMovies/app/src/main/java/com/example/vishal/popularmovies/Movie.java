package com.example.vishal.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.AbsListView;

import java.util.Date;

/**
 * Created by Vishal on 08-06-2017.
 */

public class Movie implements Parcelable {
    String originalTitle;
//    int imageResourceId;
    String overview;
    double userRating;
    String releaseDate;
    String posterPath;

    public Movie(String mOriginalTitle, String mOverview, double mUserRating, String mReleaseDate ,String mPosterPath){
        this.originalTitle = mOriginalTitle;
//        this.imageResourceId = mImageResourceId;
        this.overview = mOverview;
        this.userRating = mUserRating;
        this.releaseDate = mReleaseDate;
        this.posterPath = mPosterPath;
    }

    private Movie(Parcel input){
        originalTitle = input.readString();
//        imageResourceId = input.readInt();
        overview = input.readString();
        userRating = input.readDouble();
        releaseDate = input.readString();
        posterPath = input.readString();
    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeDouble(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };
}
