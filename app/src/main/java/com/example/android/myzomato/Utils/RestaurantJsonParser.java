package com.example.android.myzomato.Utils;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_AVERAGE_COST;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_CUISINES;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_FAVORITE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_ID;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_IMAGE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_LATITUDE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_LONGITUDE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_NAME;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_RATING;

/**
 * Created by PepovPC on 10/15/2017.
 */

public class RestaurantJsonParser {
    public static ContentValues[] getRestaurantDataFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_ALL_RESTAURANTS = "restaurants";
        final String OWM_RESTAURANT = "restaurant";
        final String OWM_ID = "id";
        final String OWM_NAME = "name";
        final String OWM_CUISIN = "cuisines";
        final String OWM_AVG_COST = "average_cost_for_two";
        final String OWM_IMAGE = "featured_image";

        final String OWM_LOCATION = "location";
        final String OWM_LAT = "latitude";
        final String OWM_LON = "longitude";

        final String OWM_RATING = "user_rating";
        final String OWM_AVG_RATING = "aggregate_rating";



        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        // JSONArray ked mas v JSONe znam "["
        JSONArray restaurantArray = forecastJson.getJSONArray(OWM_ALL_RESTAURANTS);

        int op = restaurantArray.length();
        ContentValues[] restaurantContentValues = new ContentValues[restaurantArray.length()];

        for(int i = 0; i < restaurantArray.length(); i++) {
            JSONObject restaurant = restaurantArray.getJSONObject(i);
            JSONObject restaurantData = restaurant.getJSONObject(OWM_RESTAURANT);
            int id = restaurantData.getInt(OWM_ID);
            String name = restaurantData.getString(OWM_NAME);
            String cuisines = restaurantData.getString(OWM_CUISIN);
            int cost = restaurantData.getInt(OWM_AVG_COST);
            String image = restaurantData.getString(OWM_IMAGE);

            JSONObject locationData = restaurantData.getJSONObject(OWM_LOCATION);
            Double latitude = locationData.getDouble(OWM_LAT);
            Double longitude = locationData.getDouble(OWM_LON);

            JSONObject ratingData = restaurantData.getJSONObject(OWM_RATING);
            Double rating = ratingData.getDouble(OWM_AVG_RATING);

            ContentValues restaurantValues = new ContentValues();
            restaurantValues.put(COLUMN_ID, id);
            restaurantValues.put(COLUMN_NAME, name);
            restaurantValues.put(COLUMN_CUISINES, cuisines);
            restaurantValues.put(COLUMN_AVERAGE_COST, cost);
            restaurantValues.put(COLUMN_IMAGE, image);
            restaurantValues.put(COLUMN_LATITUDE, latitude);
            restaurantValues.put(COLUMN_LONGITUDE, longitude);
            restaurantValues.put(COLUMN_RATING, rating);
            restaurantValues.put(COLUMN_FAVORITE, 0);

            restaurantContentValues[i] = restaurantValues;

        }
        return restaurantContentValues;
    }


//    public static List<List<String>> getTrailers(String id, List<List<String>> trailers)
//            throws JSONException {
//
//        final String OWM_RESULT = "results";
//        final String OWM_KEY = "key";
//
//        JSONObject trailerJson = new JSONObject(id);
//
//        // JSONArray ked mas v JSONe znam "["
//        JSONArray weatherArray = trailerJson.getJSONArray(OWM_RESULT);
//
//        for(int i = 0; i < 2; i++) {
//            // JSONObject ked mas v JSONe znam "{"
//            JSONObject movieForecast = weatherArray.getJSONObject(i);
//            String key = movieForecast.getString(OWM_KEY);
//            List<String> x = new ArrayList<String>();
//            x.add(key);
//            trailers.add(x);
//        }
//
//        return trailers;
//    }
//
//
//    public static List<List<String>> getReviews(String reviewJson, List<List<String>> reviews)
//            throws JSONException {
//
//
//
//        final String OWM_RESULT = "results";
//        final String OWM_AUTHOR = "author";
//        final String OWM_COMMENT = "content";
//
//        JSONObject trailerJson = new JSONObject(reviewJson);
//
//        // JSONArray ked mas v JSONe znam "["
//        JSONArray weatherArray = trailerJson.getJSONArray(OWM_RESULT);
//
//        for(int i = 0; i < weatherArray.length(); i++) {
//            // JSONObject ked mas v JSONe znam "{"
//            JSONObject movieForecast = weatherArray.getJSONObject(i);
//            String author = movieForecast.getString(OWM_AUTHOR);
//            String comment = movieForecast.getString(OWM_COMMENT);
//            List<String> x = new ArrayList<>();
//            x.add(author);
//            x.add(comment);
//            reviews.add(x);
//        }
//
//        return reviews;
//    }



}