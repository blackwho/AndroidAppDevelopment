package com.example.vishal.popularmovies;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 10-06-2017.
 */

public final class NetworkUtils {


    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private NetworkUtils() {
    }

    public static List<Movie> fetchMovieData(String stringUrl){

        Log.v(LOG_TAG,"URL_VALUE" + stringUrl);

        URL url = createUrl(stringUrl);

        //Perform http request to the url and receive json response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }


        List<Movie> movie = extractMovieFeaturesFromJson(jsonResponse);


        return movie;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream=null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.v(LOG_TAG,"Yes,fetching data");
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Movie> extractMovieFeaturesFromJson(String response){

        if(TextUtils.isEmpty(response))
            return null;

        List<Movie> movieList = new ArrayList<>();


        try{

            JSONObject jsonObj = new JSONObject(response);
            JSONArray results = jsonObj.getJSONArray("results");

            for(int i = 0; i < results.length(); i++){

                JSONObject movie = results.getJSONObject(i);

                String mOriginalTitle = movie.getString("original_title");
                String mOverview = movie.getString("overview");
                double mUserRating = movie.getDouble("vote_average");
                String mReleaseDate = movie.getString("release_date");
                String mPosterPath = movie.getString("poster_path");

                movieList.add(new Movie(mOriginalTitle, mOverview, mUserRating, mReleaseDate, mPosterPath));

            }

        }catch(JSONException e){
            Log.e("NetworkUtils", "Problem parsing the earthquake JSON results", e);

        }

        return movieList;
    }




}
