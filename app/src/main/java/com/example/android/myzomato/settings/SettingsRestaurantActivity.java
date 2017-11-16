package com.example.android.myzomato.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.myzomato.R;
import com.example.android.myzomato.utils.HelperClass;
import com.example.android.myzomato.detail.DetailActivity;

import java.util.Set;

import static com.example.android.myzomato.all_restaurants.AllRestaurantFragment.MAIN_RESTAURANT_PROJECTION;
import static com.example.android.myzomato.data.RestaurantTableContents.RestaurantEntry;


public class SettingsRestaurantActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SettingsRestaurantAdapter.ForecastAdapterOnClickHandler{

    private static final int LOADER_ID = 3;
    private SettingsRestaurantAdapter restaurantAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;

    private ProgressBar mLoadingIndicator;
    public static final String KEY_PREF_RATING_CHECKBOX = "rating_checkbox";
    public static final String KEY_PREF_PRICE_CHECKBOX = "price_checkbox";
    private Boolean switchPrefRating = false;
    private Boolean switchPrefPrice = false;
    Set<String> selections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_restaurant_activity);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        showLoading();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        restaurantAdapter = new SettingsRestaurantAdapter(this, this);
        mRecyclerView.setAdapter(restaurantAdapter);

        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        switchPrefRating = sharedPref.getBoolean(KEY_PREF_RATING_CHECKBOX, false);
        switchPrefPrice = sharedPref.getBoolean(KEY_PREF_PRICE_CHECKBOX, false);

        this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }


    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        switch (id) {

            case LOADER_ID:
                /* URI for all rows of all data in our weather table */
                Uri forecastQueryUri = RestaurantEntry.CONTENT_URI;
                String sortOrder = null;
                String select = null;


                if(switchPrefRating == true){

                    sortOrder = RestaurantEntry.COLUMN_RATING + " DESC LIMIT 40";

                }else if(switchPrefPrice == true){

                    sortOrder = RestaurantEntry.COLUMN_AVERAGE_COST + " ASC LIMIT 40";
                }

                select = returnSelectOfCusines(select);

                System.out.println(select);

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_RESTAURANT_PROJECTION,
                        select,
                        null,
                        sortOrder);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // maybe check of there are not any matches?!?
        restaurantAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        int i = data.getCount();
        if(data.getCount() != 0)
            showData();
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        restaurantAdapter.swapCursor(null);
    }


    @Override
    public void onClick(int id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    private String returnSelectOfCusines(String select) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        selections = preferences.getStringSet("cuisines_list", null);
        if(selections != null) {
            String[] selected = selections.toArray(new String[]{});
            for (int i = 0; i < selected.length; i++) {
                if(i==0)
                    select = "";
                System.out.println("\ntest" + i + " : " + selected[i]);
                select += RestaurantEntry.COLUMN_CUISINES + " LIKE '%"
                        + HelperClass.sendCuisineName(Integer.parseInt(selected[i])) + "%'";
                if(i!=selected.length-1)
                    select += " AND ";
            }
        }
        return select;
    }

}
