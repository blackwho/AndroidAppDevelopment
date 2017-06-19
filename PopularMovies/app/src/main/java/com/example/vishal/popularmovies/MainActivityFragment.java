package com.example.vishal.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.Toolbar;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.R.attr.data;
import android.support.v7.widget.Toolbar;
/**
 * Created by Vishal on 08-06-2017.
 */

public class MainActivityFragment extends Fragment {


    private MovieAdapter mMovieAdapter;

    ProgressBar mProgressBarView;
    TextView mTextView;

   // private Boolean mScreenRotationDetector;
    private Boolean mLoadOnBackToMainActivityFragmentDetector = false;
    private Boolean value = true;

//    private Boolean onWindowsChangeDetector;

//    final String BASE_URL = "http://api.themoviedb.org/3/discover/movie/";

    final String BASE_URL = "https://api.themoviedb.org/3/movie/";


    final String API_KEY = "api_key";
    final String LANGUAGE_PARAM = "language";
    final String PAGE_PARAM = "page";


    final String API_KEY_VALUE = "4d68386fdfbb718a8c36cc1c1053c82e";
    final String LANGUAGE_PARAM_VALUE = "en-US";
    final String PAGE_PARAM_VALUE = "1";


    ArrayList<Movie> mMovieList ;
    ArrayList<Movie> dataMovieList;

    public MainActivityFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v("checking...", "onCreate()");

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")){

            mMovieList = new ArrayList<Movie>();
        }else{
            mMovieList = savedInstanceState.getParcelableArrayList("movies");
            Log.i ("mMovieList", mMovieList.size() + "");


        }




    }@Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("checking...","onCreateView()");
        setHasOptionsMenu(true);



        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        GridView mgridView = (GridView) rootView.findViewById(R.id.gridview);


        mTextView = (TextView) rootView.findViewById(R.id.empty_state);
        mProgressBarView = (ProgressBar) rootView.findViewById(R.id.loading_spinner);

        mMovieAdapter = new MovieAdapter(getActivity(), mMovieList);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        mgridView.setAdapter(mMovieAdapter);

        Log.i ("mMovieList", mMovieList.size() + "");



        if(mMovieList.isEmpty()) {
           loadMovieData(BASE_URL);
        }


        value = true;
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie currentMovie = mMovieList.get(position);
                String mOriginalTitle = currentMovie.originalTitle;
                String mReleaseDate = currentMovie.releaseDate;
                double mVoteAverage = currentMovie.userRating;
                String mPosterPath = currentMovie.posterPath;
                String mOverView = currentMovie.overview;

                Intent myIntent = new Intent(getActivity(), MovieDetailActivity.class);
                myIntent.putExtra("originalTitle", mOriginalTitle); //Optional parameters
                myIntent.putExtra("releaseDate", mReleaseDate);
                myIntent.putExtra("voteAverage", mVoteAverage);
                myIntent.putExtra("posterPath", mPosterPath);
                myIntent.putExtra("overView", mOverView);

                getActivity().startActivity(myIntent);
                mLoadOnBackToMainActivityFragmentDetector = true;


            }
        });


        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v("checking","onSaveInstanceState()");
        outState.putParcelableArrayList("movies", mMovieList);
        Log.i ("mMovieList", mMovieList.size() + "");
        super.onSaveInstanceState(outState);


    }

    public class GetMovieDataInBackground extends AsyncTask<String, Void, List<Movie> >{


        @Override
        protected void onPreExecute(){
            setProgressBarView();
        }

        @Override
        protected List<Movie> doInBackground(String...params){

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());// getActivity() is defined in the fragment class
            String sortBy = sharedPrefs.getString(
                    getString(R.string.settings_order_by_key),
                    getString(R.string.settings_order_by_default));

            Log.v("checking...", "sortBy" +sortBy);


            String stringUri = params[0];
            stringUri = stringUri + sortBy;

            Log.v("checking...", "stringUri" +stringUri);

            Uri baseUri = Uri.parse(stringUri);
            Uri.Builder uriBuilder = baseUri.buildUpon()
                                        .appendQueryParameter(API_KEY, API_KEY_VALUE)
                                        .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_PARAM_VALUE)
                                        .appendQueryParameter(PAGE_PARAM, PAGE_PARAM_VALUE);


            Uri queryUri = uriBuilder.build();

//            uriBuilder.appendQueryParameter(API_KEY, API_KEY_VALUE);
//            uriBuilder.appendQueryParameter(LANGUAGE_PARAM, LANGUAGE_PARAM_VALUE);
//            uriBuilder.appendQ
            String stringUriBuilder = queryUri.toString();

            Log.v("checking...", "stringUriBuilder" +stringUriBuilder);

            mMovieList = (ArrayList) NetworkUtils.fetchMovieData(stringUriBuilder);
            return mMovieList;
        }
        @Override
        protected void onPostExecute(List<Movie> data){

            if(data != null && !data.isEmpty()){
                mMovieAdapter.clear();
                mMovieAdapter.addAll(data);
                setNothing();
//                mMovieAdapter.clear();
            }else{
                int textValue = R.string.no_movie_data;
                setTextView(textValue);
            }

        }


    }

    public void setProgressBarView(){
        mTextView.setVisibility(View.INVISIBLE);
        mProgressBarView.setVisibility(View.VISIBLE);
    }

    public void setNothing(){
        mTextView.setVisibility(View.INVISIBLE);
        mProgressBarView.setVisibility(View.INVISIBLE);

    }

    public void setTextView(int param){

        mProgressBarView.setVisibility(View.INVISIBLE);
        if(param != 0) {
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(param);
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }


    public void loadMovieData(String stringUrl){

        Boolean param = isOnline();

        if(param) {
            GetMovieDataInBackground dataLoadInBackgroundTask = new GetMovieDataInBackground();
            dataLoadInBackgroundTask.execute(stringUrl);
        }else{
            setTextView(R.string.no_internet_connection);
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.v("checking...","onStart()");
//        MainActivityFragment test = (MainActivityFragment) getFragmentManager().findFragmentById(R.id.fragment);
//        if (!test.isVisible()) {
//            onWindowsChangeDetector = true;
//        }
//        else {
//
//        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.v("checking...","onResume()");
//        if(mScreenRotationDetector == false){
//            mMovieAdapter.addAll(mMovieList);
//        }
//        mScreenRotationDetector = false;

        if(!value){

            mMovieAdapter.clear();
            mMovieAdapter.addAll(mMovieList);
        }
        value = false;

        if(mLoadOnBackToMainActivityFragmentDetector){

            mMovieAdapter.clear();
            loadMovieData(BASE_URL);
            mLoadOnBackToMainActivityFragmentDetector = false;

        }

        Log.i ("mMovieList", mMovieList.size() + "");



    }

    @Override
    public void onPause(){
        super.onPause();
        Log.v("checking...","onPause()");
        Log.i ("mMovieList", mMovieList.size() + "");

    }



    @Override
    public void onStop(){
        super.onStop();
        Log.v("checking...","onStop()");
//        mMovieAdapter.clear();
        Log.i ("mMovieList", mMovieList.size() + "");


        //mMovieAdapter.clear();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("checking...","onDestroy()");
//        mMovieAdapter.clear();
        Log.i ("mMovieList", mMovieList.size() + "");




    }
    @Override
    public void onDetach(){
        super.onDetach();
        Log.v("checking...","onDetach()");
        Log.i ("mMovieList", mMovieList.size() + "");


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            mLoadOnBackToMainActivityFragmentDetector = true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
