package com.example.android.myzomato.Utils;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_AVERAGE_COST;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_CUISINES;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_FAVORITE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_ID;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_IMAGE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_LATITUDE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_LONGITUDE;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_NAME;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_RATING;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry.COLUMN_STREET;

/**
 * Created by PepovPC on 10/15/2017.
 */

public class RestaurantJsonParser {

    public static int number_of_results;

    public static ContentValues[] getRestaurantDataFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String RESULTS_FOUND = "results_found";

        final String OWM_ALL_RESTAURANTS = "restaurants";
        final String OWM_RESTAURANT = "restaurant";
        final String OWM_ID = "id";
        final String OWM_NAME = "name";
        final String OWM_CUISIN = "cuisines";
        final String OWM_AVG_COST = "average_cost_for_two";
        final String OWM_IMAGE = "featured_image";

        final String OWM_LOCATION = "location";
        final String OWM_STREET = "locality_verbose";
        final String OWM_LAT = "latitude";
        final String OWM_LON = "longitude";

        final String OWM_RATING = "user_rating";
        final String OWM_AVG_RATING = "aggregate_rating";



        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        number_of_results = forecastJson.getInt(RESULTS_FOUND);

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
            String street = locationData.getString(OWM_STREET);
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
            restaurantValues.put(COLUMN_STREET, street);
            restaurantValues.put(COLUMN_LATITUDE, latitude);
            restaurantValues.put(COLUMN_LONGITUDE, longitude);
            restaurantValues.put(COLUMN_RATING, rating);
            restaurantValues.put(COLUMN_FAVORITE, 0);

            restaurantContentValues[i] = restaurantValues;

        }
        return restaurantContentValues;
    }


    public static List<List<String>> getMenuDataFromJson(String urlResponse) throws JSONException {

        final String OWM_DAILY_MENUS = "daily_menus";
        final String OWM_DAILY_MENU = "daily_menu";
        final String OWM_DISHES = "dishes";
        final String OWM_DISH = "dish";

        final String OWM_NAME = "name";
        final String OWM_PRICE = "price";

        List<List<String>> menus = new ArrayList<>();

        JSONObject forecastJson = new JSONObject(urlResponse);
        JSONArray menuArray = forecastJson.getJSONArray(OWM_DAILY_MENUS);

        if(menuArray.length() == 0){
            return null;
        }

        JSONObject menu = menuArray.getJSONObject(0);
        JSONObject restaurantData = menu.getJSONObject(OWM_DAILY_MENU);
        JSONArray dishes = restaurantData.getJSONArray(OWM_DISHES);

        for(int i = 0; i < dishes.length(); i++){
            JSONObject dish = dishes.getJSONObject(i);
            JSONObject dishData = dish.getJSONObject(OWM_DISH);
            String name = dishData.getString(OWM_NAME);
            String price = dishData.getString(OWM_PRICE);

            List<String> x = new ArrayList<>();
            x.add(name);
            x.add(price);
            menus.add(x);
        }

        return menus;
    }

}